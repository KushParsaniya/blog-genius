package dev.kush.bloggenius.config;

import dev.kush.bloggenius.clients.DevToClient;
import dev.kush.bloggenius.clients.SlackClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {

    @Bean
    DevToClient devToClient(DevToProperties devToProperties) {
        RestClient restClient = RestClient.builder()
                .baseUrl(devToProperties.getBaseUrl())
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .build();

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(DevToClient.class);
    }

    @Bean
    @ConditionalOnProperty(name = "slack.incoming-webhook.enabled", havingValue = "true")
    SlackClient slackClient(SlackProperties slackProperties) {
        RestClient restClient = RestClient.builder()
                .baseUrl(slackProperties.getUrl())
                .build();

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(SlackClient.class);
    }
}
