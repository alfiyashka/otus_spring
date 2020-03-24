package ru.avalieva.otus.hw14SpringBatch.shell;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final Job importLibraryJob;

    private final JobLauncher jobLauncher;

    @SneakyThrows
    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "migrate")
    public void startMigrationJobWithJobLauncher() {
        JobExecution execution = jobLauncher.run(importLibraryJob, new JobParametersBuilder()
                .toJobParameters());
        System.out.println(execution);
    }


}

