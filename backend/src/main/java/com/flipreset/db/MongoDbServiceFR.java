package com.flipreset.db;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDbServiceFR {

    private MongoDatabase database;

    public MongoDbServiceFR(MongoDatabase database) {
        this.database = database;

    }

    public void insertDocument(Document document) {
        MongoCollection<Document> collection = database.getCollection("flipreset");
        collection.insertOne(document);
    }

}
