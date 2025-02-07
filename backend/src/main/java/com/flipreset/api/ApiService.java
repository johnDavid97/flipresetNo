package com.flipreset.api;

import java.net.http.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ApiService {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {

            InputStream inputStream = ApiService.class.getClassLoader().getResourceAsStream("api_config.json");
            if (inputStream == null) {
                throw new RuntimeException("Kunne ikke finne api_config.json i resources-mappen!");
            }

            ApiModel apiModel = mapper.readValue(inputStream, ApiModel.class);

            URI uri = new URI(apiModel.url);
            HttpClient httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(apiModel.body)));

            for (Map.Entry<String, String> entry : apiModel.headers.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }

            HttpRequest request = requestBuilder.build();

            System.out.println("Request: " + request);
            System.out.println("Headers: " + request.headers().allValues("content-type"));
            System.out.println("Body: " + mapper.writeValueAsString(apiModel.body));

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status code: " + response.statusCode());
            System.out.println("Headers: " + response.headers().allValues("content-type"));
            System.out.println("Body: " + response.body());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
