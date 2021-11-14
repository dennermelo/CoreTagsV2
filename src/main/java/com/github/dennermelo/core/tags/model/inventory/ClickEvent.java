package com.github.dennermelo.core.tags.model.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface ClickEvent<T> {

    void onClick(InventoryClickEvent event, InventoryBuilder<?> builder, T value);

}