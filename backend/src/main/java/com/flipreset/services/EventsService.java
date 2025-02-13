package com.flipreset.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

@Component
public class EventsService {
    private final MongoService mongoService;

    @Autowired
    public EventsService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public List<Document> getAllEvents() {
        try {
            return mongoService.getDatabase().getCollection("events").find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching events: " + e.getMessage());
            return new ArrayList<>();
        }

    }
}
