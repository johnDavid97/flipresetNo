package com.flipreset.services;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class LeaguesService {

    private final MongoService mongoService;

    @Autowired
    public LeaguesService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public List<Document> getAllLeagues() {
        try {
            return mongoService.getDatabase().getCollection("leagues").find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching leagues: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
