package dev.kush.bloggenius.service;

import dev.kush.bloggenius.clients.SlackClient;
import dev.kush.bloggenius.models.SlackModels;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class SlackListener {

    private final Optional<SlackClient> slackClient;

    @EventListener(SlackModels.SlackSendMessage.class)
    public void sendMessage(SlackModels.SlackSendMessage message) {
        try {
            slackClient.ifPresent(client -> {
                log.info("SlackListener :: sendMessage :: Sending message: {}", message.text());
                client.publishToSlackChannel(message);
            });
        } catch (Exception e) {
            log.error("SlackListener :: sendMessage :: Error sending message: {}", e.getMessage());
        }
    }
}
