package com.flipreset.db;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDbConnectionFR {
    private static String URI = "mongodb://localhost:27017";
    private static MongoClient client = MongoClients.create(URI);
    private static MongoDatabase database = client.getDatabase("flipreset");

    public static MongoDatabase getDatabase() {
        System.out.println("Tilkoblet til databasen: " + database.getName());
        return database;
    }
}
