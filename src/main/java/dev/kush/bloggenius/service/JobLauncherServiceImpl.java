package dev.kush.bloggenius.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobLauncherServiceImpl implements JobLauncherService {

    private final JobLauncher jobLauncher;
    private final Job generateBlogJob;

    @Override
    public void startPublishBlogJob() {
        log.info("JobLauncherServiceImpl :: startPublishBlogJob :: start");
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
