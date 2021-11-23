package com.github.dennermelo.core.tags.model;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.model.inventory.InventoryBuilder;
import com.github.dennermelo.core.tags.model.inventory.ItemBuilder;
import com.github.dennermelo.core.tags.model.inventory.format.InventoryItem;
import com.github.dennermelo.core.tags.setting.Settings;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Rarity implements InventoryItem {

    private final String name, format;
    private final ItemStack icon;

    public Rarity(String name, String format, ItemStack icon) {
        this.name = name;
        this.format = format.replace("&", "§");
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

    @Override
    public ItemStack getItem(Inventory inventory, InventoryBuilder<?> builder) {
        long amount = CoreTags.getTagManager().getTags().stream().filter(tag -> tag.getRarity() == this).count();
        return new ItemBuilder(icon.clone())
                .setName(Settings.RARITY_ITEM_NAME.asString().replace("%rarity%", format).replace("%amount%", String.valueOf(amount)))
                .setLore(Settings.RARITY_ITEM_LORE.asList().stream().map(lore -> lore.replace("%rarity%", format).replace("%amount%", String.valueOf(amount))).collect(java.util.stream.Collectors.toList()))
                .build();
    }
}
