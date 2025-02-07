package com.flipreset.db;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDbServiceFR {
    private final MongoCollection<Document> collection;
    private static final MongoDatabase database = MongoDbConnectionFR.getDatabase();

    public MongoDbServiceFR(String collectionName) {
        this.collection = database.getCollection(collectionName);
    }

    public void insertDocument(Document document) {
        collection.insertOne(document);
        System.out.println("Dokument lagret i MongoDB: " + document.toJson());
    }

    public Boolean findDocument(String key, String value) {
        if (collection.find(Filters.eq("key", key)).first() != null) {

            System.out.println("Dokument fantes:" + collection.find(Filters.eq("key", key)).first().toJson());
            return true;
        }
        return false;
    }
}
