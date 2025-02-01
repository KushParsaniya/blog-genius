package dev.kush.bloggenius.clients;

import dev.kush.bloggenius.models.SlackModels;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(accept = "application/json", contentType = "application/json")
public interface SlackClient {

    @PostExchange
    void publishToSlackChannel(
            @RequestBody SlackModels.SlackSendMessage slackSendMessage
            );
}
