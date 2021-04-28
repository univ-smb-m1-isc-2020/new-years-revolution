package net.oups.new_years_revolution;

import java.util.Arrays;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private static Date startDate;
    public static Date getStartDate() {
        return startDate;
    }

    public static void main(String[] args) {
        startDate = new Date(System.currentTimeMillis());
        SpringApplication.run(Application.class, args);
    }



    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {};
    }
}