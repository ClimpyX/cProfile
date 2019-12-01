package com.climpy.profile.listeners;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.events.ServerChangeEvent;
import com.climpy.profile.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ServerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerChangeEvent(ServerChangeEvent event) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(event.getUniqueUUID());
        if (user == null) {
            event.setCancelled(true);
            return;
        }

       user.save();
    }
}
