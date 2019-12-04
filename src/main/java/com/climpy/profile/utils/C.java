package com.climpy.profile.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class C {
    public static final String CHAT_BAR;

    static {
        CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------";
    }

    public static String color(final String mesaj) {
        return ChatColor.translateAlternateColorCodes('&', mesaj);
    }
}
