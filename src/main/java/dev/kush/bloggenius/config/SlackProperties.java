package dev.kush.bloggenius.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "slack.incoming-webhook")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SlackProperties {

    private String url;
    private String enabled;

}