package com.github.dennermelo.core.tags.listener.player;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.manager.UserManager;
import com.github.dennermelo.core.tags.model.User;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerGeneralListener implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        UserManager userManager = CoreTags.getUserManager();
        if (!userManager.hasUser(event.getPlayer().getName())) {
            userManager.addUser(new User(event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void playerChatEvent(ChatMessageEvent event) {
        UserManager userManager = CoreTags.getUserManager();
        User user = userManager.getUser(event.getSender().getName());
        event.setMessage(PlaceholderAPI.setPlaceholders(event.getSender(), event.getMessage()));
    }
}
