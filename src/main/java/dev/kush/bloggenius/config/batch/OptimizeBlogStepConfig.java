package dev.kush.bloggenius.config.batch;

import dev.kush.bloggenius.models.DevToModels;
import dev.kush.bloggenius.service.BlogGenerator;
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
@Slf4j
public class OptimizeBlogStepConfig {

    @Bean
    public Step optimizeBlogStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("optimizeBlogTasklet") Tasklet optimizeBlogTasklet

    ) {
        log.info("OptimizeBlogStepConfig :: optimizeBlogStep :: Creating optimize blog step");
        return new StepBuilder("optimizeBlogStep", jobRepository)
                .tasklet(optimizeBlogTasklet, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    Tasklet optimizeBlogTasklet(BlogGenerator blogGenerator, RetryTemplate retryTemplate) {
        return (contribution, chunkContext) -> {
            var blog = (DevToModels.DevToPostRequestArticle) chunkContext.getStepContext()
                    .getStepExecution().getJobExecution().getExecutionContext().get("devToRequest");

            // Optimize the blog content
            if (blog == null || StringUtils.isBlank(blog.bodyMarkdown())) {
                throw new IllegalStateException("DevToRequest not found in job execution context");
            }

            return retryTemplate.execute(context -> {
                try {
                    String optimizedContent = blogGenerator.optimizeBlogContent(blog.bodyMarkdown());
                    chunkContext.getStepContext().getStepExecution().getJobExecution()
                            .getExecutionContext().put("optimizedRequest", DevToModels.DevToPostRequestArticle.of(blog, optimizedContent));
                } catch (Exception e) {
                    log.error("Error optimizing blog {}", e.getMessage());
                    throw e; // Re-throw to trigger retry
                }
                return RepeatStatus.FINISHED;
            });
        };
    }
}
