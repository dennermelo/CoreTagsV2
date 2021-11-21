package com.github.dennermelo.core.tags.listener.player;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.manager.UserManager;
import com.github.dennermelo.core.tags.model.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerGeneralListener implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        UserManager userManager = CoreTags.getUserManager();

        if (!userManager.hasUser(event.getPlayer().getName())) {
            userManager.addUser(new User(event.getPlayer().getName()));
        }
        for (int i = 0; i < 100; i++) {
            event.getPlayer().sendMessage("");
        }
    }
}
