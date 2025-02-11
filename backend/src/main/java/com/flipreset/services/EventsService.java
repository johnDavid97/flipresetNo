package com.flipreset.services;

import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

public class EventsService {
    private static MongoService mongoService;

    public EventsService(MongoService mongoService) {
        EventsService.mongoService = mongoService;
    }

    public static List<Document> getAllEvents() {
        try {
            return mongoService.getDatabase().getCollection("events").find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error fetching events: " + e.getMessage());
            return new ArrayList<>();
        }

    }
}
