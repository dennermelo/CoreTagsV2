package com.github.dennermelo.core.tags.model.inventory.format;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Interface to format of inventories
 *
 * @param <T> The type of format
 */
public interface InventoryFormat<T> {

    T getValue(int slot);

    boolean isValid(int slot);

    void accept(InventoryClickEvent event);
}