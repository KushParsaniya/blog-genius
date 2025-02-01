package dev.kush.bloggenius;

import dev.kush.bloggenius.models.DevToModels;
import dev.kush.bloggenius.service.BlogGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

//@Import(TestcontainersConfiguration.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogGeniusApplicationTests {

    @Autowired
    private BlogGenerator blogGenerator;

    private Resource createBlogPrompt;

    @BeforeEach
    void setUp() {
        createBlogPrompt = new ClassPathResource("prompts/create-blog.st");
    }

    @Test
    void generateBlog_shouldReturnDevToPostRequest() {
        DevToModels.DevToPostRequestArticle result = blogGenerator.generateBlog("Count down latch");
        assertThat(result).isNotNull();
    }

}
