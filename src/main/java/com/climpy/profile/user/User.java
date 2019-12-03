package com.climpy.profile.user;

import com.climpy.profile.ProfileAPI;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.mongo.CollectionManager;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.symbol.SymbolType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

@Data @Getter @Setter
public class User {
    private final UUID uniqueUUID;
    private String name;

    private SymbolType symbolType = SymbolType.SYMBOL_0;
    private RankType rankType = RankType.DEFAULT;

    private long credit;

    private boolean staffMode;
    private boolean staffChat;
    private boolean vanishStatus;
    private boolean frozenStatus;
    private boolean onlineStatus;
    private boolean firstJoin;

    private String serverName;

    private String firstLoginTime;
    private String lastLoginTime;

    private String currentAddress;
    private List<String> ipAddresses = new ArrayList<>();

    public User(UUID uniqueUUID) {
        this.uniqueUUID = uniqueUUID;

        this.staffMode = false;
        this.staffChat = false;
        this.vanishStatus = false;
        this.frozenStatus = false;
        this.firstJoin = true;

        this.serverName = ProfilePlugin.getInstance().getConfig().getString("server-name");

        Player player = Bukkit.getPlayer(uniqueUUID);
        if (player != null) {
            this.setupBukkitPlayer(player);
        }
    }

    public User(Document document) {
        this.uniqueUUID = UUID.fromString(document.getString("uniqueUUID"));
        this.loadData(document);

        Player player = Bukkit.getPlayer(uniqueUUID);
        if (player != null) {
            this.setupBukkitPlayer(player);
        }
    }

    private void loadData(Document document) {
       this.name = document.getString("name");
       this.rankType = RankType.getRankOrDefault(document.getString("rankType"));
       this.symbolType = SymbolType.getSymbolOrDefault(document.getString("symbolType"));
       this.credit = document.getLong("credit");
       this.staffMode = document.getBoolean("staffMode");
       this.staffChat = document.getBoolean("staffChat");
       this.vanishStatus = document.getBoolean("vanishStatus");
       this.frozenStatus = document.getBoolean("frozenStatus");
       this.firstJoin = document.getBoolean("firstJoin");
       this.onlineStatus = document.getBoolean("onlineStatus");
       this.serverName = document.getString("serverName");

       this.firstLoginTime = document.getString("firstLoginTime");
       this.lastLoginTime = document.getString("lastLoginTime");

        this.currentAddress = document.getString("currentAddress");
        this.ipAddresses = new Gson().fromJson(document.getString("ipAddresses"), new TypeToken<List<String>>() {}.getType());
    }

    public void save() {
        Document document = new Document();
        document.put("name", this.name);
        document.put("uniqueUUID", this.uniqueUUID.toString());
        document.put("rankType", this.rankType.getRankName().toLowerCase(Locale.ENGLISH));
        document.put("symbolType", this.symbolType.getDisplayName().toLowerCase(Locale.ENGLISH));
        document.put("credit", this.credit);
        document.put("staffMode", this.staffMode);
        document.put("staffChat", this.staffChat);
        document.put("vanishStatus", this.vanishStatus);
        document.put("frozenStatus", this.frozenStatus);
        document.put("firstJoin", this.firstJoin);
        document.put("onlineStatus", this.onlineStatus);
        document.put("serverName", this.serverName);

        document.put("firstLoginTime", this.firstLoginTime);
        document.put("lastLoginTime", this.lastLoginTime);

        document.put("currentAddress", this.currentAddress);
        document.put("ipAddresses", new Gson().toJson(this.ipAddresses, new TypeToken<List<String>>() {}.getType()));

        ProfileAPI.replaceOne("user", Filters.eq("uniqueUUID", this.uniqueUUID.toString()), document, true);
        ProfileAPI.replaceOne("user", Filters.eq("onlineStatus", this.onlineStatus), document, true);
        ProfileAPI.replaceOne("user", Filters.eq("serverName", this.serverName), document, true);
        ProfileAPI.replaceOne("user", Filters.eq("firstLoginTime", this.firstLoginTime), document, true);
        ProfileAPI.replaceOne("user", Filters.eq("lastLoginTime", this.lastLoginTime), document, true);
    }

    public void setupBukkitPlayer(Player player) {
        if (player == null) {
            return;
        }

        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            if (permissionAttachmentInfo.getAttachment() == null || permissionAttachmentInfo.getAttachment().getPlugin() == null ||
            !permissionAttachmentInfo.getAttachment().getPlugin().equals(ProfilePlugin.getInstance())) {
                continue;
            }

            permissionAttachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> {
                permissionAttachmentInfo.getAttachment().unsetPermission(permission);
            });
        }

        PermissionAttachment permissionAttachment = player.addAttachment(ProfilePlugin.getInstance());
        for (String permission : this.rankType.getPermissions()) {
            permissionAttachment.setPermission(permission, true);
        }

        player.recalculatePermissions();
    }
}
