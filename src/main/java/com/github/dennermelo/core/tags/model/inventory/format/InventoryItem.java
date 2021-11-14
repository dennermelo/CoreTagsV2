package com.github.dennermelo.core.tags.model.inventory.format;

import com.github.dennermelo.core.tags.model.inventory.InventoryBuilder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface InventoryItem {

    ItemStack getItem(Inventory inventory, InventoryBuilder<?> builder);

}