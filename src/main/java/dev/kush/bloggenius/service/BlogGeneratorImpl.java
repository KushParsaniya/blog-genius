package dev.kush.bloggenius.service;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kush.bloggenius.models.DevToModels;
import dev.kush.bloggenius.repos.PublishedBlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogGeneratorImpl implements BlogGenerator {

    private final ChatClient chatClient;
    private final PublishedBlogRepository publishedBlogRepository;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature())
            .enable(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature());

    @Value("classpath:prompts/create-blog.st")
    private Resource createBlogPrompt;

    @Value("classpath:prompts/create-topic.st")
    private Resource createTopicPrompt;

    @Value("classpath:prompts/optimize-blog.st")
    private Resource optimizeBlogPrompt;

    @Value("classpath:prompts/fact-check-blog.st")
    private Resource factCheckBlogPrompt;

    @Override
    public String generateRandomTopic() {
        var publishedBlogs = publishedBlogRepository.findAllPublishedBlogTitles();
        return chatClient.prompt()
                .system(promptSystemSpec -> {
                    promptSystemSpec.text(createTopicPrompt);
                    promptSystemSpec.param("alreadyCoveredTopics", String.join(", ", publishedBlogs));
                })
                .user("generate random topic")
                .call()
                .content();
    }


    @Override
    public DevToModels.DevToPostRequestArticle generateBlog(String topic) {

        return chatClient.prompt()
                .system(createBlogPrompt)
                .user("topic is " + topic)
                .call()
                .entity(new BeanOutputConverter<>(DevToModels.DevToPostRequestArticle.class, objectMapper));
    }

    @Override
    public String optimizeBlogContent(String content) {
        return chatClient.prompt()
                .system(optimizeBlogPrompt)
                .user(content)
                .call()
                .content();
    }

    @Override
    public DevToModels.DevToFactCheckResponse factCheckBlogContent(String content) {
        return chatClient.prompt()
                .system(factCheckBlogPrompt)
                .user(content)
                .call()
                .entity(DevToModels.DevToFactCheckResponse.class);
    }
}
