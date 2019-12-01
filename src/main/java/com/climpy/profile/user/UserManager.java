package com.climpy.profile.user;

import com.climpy.profile.ProfileAPI;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class UserManager {
    public Map<UUID, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public User getUser(String name) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        if (offlinePlayer == null) {
            return null;
        }

        return this.getPlayerUser(offlinePlayer.getUniqueId());
    }

    private User getPlayerUser(UUID uniqueUUID) {
        return this.getPlayerUser(uniqueUUID, "");
    }

    public User getUser(UUID uniqueUUID) {
        return this.getPlayerUser(uniqueUUID);
    }

    private User getPlayerUser(UUID uniqueUUID, String name) {
        User user = null;

        if (this.users.containsKey(uniqueUUID) && Bukkit.getPlayer(uniqueUUID) != null) {
            user = this.users.get(uniqueUUID);
        } else {
            Document documentUUID = ProfileAPI.find("user", Filters.eq("uniqueUUID", uniqueUUID.toString()));
            Document documentName;

            boolean create = false;
            if (documentUUID == null) {

                if (!name.isEmpty()) {
                    documentName = ProfileAPI.find("user", Filters.eq("name", name));
                    if (documentName != null) {
                        documentName.put("uniqueUUID", uniqueUUID);
                        documentUUID = documentName;

                        create = true;
                    }
                }
            } else {
                create = true;
            }
            if (create) {
                this.users.put(uniqueUUID, new User(documentUUID));
                user = this.users.get(uniqueUUID);
            }
        }
        return user;
    }
}
