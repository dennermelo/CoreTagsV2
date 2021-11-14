package com.github.dennermelo.core.tags.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public class GuiHolder implements InventoryHolder {

    private final Consumer<InventoryClickEvent> consumer;

    @Override
    public Inventory getInventory() {
        return null;
    }
}