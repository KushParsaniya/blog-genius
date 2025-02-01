package dev.kush.bloggenius;

import org.springframework.boot.SpringApplication;

public class TestBlogGeniusApplication {

    public static void main(String[] args) {
        SpringApplication.from(BlogGeniusApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
