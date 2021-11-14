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
        UserManager userManager = CoreTags.getUserManager();

        userManager.getUsers().clear();
        User user = new User(event.getPlayer().getName());
        user.addTagInUse(CoreTags.getTagManager().getTag("Pro"));
        user.addTagInUse(CoreTags.getTagManager().getTag("Master"));
        user.addTagInUse(CoreTags.getTagManager().getTag("Grandmaster"));
        userManager.addUser(user);
        event.getPlayer().sendMessage("§aBem vindo ao servidor §e§lCore§a!");
    }
}
