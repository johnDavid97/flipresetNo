package com.flipreset.services;

import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.UpdateOptions;
//import org.bson.Document;
//import com.flipreset.models.*;
import org.springframework.stereotype.Service;

//import java.util.ArrayList;
//import java.util.List;

@Service
public class MongoService {

    private final MongoDatabase database;

    public MongoService(MongoClient mongoClient) {
        this.database = mongoClient.getDatabase("flipreset");
    }

    // Eksponer databasen for andre tjenester
    public MongoDatabase getDatabase() {
        return this.database;
    }

    /*
     * public void saveTeam(TeamModel team) {
     * MongoCollection<Document> collection = database.getCollection("teams");
     * Document teamDoc = new Document()
     * .append("_id", team.getId())
     * .append("name", team.getName())
     * .append("members", team.getMembers())
     * .append("images", team.getImages());
     * 
     * collection.updateOne(
     * new Document("_id", team.getId()),
     * new Document("$set", teamDoc),
     * new UpdateOptions().upsert(true)
     * );
     * }
     */
}