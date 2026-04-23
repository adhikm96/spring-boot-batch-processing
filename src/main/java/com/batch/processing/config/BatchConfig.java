package com.batch.processing.config;

import com.batch.processing.entity.User;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public FlatFileItemReader<User> reader() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("name", "salary");

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);


        FlatFileItemReader<User> reader = new FlatFileItemReader<User>(new ClassPathResource("data.csv"), lineMapper);
        reader.setLinesToSkip(1);
        return reader;
    }

    @Bean
    public ItemProcessor<User, User> processor() {
        return user -> {
            user.setName(user.getName().toUpperCase());
            return user;
        };
    }

    @Bean
    public ItemWriter<User> writer() {
        return users -> users.forEach(user ->
                System.out.println(user.getName() + " " + user.getSalary())
        );
    }

    @Bean
    public Step step(JobRepository jobRepository) {
        return new StepBuilder("myStep", jobRepository)
                .<User, User>chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("myJob", jobRepository).start(step).build();
    }


}
