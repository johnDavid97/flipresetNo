package com.flipreset.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbServiceFR {
    private static MongoClient client = MongoClients.create("mongodb://localhost:27017");
    private static MongoDatabase database = client.getDatabase("flipreset");

    public MongoDbServiceFR(String dbName) {
        MongoDbServiceFR.database = client.getDatabase(dbName);
    }

    /**
     * Sjekker om dokument med gitt ID allerede finnes.
     */
    public boolean documentExists(String collectionName, String id) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Bson filter = eq("id", id);
        return collection.find(filter).first() != null;
    }

    /**
     * Setter inn et dokument i riktig collection hvis det ikke allerede finnes.
     */
    public boolean inseDocument(String collectionName, Document document) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        try {
            // Setter inn dokumentet uten å sjekke for "id"-nøkkel
            collection.insertOne(document);
            System.out.println("Dokument ble satt inn: " + document.toJson());
            return true;
        } catch (MongoWriteException e) {
            System.err.println("Feil ved innsetting av dokument: " + e.getMessage());
            return false;
        }
    }

    public void insertMatchesDoc(String collectionName, String documentId, Object data) throws Exception {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        ObjectMapper mapper = new ObjectMapper();
        String jsonData = mapper.writeValueAsString(data);
        Document document = Document.parse(jsonData);
        document.put("_id", documentId); // Legg til MongoDB "_id"-felt
        collection.insertOne(document);
    }

    public Boolean findDocument(String key, String value, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        if (collection.find(Filters.eq("id", key)).first() != null) {

            System.out.println("Dokument fantes:" + collection.find(Filters.eq("id", key)).first().toJson());
            return true;
        }
        return false;
    }
}
