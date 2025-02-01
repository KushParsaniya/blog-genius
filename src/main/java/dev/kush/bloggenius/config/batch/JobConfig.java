package dev.kush.bloggenius.config.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class JobConfig {

    @Bean
    public Job generateBlogJob(
            JobRepository jobRepository,
            @Qualifier("generateTopicStep") Step generateTopicStep,
            @Qualifier("generateBlogStep") Step generateBlogStep,
            @Qualifier("optimizeBlogStep") Step optimizeBlogStep,
            @Qualifier("factCheckBlogStep") Step factCheckBlogStep,
            @Qualifier("publishDevToStep") Step publishDevToStep

    ) {
        return new JobBuilder("generateBlogJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(generateTopicStep)
                .next(generateBlogStep)
                .next(optimizeBlogStep)
//                .next(factCheckBlogStep)
                .next(publishDevToStep)
                .build();
    }

}