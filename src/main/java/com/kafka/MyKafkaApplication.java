package com.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hcw
 */
@SpringBootApplication(scanBasePackages = {"com.kafka"})
public class MyKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyKafkaApplication.class, args);
    }

}
