package com.climpy.profile.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.Locale;

@Getter @AllArgsConstructor
public enum RankType {
    OWNER("§4Owner §c", "", "owner", "§4Owner", ChatColor.RED),
    CO_OWNER("§4Co-Owner §c", "", "coowner", "§4Co-Owner", ChatColor.RED),
    ADMIN("§4Admin §c", "", "admin", "§cAdmin", ChatColor.RED),
    DEVELOPER("§bDeveloper ", "", "developer", "§bDeveloper", ChatColor.AQUA),
    BUILDTEAMPLUS("§3Mimar§c+ §b", "", "buildteam+", "§3Build Team§c+", ChatColor.AQUA),
    BUILDTEAM("§3Mimar §b", "", "buildteam", "§3Build Team", ChatColor.BLUE),
    MODPLUS("§2Mod§c+ §2", "", "mod+", "§2Mod§c+", ChatColor.DARK_GREEN),
    MOD("§2Mod ", "", "mod", "§2Mod", ChatColor.DARK_GREEN),
    CHAT_MOD("§bChatMod §9", "", "chat_mod", "§bChatMod", ChatColor.AQUA),
    HELPER("§9Yardımcı §b", "", "helper", "§9Helper", ChatColor.AQUA),
    TRAILERHELPER("§9Trailer Helper §b", "", "trailerhelper", "§9T. Helper", ChatColor.BLUE),
    YOUTUBER("§6Youtuber ", "", "youtuber", "§6Youtuber", ChatColor.GOLD),
    CVIPPLUSPLUS("§eCVIP§c++ ", "", "cvip++", "§eCVIP§c++", ChatColor.YELLOW),
    CVIPPLUS("§eCVIP§c+ ", "", "cvip+", "§eCVIP§c+", ChatColor.YELLOW),
    CVIP("§eCVIP ", "", "cvip", "§eCVIP", ChatColor.YELLOW),
    MVIPPLUS("§dMVIP§e+§d ", "", "mvip+", "§dMVIP§e+", ChatColor.LIGHT_PURPLE),
    MVIP("§dMVIP ", "", "mvip", "§dMVIP", ChatColor.LIGHT_PURPLE),
    VIPPLUS("§bVIP§6+§b ", "", "vip+", "§bVIP§6+", ChatColor.AQUA),
    VIP("§bVIP ", "", "vip", "§bVIP", ChatColor.AQUA),
    DEFAULT("§9", "", "default", "§7Normal", ChatColor.BLUE);

    private String prefix, suffix, rankName, displayName;
    private ChatColor color;

    public boolean isAboveOrEqual(RankType rank) {
        return (ordinal() <= rank.ordinal());
    }

    public static RankType getRankOrDefault(String internalName) {
        RankType rankType;

        try {
            rankType = RankType.valueOf(internalName.toUpperCase(Locale.ENGLISH));
        } catch (Exception ex) {
            rankType = RankType.DEFAULT;
        }

        return rankType;
    }

    public static String separatorCheck(StringBuilder stringBuilder) {
        String STRING_SEPARATOR = "\n";
        return separatorCheck(stringBuilder, "\n");
    }
    public static String separatorCheck(StringBuilder stringBuilder, String STRING_SEPARATOR) {
        String toReturn = stringBuilder.toString();
        if (toReturn.endsWith(STRING_SEPARATOR)) {
            toReturn = toReturn.substring(0, toReturn.length() - STRING_SEPARATOR.length());
        }

        return toReturn;
    }

    public static String getAllRanks() {
        StringBuilder stringBuilder = new StringBuilder();
        for (RankType rankType : values()) {
            stringBuilder.append(rankType.getColor()).append(rankType.color + rankType.getRankName());
            stringBuilder.append(ChatColor.WHITE).append(", ");
        }

        if (stringBuilder.length() == 0) {
            return "Sunucu üzerinde rütbe grubu yok.";
        }

        return separatorCheck(stringBuilder, ", ");
    }
}
