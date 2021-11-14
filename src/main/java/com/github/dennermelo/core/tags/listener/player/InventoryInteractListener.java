package com.github.dennermelo.core.tags.listener.player;

import com.github.dennermelo.core.tags.CoreTags;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryInteractListener implements Listener {


    @EventHandler
    public void onInventoryInteract(org.bukkit.event.inventory.InventoryInteractEvent event) {
        Player player = (Player) event.getWhoClicked();
    }
}
