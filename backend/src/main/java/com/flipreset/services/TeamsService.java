package com.flipreset.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

@Component
public class TeamsService {
    private final MongoService mongoService;

    @Autowired
    public TeamsService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public List<Document> getAllTeams() {
        try {
            return mongoService.getDatabase().getCollection("teams").find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching leagues: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Document> getMatchesByTeam(String teamId) {
        try {
            return mongoService.getDatabase().getCollection("matches")
                    .find(new Document("teamId", teamId))
                    .into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Document> getAllPlayersInTeam(String teamId) {
        try {
            Document team = mongoService.getDatabase()
                    .getCollection("teams")
                    .find(new Document("_id", teamId))
                    .first();

            if (team != null && team.containsKey("members")) {
                return team.getList("members", Document.class);
            } else {
                System.err.println("Team with id " + teamId + " not found or has no members.");
            }
        } catch (Exception e) {
            System.err.println("Error fetching players for teamId " + teamId + ": " + e.getMessage());
        }

        return new ArrayList<>();
    }
}
