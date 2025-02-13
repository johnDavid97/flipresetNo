package com.flipreset;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import com.flipreset.api.ApiService;
import com.flipreset.kafka.consumer.KafkaConsumerFR;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private ApiService apiService;

    @Autowired
    private KafkaConsumerFR kafkaConsumerFR;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Start ApiService parallelt
        new Thread(() -> apiService.startApi()).start();

        // Start KafkaConsumer parallelt
        new Thread(() -> kafkaConsumerFR.startConsumer()).start();
    }
}
