package com.flipreset.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.flipreset.db.MongoDbServiceFR;
import com.flipreset.models.EventModel;
import com.flipreset.models.LeagueModel;
import com.flipreset.models.MatchesModel;
import com.flipreset.services.JsonProcessingService;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@Component
public class KafkaConsumerFR {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerFR.class);
    private static final MongoDbServiceFR mongoDbServiceFR = new MongoDbServiceFR("flipreset");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Async
    public void startConsumer() {
        log.info("Kafka Consumer starter...");

        // Kafka config
        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "flipreset-consumer_api-group";
        String[] topics = { "leagues-topic", "events-topic", "sets-topic" };

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topics));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String topic = record.topic();
                    String jsonValue = record.value();
                    String collectionName = getCollectionName(topic);

                    log.info(" Mottatt melding fra topic: {}", topic);

                    try {
                        // Behandle meldingen basert på topic
                        switch (topic) {
                            case "leagues-topic" -> processLeague(jsonValue, collectionName);
                            case "events-topic" -> processEvent(jsonValue, collectionName);
                            case "sets-topic" -> processSet(jsonValue, collectionName);
                            default -> log.warn("⚠️ Ukjent topic: {}", topic);
                        }
                    } catch (Exception e) {
                        log.error(" Feil ved behandling av melding fra {}: {}", topic, e.getMessage(), e);
                    }
                }
            }
        } finally {
            consumer.close();
            log.info(" Kafka Consumer stoppet.");
        }
    }

    private static void processLeague(String jsonValue, String collectionName) throws Exception {
        LeagueModel league = JsonProcessingService.processLeagueResponse(jsonValue);
        saveToMongoDB(collectionName, league.getId(), league);
    }

    private static void processEvent(String jsonValue, String collectionName) throws Exception {
        // Process single event
        EventModel event = JsonProcessingService.processSingleEvent(jsonValue);
        saveToMongoDB(collectionName, event.getId(), event);
        log.info("Lagret EventModel i MongoDB: {}", event.getId());
    }

    private static void processSet(String jsonValue, String collectionName) throws Exception {
        // Process single match
        MatchesModel match = JsonProcessingService.processSingleMatch(jsonValue);
        saveToMongoDB(collectionName, match.getId(), match);
        log.info(" Lagret MatchesModel i MongoDB: {}", match.getId());

        // Lagrer lag og spillere
        saveTeamsAndPlayers(match);
    }

    private static void saveTeamsAndPlayers(MatchesModel match) throws Exception {
        if (match.getTeam1() != null) {
            saveTeamToMongoDB(match.getTeam1());
        }
        if (match.getTeam2() != null) {
            saveTeamToMongoDB(match.getTeam2());
        }
    }

    private static void saveTeamToMongoDB(MatchesModel.Team team) throws Exception {
        // Lagre team
        if (!mongoDbServiceFR.documentExists("teams", team.getId())) {
            mongoDbServiceFR.insertMatchesDoc("teams", team.getId(), team);
            log.info(" Team lagret i MongoDB: {}", team.getName());
        }

        // Lagre spillere i laget
        for (MatchesModel.Player player : team.getMembers()) {
            savePlayerToMongoDB(player);
        }
    }

    private static void savePlayerToMongoDB(MatchesModel.Player player) throws Exception {
        if (!mongoDbServiceFR.documentExists("players", player.getId())) {
            mongoDbServiceFR.insertMatchesDoc("players", player.getId(), player);
            log.info(" Player lagret i MongoDB: {}", player.getPlayer().getGamerTag());
        }
    }

    private static void saveToMongoDB(String collectionName, String documentId, Object processedModel)
            throws Exception {
        if (!mongoDbServiceFR.documentExists(collectionName, documentId)) {
            String processedJson = objectMapper.writeValueAsString(processedModel);
            Document document = Document.parse(processedJson);
            document.put("_id", documentId); // Sett MongoDB sin _id
            mongoDbServiceFR.inseDocument(collectionName, document);
            log.info(" JSON-dokument lagret i MongoDB-samling: {}", collectionName);
        } else {
            log.info(" Dokument med ID {} finnes allerede i {}, hopper over.", documentId, collectionName);
        }
    }

    private static String getCollectionName(String topic) {
        return switch (topic) {
            case "leagues-topic" -> "leagues";
            case "events-topic" -> "events"; // Endret for riktig lagring av EventModel
            case "sets-topic" -> "matches"; // Endret for riktig lagring av MatchesModel
            default -> "unknown";
        };
    }
}
