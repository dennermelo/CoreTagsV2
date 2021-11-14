package com.github.dennermelo.core.tags.model.inventory;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof GuiHolder)) return;

        event.setCancelled(true);
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
            holder.getConsumer().accept(event);
        }
    }
}