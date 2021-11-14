package com.github.dennermelo.core.tags.model.inventory.type;

import com.github.dennermelo.core.tags.model.inventory.InventoryBuilder;
import com.github.dennermelo.core.tags.model.inventory.format.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VoidItem implements InventoryItem {

    @Override
    public ItemStack getItem(Inventory inventory, InventoryBuilder<?> builder) {
        return null;
    }
}