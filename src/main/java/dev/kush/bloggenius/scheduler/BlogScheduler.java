package dev.kush.bloggenius.scheduler;

import dev.kush.bloggenius.service.JobLauncherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlogScheduler {

    private final JobLauncherService jobLauncherService;

    @Scheduled(cron = "0 0 0 * * *")
    public void generateBlog() {
        log.info("BlogScheduler :: generateBlog :: called");

        jobLauncherService.startPublishBlogJob();

        log.info("BlogScheduler :: generateBlog :: ended");
    }

}
