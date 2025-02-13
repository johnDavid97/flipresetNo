package com.flipreset.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

@Component
public class MatchesService {
    private final MongoService mongoService;

    @Autowired
    public MatchesService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public List<Document> getAllMatches() {
        try {
            return mongoService.getDatabase().getCollection("matches").find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching leagues: " + e.getMessage());
            return new ArrayList<>();
        }

    }

    public List<Document> getAllMatchesByEvent(String eventId) {
        try {
            // Hent eventet basert på eventId
            Document event = mongoService.getDatabase().getCollection("events")
                    .find(new Document("_id", eventId))
                    .first();

            if (event != null && event.containsKey("series")) {
                // Hent liste med match-IDs fra "series"-feltet
                List<Document> seriesList = event.getList("series", Document.class);

                // Ekstraher IDene til matchene
                List<String> matchIds = new ArrayList<>();
                for (Document series : seriesList) {
                    if (series.containsKey("id")) {
                        matchIds.add(series.getString("id"));
                    }
                }

                // Hent matchene fra "matches"-samlingen basert på IDene
                return mongoService.getDatabase().getCollection("matches")
                        .find(new Document("_id", new Document("$in", matchIds)))
                        .into(new ArrayList<>());
            } else {
                System.err.println("No series found for event with id: " + eventId);
            }

        } catch (Exception e) {
            System.err.println("Error fetching matches for event " + eventId + ": " + e.getMessage());
        }

        return new ArrayList<>();
    }

    public Document getMatchById(String matchId) {
        try {
            return mongoService.getDatabase()
                    .getCollection("matches")
                    .find(new Document("_id", matchId))
                    .first();
        } catch (Exception e) {
            System.err.println("Error fetching match with id " + matchId + ": " + e.getMessage());
            return null;
        }
    }

}
