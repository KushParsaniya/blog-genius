package dev.kush.bloggenius.clients;

import dev.kush.bloggenius.models.DevToModels;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/api", accept = "application/json", contentType = "application/json")
public interface DevToClient {

    @PostExchange("/articles")
    DevToModels.DevToPostResponse publishToDevTo(
            @RequestBody DevToModels.DevToPostRequest article
            ,@RequestHeader(name = "api-key") String apiKey
    );
}