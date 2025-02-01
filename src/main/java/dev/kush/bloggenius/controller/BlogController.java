package dev.kush.bloggenius.controller;

import dev.kush.bloggenius.service.JobLauncherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlogController {

    private final JobLauncherService jobLauncherService;

    @GetMapping("/publish-blog")
    public String publishBlog() {
        jobLauncherService.startPublishBlogJob();
        return "Ok";
    }
}
