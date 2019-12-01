package com.climpy.profile.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.*;

@Getter @AllArgsConstructor
public enum RankType {
    OWNER("§4Owner §c", "", "owner", "§4Owner", ChatColor.RED, Arrays.asList("worldedit.*", "fawe.admin")),
    CO_OWNER("§4Co-Owner §c", "", "coowner", "§4Co-Owner", ChatColor.RED, Arrays.asList("worldedit.*", "fawe.admin")),
    ADMIN("§4Admin §c", "", "admin", "§cAdmin", ChatColor.RED, Arrays.asList("worldedit.*", "fawe.admin")),
    DEVELOPER("§bDeveloper ", "", "developer", "§bDeveloper", ChatColor.AQUA, Arrays.asList("worldedit.*", "fawe.admin")),
    BUILDTEAMPLUS("§3Mimar§c+ §b", "", "buildteam+", "§3Build Team§c+", ChatColor.AQUA, Arrays.asList("worldedit.*", "fawe.admin")),
    BUILDTEAM("§3Mimar §b", "", "buildteam", "§3Build Team", ChatColor.BLUE, Arrays.asList("worldedit.*", "fawe.admin")),
    MODPLUS("§2Mod§c+ §2", "", "mod+", "§2Mod§c+", ChatColor.DARK_GREEN, Arrays.asList("worldedit.*", "fawe.admin")),
    MOD("§2Mod ", "", "mod", "§2Mod", ChatColor.DARK_GREEN, Arrays.asList("worldedit.*", "fawe.admin", "fawe.admin")),
    CHAT_MOD("§bChatMod §9", "", "chat_mod", "§bChatMod", ChatColor.AQUA, Arrays.asList("worldedit.*", "fawe.admin")),
    HELPER("§9Yardımcı §b", "", "helper", "§9Helper", ChatColor.AQUA, Collections.emptyList()),
    TRAILERHELPER("§9Trailer Helper §b", "", "trailerhelper", "§9T. Helper", ChatColor.BLUE, Collections.emptyList()),
    YOUTUBER("§6Youtuber ", "", "youtuber", "§6Youtuber", ChatColor.GOLD, Collections.emptyList()),
    CVIPPLUSPLUS("§eCVIP§c++ ", "", "cvip++", "§eCVIP§c++", ChatColor.YELLOW, Collections.emptyList()),
    CVIPPLUS("§eCVIP§c+ ", "", "cvip+", "§eCVIP§c+", ChatColor.YELLOW, Collections.emptyList()),
    CVIP("§eCVIP ", "", "cvip", "§eCVIP", ChatColor.YELLOW, Collections.emptyList()),
    MVIPPLUS("§dMVIP§e+§d ", "", "mvip+", "§dMVIP§e+", ChatColor.LIGHT_PURPLE, Collections.emptyList()),
    MVIP("§dMVIP ", "", "mvip", "§dMVIP", ChatColor.LIGHT_PURPLE, Collections.emptyList()),
    VIPPLUS("§bVIP§6+§b ", "", "vip+", "§bVIP§6+", ChatColor.AQUA, Collections.emptyList()),
    VIP("§bVIP ", "", "vip", "§bVIP", ChatColor.AQUA, Collections.emptyList()),
    DEFAULT("§9", "", "default", "§7Normal", ChatColor.BLUE, Collections.emptyList());

    private String prefix, suffix, rankName, displayName;
    private ChatColor color;
    private List<String> permissions = new LinkedList<>();

    public boolean isAboveOrEqual(RankType rank) {
        return (ordinal() <= rank.ordinal());
    }

    public static RankType getRankOrDefault(String internalName, String... anan) {
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
