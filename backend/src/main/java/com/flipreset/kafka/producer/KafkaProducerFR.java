package com.flipreset.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerFR {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerFR.class);
    private static final String BOOTSTRAP_SERVERS = "127.0.0.1:9092";

    private static final Properties properties = new Properties();
    private static final KafkaProducer<String, String> producer;

    static {
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(properties);
    }

    public static void sendMessage(String topic, String key, String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, msg);

        try {
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    log.info("Melding sendt til Kafka -> Topic: {}, Partition: {}, Offset: {}",
                            metadata.topic(), metadata.partition(), metadata.offset());
                } else {
                    log.error("Feil ved sending av Kafka-melding: {}", exception.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Exception i KafkaProducer: ", e);
        }
    }

    public static void closeProducer() {
        producer.close();
        log.info("Kafka Producer lukket.");
    }
}
