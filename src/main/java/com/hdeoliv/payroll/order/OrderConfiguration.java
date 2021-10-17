package com.hdeoliv.payroll.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OrderConfiguration {
    private static final Logger log = LoggerFactory.getLogger(OrderConfiguration.class);

    @Bean
    CommandLineRunner initOrderDatabase(OrderRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Order("Gibson Les Paul", OrderStatus.COMPLETED)));
            log.info("Preloading " + repository.save(new Order("Rickenbacker", OrderStatus.IN_PROGRESS)));
            log.info("Preloading " + repository.save(new Order("Fender Telecaster", OrderStatus.CANCELLED)));
        };
    }
}
