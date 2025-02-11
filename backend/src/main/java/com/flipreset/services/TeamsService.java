package com.flipreset.services;

import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

public class TeamsService {
    private static MongoService mongoService;

    public TeamsService(MongoService mongoService) {
        TeamsService.mongoService = mongoService;
    }

    public static List<Document> getAllTeams() {
        try {
            return mongoService.getDatabase().getCollection("teams").find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching leagues: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<Document> getMatchesByTeam(String teamId) {
        try {
            return mongoService.getDatabase().getCollection("matches")
                    .find(new Document("teamId", teamId))
                    .into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<Document> getAllPlayersInTeam(String teamId) {
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
