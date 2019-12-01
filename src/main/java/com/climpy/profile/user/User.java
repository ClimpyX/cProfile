package com.climpy.profile.user;

import com.climpy.profile.ProfileAPI;
import com.climpy.profile.mongo.CollectionManager;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.symbol.SymbolType;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

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

    public User(UUID uniqueUUID) {
        this.uniqueUUID = uniqueUUID;

        this.staffMode = false;
        this.staffChat = false;
        this.vanishStatus = false;
        this.frozenStatus = false;
        this.firstJoin = true;
    }

    public User(Document document) {
        this.uniqueUUID = UUID.fromString(document.getString("uniqueUUID"));
        this.loadData(document);
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

        ProfileAPI.replaceOne("user", Filters.eq("uniqueUUID", this.uniqueUUID.toString()), document, true);
        ProfileAPI.replaceOne("user", Filters.eq("onlineStatus", this.onlineStatus), document, true);
    }
}
