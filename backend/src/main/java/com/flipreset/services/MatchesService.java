package com.flipreset.services;

import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

public class MatchesService {
    private static MongoService mongoService;

    public MatchesService(MongoService mongoService) {
        MatchesService.mongoService = mongoService;
    }

    public static List<Document> getAllMatches() {
        try {
            return mongoService.getDatabase().getCollection("matches").find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching leagues: " + e.getMessage());
            return new ArrayList<>();
        }

    }

    public static List<Document> getAllMatchesByEvent(String eventId) {
        try {
            Document match = mongoService.getDatabase().getCollection("events")
                    .find(new Document("_id", eventId))
                    .first();

            if (match != null && match.containsKey("series")) {
                return match.getList("series", Document.class);
            } else {
                System.err.println("Error" + eventId);
            }

        } catch (Exception e) {
            System.err.println("Error fetching " + eventId);
        }

        return new ArrayList<Document>();
    }

    public static Document getMatchById(String matchId) {
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
