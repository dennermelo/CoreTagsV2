package com.github.dennermelo.core.tags.model;

import com.github.dennermelo.core.tags.enums.EconomyType;
import com.github.dennermelo.core.tags.model.inventory.InventoryBuilder;
import com.github.dennermelo.core.tags.model.inventory.ItemBuilder;
import com.github.dennermelo.core.tags.model.inventory.format.InventoryItem;
import com.github.dennermelo.core.tags.model.inventory.item.ItemList;
import com.github.dennermelo.core.tags.setting.Settings;
import com.github.dennermelo.core.tags.type.Messages;
import com.github.dennermelo.core.tags.util.NumberUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tag implements InventoryItem {

    private final String name, format;
    private final double price;
    private final EconomyType economy;
    private final List<String> description;
    private final boolean exclusive;
    private final Rarity rarity;

    public Tag(String name, String format, double price, EconomyType economy, List<String> description, boolean exclusive, Rarity rarity) {
        this.name = name;
        this.format = format;
        this.price = price;
        this.economy = economy;
        this.description = description;
        this.exclusive = exclusive;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public double getPrice() {
        return price;
    }

    public EconomyType getEconomy() {
        return economy;
    }

    public List<String> getDescription() {
        return description;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public ItemStack getItem(Inventory inventory, InventoryBuilder<?> builder) {
        User user = (User) builder.getData("user");
        if (user == null) return null;

        List<String> lore = new ArrayList<>();
        for (String line : Settings.TAG_INFORMATION.asList()) {
            if (!line.contains("%description%")) {
                lore.add(line.replace("%format%", this.format)
                        .replace("%rarity%", this.rarity.getFormat())
                        .replace("%price%", NumberUtil.format(this.price))
                        .replace("%type%", this.economy.toString())
                        .replace("&", "§"));
            } else {
                lore.addAll(this.getDescription().stream().map(s -> "§7" + s).collect(Collectors.toList()));
            }
        }
        if (user.getTagsInUse().contains(this)) {
            lore.addAll(Arrays.asList("", Messages.INVENTORIES_TAG_EQUIPPED.asString()));
            return new ItemBuilder(ItemList.TAG_ITEM_USING.get().clone())
                    .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                    .addFlags(ItemFlag.HIDE_ENCHANTS)
                    .setName(ItemList.TAG_ITEM_USING.get().getItemMeta().getDisplayName().replace("%name%", this.getName())
                            .replace("%format%", this.getFormat()))
                    .setLore(lore)
                    .build();
        } else if (user.getTags().contains(this)) {
            lore.addAll(Arrays.asList("", Messages.INVENTORIES_TAG_OWNED.asString()));
            return new ItemBuilder(ItemList.TAG_ITEM_OWNED.get().clone())
                    .setName(ItemList.TAG_ITEM_OWNED.get().getItemMeta().getDisplayName().replace("%name%", this.getName())
                            .replace("%format%", this.getFormat()))
                    .setLore(lore)
                    .build();
        }
        lore.addAll(Arrays.asList("", Messages.INVENTORIES_TAG_NOT_OWNED.asString()));
        return new ItemBuilder(ItemList.TAG_ITEM_NOT_OWNED.get().clone())
                .setName(ItemList.TAG_ITEM_NOT_OWNED.get().getItemMeta().getDisplayName().replace("%name%", this.getName())
                        .replace("%format%", this.getFormat()))
                .setLore(lore)
                .build();
    }
}
