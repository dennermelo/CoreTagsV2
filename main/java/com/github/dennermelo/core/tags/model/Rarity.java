package com.github.dennermelo.core.tags.model;

import org.bukkit.inventory.ItemStack;

public class Rarity {

    private final String name, format;
    private final ItemStack icon;

    public Rarity(String name, String format, ItemStack icon) {
        this.name = name;
        this.format = format;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public ItemStack getIcon() {
        return icon;
    }

}
