package dev.kush.bloggenius.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class DevToModels {


    public record DevToPostRequest(DevToPostRequestArticle article) implements Serializable {
    }

    public record DevToPostRequestArticle(
            String title, @JsonProperty("body_markdown") String bodyMarkdown, boolean published, String description,
            String[] tags,
            @JsonProperty("main_image") String mainImage,
            @JsonProperty("canonical_url") String canonicalUrl,
            @JsonProperty("organization_id") int organizationId
    ) implements Serializable {
        public static DevToPostRequestArticle of(String title, String body, String description, String[] tags) {
            return new DevToPostRequestArticle(title, body, false, description, tags, "", "", 0);
        }

        public static DevToPostRequestArticle of(DevToPostRequestArticle blog, String bodyMarkdown) {
            return new DevToPostRequestArticle(blog.title(), bodyMarkdown, blog.published(), blog.description(), blog.tags(), blog.mainImage(), blog.canonicalUrl(), blog.organizationId());
        }
    }

    public record DevToPostResponse(
            String typeOf, long id, String title, String description, String url, Instant createdAt
    ) implements Serializable {
    }

    public record DevToFactCheckResponse(boolean isFactuallyCorrect,
                                         List<DevToFactCheckInfo> inaccuracies) implements Serializable {
    }

    public record DevToFactCheckInfo(String statement, String correction, String source) implements Serializable {
    }
}
