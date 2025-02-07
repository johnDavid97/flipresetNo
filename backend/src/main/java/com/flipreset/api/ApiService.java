package com.flipreset.api;

import java.net.http.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class ApiService {
    private static HttpClient client = HttpClient.newHttpClient();
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            // This was made by following the tuorial of Gayanga Kuruppu on Medium
            ApiModel apiModel = mapper.readValue(new File("src/main/resources/api_config.json"), ApiModel.class);

            URI uri = new URI(apiModel.url);
            URL newURL = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();
            conn.setRequestMethod(apiModel.method);
            conn.setRequestProperty("Content-Type", "application/json");

            for (Map.Entry<String, String> header : apiModel.headers.entrySet()) {
                conn.setRequestProperty(header.getKey(), header.getValue());
            }

            if (apiModel.method.equalsIgnoreCase("POST")) {
                conn.setDoOutput(true);
                String requestBody = mapper.writeValueAsString(apiModel.body);
                conn.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                InputStream responseStream = conn.getInputStream();
                Scanner scanner = new Scanner(responseStream, StandardCharsets.UTF_8);
                String responseBody = scanner.useDelimiter("\\A").next();
                scanner.close();

                System.out.println("Response fra API: " + responseBody);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
