package com.climpy.profile.listeners;

import com.climpy.profile.ProfileAPI;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.user.User;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class SecurityListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void asyncPlayerLoginEvent(AsyncPlayerPreLoginEvent event) {

    }
}
