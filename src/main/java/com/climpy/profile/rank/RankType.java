package com.climpy.profile.rank;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

@Getter @AllArgsConstructor
public enum RankType {
    OWNER("§4Owner §c", "", "Owner", "§4Owner", ChatColor.DARK_RED, Arrays.asList("worldedit.*", "fawe.admin")),
    CO_OWNER("§4Co-Owner §c", "", "Co-Owner", "§4Co-Owner", ChatColor.DARK_RED, Arrays.asList("worldedit.*", "fawe.admin")),
    ADMIN("§4Admin §c", "", "Admin", "§cAdmin", ChatColor.DARK_RED, Arrays.asList("worldedit.*", "fawe.admin")),
    DEVELOPER("§bDeveloper ", "", "Developer", "§bDeveloper", ChatColor.AQUA, Arrays.asList("worldedit.*", "fawe.admin")),
    BUILDTEAMPLUS("§3Mimar§c+ §b", "", "Build Team+", "§3Build Team§c+", ChatColor.BLUE, Arrays.asList("worldedit.*", "fawe.admin")),
    BUILDTEAM("§3Mimar §b", "", "Build Team", "§3Build Team", ChatColor.BLUE, Arrays.asList("worldedit.*", "fawe.admin")),
    MODPLUS("§2Mod§c+ §2", "", "MOD+", "§2Mod§c+", ChatColor.DARK_GREEN, Arrays.asList("worldedit.*", "fawe.admin")),
    MOD("§2Mod ", "", "Mod", "§2Mod", ChatColor.DARK_GREEN, Arrays.asList("worldedit.*", "fawe.admin", "fawe.admin")),
    CHAT_MOD("§bChatMod §9", "", "Chat Moderatör", "§bChatMod", ChatColor.AQUA, Arrays.asList("worldedit.*", "fawe.admin")),
    HELPER("§9Yardımcı §b", "", "Helper", "§9Helper", ChatColor.BLUE, Collections.emptyList()),
    TRAILERHELPER("§9Trailer Helper §b", "", "Trailer Helper", "§9T. Helper", ChatColor.BLUE, Collections.emptyList()),
    YOUTUBER("§6Youtuber ", "", "Youtuber", "§6Youtuber", ChatColor.GOLD, Collections.emptyList()),
    CVIPPLUSPLUS("§eCVIP§c++ ", "", "CVIP++", "§eCVIP§c++", ChatColor.YELLOW, Collections.emptyList()),
    CVIPPLUS("§eCVIP§c+ ", "", "CVIP+", "§eCVIP§c+", ChatColor.YELLOW, Collections.emptyList()),
    CVIP("§eCVIP ", "", "CVIP", "§eCVIP", ChatColor.YELLOW, Collections.emptyList()),
    MVIPPLUS("§dMVIP§e+§d ", "", "MVIP+", "§dMVIP§e+", ChatColor.LIGHT_PURPLE, Collections.emptyList()),
    MVIP("§dMVIP ", "", "MVIP", "§dMVIP", ChatColor.LIGHT_PURPLE, Collections.emptyList()),
    VIPPLUS("§bVIP§6+§b ", "", "VIP+", "§bVIP§6+", ChatColor.AQUA, Collections.emptyList()),
    VIP("§bVIP ", "", "VIP", "§bVIP", ChatColor.AQUA, Collections.emptyList()),
    DEFAULT("§9", "", "Varsayılan", "§7Normal", ChatColor.GRAY, Collections.emptyList());

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

    public List<String> getPlayerAllPermissions(Player player) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
        List<String> permissions = new ArrayList<>();
        permissions.addAll(this.permissions);

        //for (RankType rankType : inherited) {
            permissions.addAll(user.getRankType().getPermissions());
        //}

        return permissions;
    }
}
