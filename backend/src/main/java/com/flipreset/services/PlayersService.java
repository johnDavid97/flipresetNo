package com.flipreset.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

@Service
public class PlayersService {
    private final MongoService mongoService;

    @Autowired
    public PlayersService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public List<Document> getAllPlayers() {
        try {
            return mongoService.getDatabase().getCollection("players").find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching leagues: " + e.getMessage());
            return new ArrayList<>();
        }

    }

    public Document getTeamByPlayer(String playerId) {
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
