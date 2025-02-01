package dev.kush.bloggenius.config.batch;

import dev.kush.bloggenius.service.BlogGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class GenerateTopicStepConfig {
    private final BlogGenerator blogGenerator;

    @Bean
    public Step generateTopicStep(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager,
            @Qualifier("generateTopicTasklet") Tasklet generateTopicTasklet
    ) {
        log.info("generateTopicStepConfig :: generateTopicStep :: Creating generate blog step");
        return new StepBuilder("generateTopicStep", jobRepository)
                .tasklet(generateTopicTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    @StepScope
    Tasklet generateTopicTasklet(RetryTemplate retryTemplate) {
        return (contribution, chunkContext) -> {
            log.info("generateTopicTasklet :: generateTopicTasklet :: Generating topic");
            return retryTemplate.execute(context -> {
                try {
                    var topic = blogGenerator.generateRandomTopic();
                    log.info("Generated topic: {}", topic);
                    chunkContext.getStepContext().getStepExecution().getJobExecution()
                            .getExecutionContext().put("topic", topic
                            );
                } catch (Exception e) {
                    log.error("Error generating topic {}", e.getMessage());
                    throw e; // Re-throw to trigger retry
                }
                return RepeatStatus.FINISHED;
            });
        };
    }
}
