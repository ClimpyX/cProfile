package com.climpy.profile;


import com.climpy.profile.mongo.CollectionManager;
import com.climpy.profile.mongo.MongoConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ProfileAPI {

    public static FindIterable findAll(String collectionName) {
        MongoConnection mongoConnection = ProfilePlugin.getInstance().getMongoConnection();
        CollectionManager collectionManager = mongoConnection.getCollectionManager();

        MongoCollection mongoCollection = collectionManager.getMongoCollection(collectionName);
        return mongoCollection.find();
    }

    public static Document find(String collectionName, Bson bson) {
        MongoConnection mongoConnection = ProfilePlugin.getInstance().getMongoConnection();
        CollectionManager collectionManager = mongoConnection.getCollectionManager();

        MongoCollection mongoCollection = collectionManager.getMongoCollection(collectionName);
        return (Document) mongoCollection.find(bson).first();
    }

    public static void replaceOne(String collectionName, Bson bson, Document document, boolean upsert) {
        MongoConnection mongoConnection = ProfilePlugin.getInstance().getMongoConnection();
        CollectionManager collectionManager = mongoConnection.getCollectionManager();

        MongoCollection mongoCollection = collectionManager.getMongoCollection(collectionName);
        mongoCollection.replaceOne(bson, document, new UpdateOptions().upsert(upsert));
    }

    public static void removeBson(String collectionName, Bson bson, boolean manyDelete) {
        MongoConnection mongoConnection = ProfilePlugin.getInstance().getMongoConnection();
        CollectionManager collectionManager = mongoConnection.getCollectionManager();

        MongoCollection mongoCollection = collectionManager.getMongoCollection(collectionName);
        if (manyDelete) {
            mongoCollection.deleteMany(bson);
        } else {
            mongoCollection.deleteOne(bson);
        }
    }
    
}
