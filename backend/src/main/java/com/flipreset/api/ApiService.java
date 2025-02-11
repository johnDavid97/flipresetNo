package com.flipreset.api;

import java.net.http.HttpClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipreset.models.*;
import com.flipreset.services.JsonProcessingService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import com.flipreset.kafka.producer.KafkaProducerFR;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final Logger log = LoggerFactory.getLogger(ApiService.class);

    public void startApi() {
        try {
            InputStream inputStream = ApiService.class.getClassLoader().getResourceAsStream("api_config.json");
            if (inputStream == null) {
                throw new RuntimeException("Kunne ikke finne api_config.json i resources-mappen!");
            }

            ApiModel apiModel = mapper.readValue(inputStream, ApiModel.class);

            // Send hver query
            sendAllQueries(apiModel);

        } catch (IOException | InterruptedException | URISyntaxException e) {
            System.err.println("Error with ApiService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendAllQueries(ApiModel apiModel) throws IOException, URISyntaxException, InterruptedException {
        QueryModel[] queries = {
                query.GET_LEAGUES,
                query.GET_EVENTS,
                query.GET_SETS
        };

        for (QueryModel selectedQuery : queries) {
            System.out.println("Sending query: " + selectedQuery.getName());

            if (apiModel.body == null) {
                apiModel.body = new ApiModel.Body();
            }

            apiModel.body.query = selectedQuery.getQuery();

            URI uri = new URI(apiModel.url);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(apiModel.body)));

            for (Map.Entry<String, String> entry : apiModel.headers.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(
                    "Respons for " + selectedQuery.getName() + " (Status: " + response.statusCode() + ")");
            processResponse(selectedQuery, response.body());
        }
    }

    private static void processResponse(QueryModel query, String jsonResponse) {
        try {
            switch (query.getName()) {
                case "getLeagues":
                    KafkaProducerFR.sendMessage("leagues-topic", "leagues", jsonResponse);
                    LeagueModel league = JsonProcessingService.processLeagueResponse(jsonResponse);
                    log.info("✅ LeagueModel sendt: ID = {}", league.getId());
                    break;

                case "getEvents":
                    log.info("Event respons:" + jsonResponse);
                    JsonNode eventNodes = mapper.readTree(jsonResponse).path("data").path("league").path("events")
                            .path("nodes");
                    log.info("Etter å mappe eventNodes: " + eventNodes.toString());
                    for (JsonNode eventNode : eventNodes) {
                        String eventJson = mapper.writeValueAsString(eventNode);
                        KafkaProducerFR.sendMessage("events-topic", "events", eventJson);
                        log.info("✅ Event sendt til Kafka: {}", eventJson);
                    }
                    break;

                case "getSets":
                    log.info("Sets respons:" + jsonResponse);

                    JsonNode matchesNodes = mapper.readTree(jsonResponse).path("data").path("league").path("events")
                            .path("nodes");
                    log.info("etter å mappe matchesNodes: " + matchesNodes.toString());
                    for (JsonNode matchNode : matchesNodes) {
                        String matchJson = mapper.writeValueAsString(matchNode);
                        KafkaProducerFR.sendMessage("sets-topic", "sets", matchJson);
                        log.info("✅ Match sendt til Kafka: {}", matchNode.path("id").asText());
                    }
                    break;

                default:
                    log.warn("⚠️ Ukjent query: {}", query.getName());
                    break;
            }
        } catch (Exception e) {
            log.error("❌ Feil ved prosessering av respons for {}: {}", query.getName(), e.getMessage(), e);
        }
    }
}
