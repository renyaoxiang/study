package com.rionour.study.spring;

import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing
public class BatchConfig implements CommandLineRunner {
    @Autowired
    private
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job downloadFile() {
        return jobBuilderFactory.get("downloadFile").incrementer(new RunIdIncrementer()).start(downloadFileStep()).build();
    }

    @Bean
    public Step downloadFileStep() {
        return this.stepBuilderFactory.get("sampleStep").tasklet(tasklet()).build();
    }

    @Bean
    @StepScope
    public MyTasklet tasklet() {
        return new MyTasklet();
    }

    public static class MyTasklet implements Tasklet, StepExecutionListener {
        private String info;

        @Override
        public void beforeStep(StepExecution stepExecution) {
            JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
            info = parameters.getString("id");
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            return stepExecution.getExitStatus();
        }


        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            System.out.println("  process" + info);
            return RepeatStatus.FINISHED;
        }

    }


    @Autowired
    JobLauncher jobLauncher;

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(downloadFile(), new JobParametersBuilder().addString("id", "1").toJobParameters());
        jobLauncher.run(downloadFile(), new JobParametersBuilder().addString("id", "2").toJobParameters());
        jobLauncher.run(downloadFile(), new JobParametersBuilder().addString("id", "3").toJobParameters());
    }
}
