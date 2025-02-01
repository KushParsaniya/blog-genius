package dev.kush.bloggenius.scheduler;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@lombok.extern.slf4j.Slf4j
@Component
@RequiredArgsConstructor
@Slf4j
public class BlogScheduler {

    private final JobLauncher jobLauncher;
    private final Job generateBlogJob;

    @Scheduled(cron = "0 0 0 * * *")
    public void generateBlog() {
        log.info("BlogScheduler :: generateBlog :: Starting blog generation job");
        try {
                JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(generateBlogJob, jobParameters);
        } catch (Exception e) {
            log.error("BlogScheduler :: generateBlog :: Error running blog generation job", e);
        }
    }

}
