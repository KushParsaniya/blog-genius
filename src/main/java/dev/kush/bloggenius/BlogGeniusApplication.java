package dev.kush.bloggenius;

import dev.kush.bloggenius.config.DevToProperties;
import dev.kush.bloggenius.config.SlackProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {DevToProperties.class, SlackProperties.class})
public class BlogGeniusApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogGeniusApplication.class, args);
    }

}
