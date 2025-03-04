package com.example.interview.batch;

import com.example.interview.entity.MyTransactions;
import com.example.interview.repository.MyTransactionsRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@Slf4j
public class ReadDataSourceBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MyTransactionsRepository transactionRepository;
    private final int CHUNK_SIZE = 25;


    public ReadDataSourceBatch(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               MyTransactionsRepository transactionRepository) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.transactionRepository = transactionRepository;
    }

    @Bean
    public FlatFileItemReader<MyTransactions> reader() {
        return new FlatFileItemReaderBuilder<MyTransactions>()
                .name("transactionItemReader")
                .resource(new ClassPathResource("datasource.txt"))
                .lineMapper(new CustomTransactrionBatchLineMapper())
                .linesToSkip(1)
                .build();
    }

    @Bean
    public ItemProcessor<MyTransactions, MyTransactions> processor() {
        return transaction -> {
            if (StringUtils.isBlank(transaction.getAccountNumber())) {
                return null;
            }
            log.info("Processing {}", transaction.getAccountNumber());
            return transaction;
        };
    }

    @Bean
    public ItemWriter<MyTransactions> writer() {
        return transactionRepository::saveAll;
    }

    @Bean
    public Step step() {
        return new StepBuilder("step1", jobRepository)
                .<MyTransactions, MyTransactions>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job() {
        return new JobBuilder("importTransactionsJob", jobRepository)
                .start(step())
                .build();
    }
}


