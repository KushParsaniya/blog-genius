package dev.kush.bloggenius.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "dev.to")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DevToProperties {

    private String apiKey;
    private String baseUrl;

}
