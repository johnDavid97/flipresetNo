package com.flipreset.db;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDbConnectionFR {
    public static void main(String[] args) {
        String URI = "mongodb://localhost:27017";
        MongoClient clinet = MongoClients.create(URI);

        MongoDatabase database = clinet.getDatabase("flipreset");
        System.out.println("Tilkoblet til databasen: " + database.getName());
    }

}
