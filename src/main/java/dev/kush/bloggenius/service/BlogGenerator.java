package dev.kush.bloggenius.service;

import dev.kush.bloggenius.models.DevToModels;

public interface BlogGenerator {

    String generateRandomTopic();

    DevToModels.DevToPostRequestArticle generateBlog(String topic);

    String optimizeBlogContent(String content);

    DevToModels.DevToFactCheckResponse factCheckBlogContent(String content);
}
