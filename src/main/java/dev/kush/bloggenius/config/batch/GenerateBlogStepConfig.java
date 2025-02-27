package dev.kush.bloggenius.config.batch;

import dev.kush.bloggenius.models.DevToModels;
import dev.kush.bloggenius.service.BlogGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GenerateBlogStepConfig {

    private final BlogGenerator blogGenerator;

    @Bean
    public Step generateBlogStep(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager,
            @Qualifier("generateBlogTasklet") Tasklet generateBlogTasklet
    ) {
        log.info("GenerateBlogStepConfig :: generateBlogStep :: Creating generate blog step");
        return new StepBuilder("generateBlogStep", jobRepository)
                .tasklet(generateBlogTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    @StepScope
    Tasklet generateBlogTasklet(RetryTemplate retryTemplate) {
        return (contribution, chunkContext) -> {

            var topic = (String) chunkContext.getStepContext().getStepExecution().getJobExecution()
                    .getExecutionContext().get("topic");

            if (StringUtils.isBlank(topic)) {
                log.error("generateBlogTasklet :: generateBlogTasklet :: No topic found in job execution context");
                return RepeatStatus.FINISHED;
            }
            return retryTemplate.execute(context -> {
                try {
                    DevToModels.DevToPostRequestArticle blog = blogGenerator.generateBlog(topic);
                    chunkContext.getStepContext().getStepExecution().getJobExecution()
                            .getExecutionContext().put("devToRequest", blog);
                } catch (Exception e) {
                    log.error("Error generating blog {}", e.getMessage());
                    throw e; // Re-throw to trigger retry
                }
                return RepeatStatus.FINISHED;
            });
        };
    }
}
