package com.flipreset.kafka.consumer;

import org.bson.Document;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flipreset.db.MongoDbServiceFR;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerFR {
    public static final Logger log = LoggerFactory.getLogger(KafkaConsumerFR.class);

    public static void main(String[] args) {
        MongoDbServiceFR mongoDbServiceFR = new MongoDbServiceFR("Kafka_test");

        log.info("I am a Kafka Consumer");

        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "my-fourth-application";
        String topic = "frist_topic";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Arrays.asList(topic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {

                    if (mongoDbServiceFR.findDocument(record.key(), record.value())) {
                        log.info("Key: " + record.key() + ", Value: " + record.value() + "Finnes allerede");
                    } else {
                        // document does not exist, insert a new one
                        Document document = new Document("key", record.key()).append("value", record.value());
                        mongoDbServiceFR.insertDocument(document);

                        log.info("Key: " + record.key() + ", Value: " + record.value() + " Lagret i MongoDB");
                        log.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                    }

                }
            }
        } finally {
            consumer.close();
        }
    }
}
