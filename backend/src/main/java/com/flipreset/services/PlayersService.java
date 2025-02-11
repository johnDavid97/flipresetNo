package com.flipreset.services;

import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

public class PlayersService {
    private static MongoService mongoService;

    public PlayersService(MongoService mongoService) {
        PlayersService.mongoService = mongoService;
    }

    public static List<Document> getAllPlayers() {
        try {
            return mongoService.getDatabase().getCollection("players").find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching leagues: " + e.getMessage());
            return new ArrayList<>();
        }

    }

    public static Document getTeamByPlayer(String playerId) {
        Document player = mongoService.getDatabase().getCollection("players").find(new Document("_id", playerId))
                .first();

        if (player != null) {
            String teamId = player.getString("teamId");
            return mongoService.getDatabase().getCollection("teams")
                    .find(new Document("_id", teamId))
                    .first();
        }

        return null;
    }
}
