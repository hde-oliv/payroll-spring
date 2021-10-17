package com.hdeoliv.payroll.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EmployeeConfiguration {
    private static final Logger log = LoggerFactory.getLogger(EmployeeConfiguration.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Employee("Michael", "Gira", "singer")));
            log.info("Preloading " + repository.save(new Employee("Ted", "Parsons", "drummer")));
        };
    }
}
