package dev.kush.bloggenius.config.batch;

import dev.kush.bloggenius.models.DevToModels;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class PublishDevToStepConfig {

    @Bean
    public Step publishDevToStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("publishDevToTasklet") Tasklet publishDevToTasklet

    ) {
        log.info("PublishDevToStepConfig :: factCheckBlogStep :: Creating publish dev to step");
        return new StepBuilder("factCheckBlogStep", jobRepository)
                .tasklet(publishDevToTasklet, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    Tasklet publishDevToTasklet(@Value("${fact.checking.enabled}") boolean factCheckingOn, ApplicationEventPublisher applicationEventPublisher, RetryTemplate retryTemplate) {
        return (contribution, chunkContext) -> {
            var blog = (DevToModels.DevToPostRequestArticle) chunkContext.getStepContext()
                    .getStepExecution().getJobExecution().getExecutionContext().get("optimizedRequest");

            var factCheck = (DevToModels.DevToFactCheckResponse) chunkContext.getStepContext()
                    .getStepExecution().getJobExecution().getExecutionContext().get("factCheckResponse");

            // factCheck the blog content
            if (blog == null || StringUtils.isBlank(blog.bodyMarkdown())) {
                throw new IllegalStateException("optimizedRequest not found in job execution context");
            }

            return retryTemplate.execute(context -> {
                try {
                    if ((factCheck != null && factCheck.isFactuallyCorrect()) || !factCheckingOn) {
                        log.info("Publishing to Dev.to for topic: {}", blog.title());
                        applicationEventPublisher.publishEvent(blog);
                    } else {
                        log.error("Blog content is not factually correct. Not publishing to Dev.to");
                    }
                } catch (Exception e) {
                    log.error("Error publishing blog {}", e.getMessage());
                    throw e; // Re-throw to trigger retry
                }
                return RepeatStatus.FINISHED;
            });
        };
    }

}
