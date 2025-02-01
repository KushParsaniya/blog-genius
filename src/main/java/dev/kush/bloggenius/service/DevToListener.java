package dev.kush.bloggenius.service;

import dev.kush.bloggenius.clients.DevToClient;
import dev.kush.bloggenius.config.DevToProperties;
import dev.kush.bloggenius.entities.PublishedBlog;
import dev.kush.bloggenius.models.DevToModels;
import dev.kush.bloggenius.repos.PublishedBlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DevToListener {

    private final DevToClient devToClient;
    private final DevToProperties devToProperties;
    private final PublishedBlogRepository publishedBlogRepository;

    @EventListener(DevToModels.DevToPostRequestArticle.class)
    public void publishToDevTo(DevToModels.DevToPostRequestArticle blog) {


        log.info("DevToListener :: publishToDevTo :: Publishing to Dev.to for topic: {}", blog.title());
        DevToModels.DevToPostResponse response;
        PublishedBlog publishedBlog;
        try {
            response = devToClient.publishToDevTo(new DevToModels.DevToPostRequest(blog), devToProperties.getApiKey());
            publishedBlog = PublishedBlog.of(response.id(), blog.title(), blog.description(), blog.bodyMarkdown(), "", blog.tags(), response.url(), true);
            log.info("DevToListener :: publishToDevTo :: Published to Dev.to for topic: {}", blog.title());
        } catch (Exception e) {
            publishedBlog = PublishedBlog.of(0, blog.title(), blog.description(), blog.bodyMarkdown(), "", blog.tags(), "", false);
            log.error("DevToListener :: publishToDevTo :: Error publishing to Dev.to {}", e.getMessage());
            return;
        }
        publishedBlogRepository.save(publishedBlog);
    }
}
