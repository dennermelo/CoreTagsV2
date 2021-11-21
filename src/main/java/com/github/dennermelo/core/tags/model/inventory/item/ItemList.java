package com.github.dennermelo.core.tags.model.inventory.item;

import com.github.dennermelo.core.tags.model.inventory.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public enum ItemList {

    TAG_ITEM_USING("Items.Tag.using"),
    TAG_ITEM_OWNED("Items.Tag.owned"),
    TAG_ITEM_NOT_OWNED("Items.Tag.not-owned"),
    FILTERS_FILTER_ITEM("Items.Filters.filter"),
    FILTERS_EQUIPPED_ITEM("Items.Filters.Equipped.default"),
    FILTERS_EQUIPPED_ITEM_SELECTED("Items.Filters.Equipped.selected"),
    FILTERS_BOUGHT_ITEM("Items.Filters.Bought.default"),
    FILTERS_BOUGHT_ITEM_SELECTED("Items.Filters.Bought.selected"),
    FILTERS_NOT_BOUGHT_ITEM("Items.Filters.Not-Bought.default"),
    FILTERS_NOT_BOUGHT_ITEM_SELECTED("Items.Filters.Not-Bought.selected"),
    FILTERS_ALL_ITEM("Items.Filters.All.default"),
    FILTERS_ALL_ITEM_SELECTED("Items.Filters.All.selected"),
    INVENTORIES_NEXT_PAGE("Items.Inventories.next-page"),
    INVENTORIES_PREVIOUS_PAGE("Items.Inventories.previous-page"),
    INVENTORIES_BACK("Items.Inventories.back"),
    ;

    private final String path;
    private ItemStack item;

    ItemList(String path) {
        this.path = path;
    }

    public static void load(FileConfiguration config) {
        for (ItemList item : ItemList.values()) {
            String[] itemString = config.getString(item.path).split(";");
            int id = Integer.parseInt(itemString[0]);
            short data = Short.parseShort(itemString[1]);
            String name = itemString[2].replace("&", "§");
            List<String> lore = Arrays.asList(itemString[3].replace("&", "§").split("<nl>"));
            item.item = new ItemBuilder(Material.getMaterial(id)).setDurability(data).setName(name).setLore(lore).build();
        }
    }

    public ItemStack get() {
        return item;
    }
}
