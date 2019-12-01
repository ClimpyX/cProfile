package com.climpy.profile.mongo;

import com.climpy.profile.ProfilePlugin;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.logging.Level;

@Getter
public class MongoConnection {
    private MongoCredential credential;
    private MongoClient mongoClient;
    private CollectionManager collectionManager;

    public void setupDatabaseConnection() {
        try {
            this.credential = MongoCredential.createCredential(ProfilePlugin.getInstance().getConfig().getString("mongoDB.user"), ProfilePlugin.getInstance().getConfig().getString("mongoDB.database"), ProfilePlugin.getInstance().getConfig().getString("mongoDB.password").toCharArray());
            this.mongoClient = new MongoClient(new ServerAddress(ProfilePlugin.getInstance().getConfig().getString("mongoDB.host"), ProfilePlugin.getInstance().getConfig().getInt("mongoDB.port")), Collections.singletonList(this.credential));

            this.collectionManager = new CollectionManager(this.mongoClient);
            this.collectionManager.createDataConnection();
            ProfilePlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] Veritabanı sunucusuna başarıyla bağlanıldı.");
        } catch (MongoClientException ex) {
            ProfilePlugin.getInstance().getLogger().log(Level.SEVERE, "[MongoDB] Çalışan, yerel Mongo Veritabanı sunucusuna bağlanılamıyor!");
            Bukkit.getServer().shutdown();
        }
    }

    public void disableDatabaseConnection() {
        if (this.mongoClient != null) {
            ProfilePlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] Veritabanı bağlantısı kapatılıyor..");

            try {
                this.mongoClient.close();
                ProfilePlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] Veritabanı bağlantısı başarıyla kapatıldı.");
            } catch (Exception ex) {
                ProfilePlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] Veritabanı verileri kaydedilemedi.");
            }
        }
    }
}