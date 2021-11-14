package com.github.dennermelo.core.tags.model;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.enums.EconomyType;
import com.github.dennermelo.core.tags.model.Rarity;
import com.github.dennermelo.core.tags.model.inventory.InventoryBuilder;
import com.github.dennermelo.core.tags.model.inventory.ItemBuilder;
import com.github.dennermelo.core.tags.model.inventory.format.InventoryItem;
import org.bukkit.Material;
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
    private final EconomyType economyType;
    private final List<String> description;
    private final boolean exclusive;
    private final Rarity rarity;

    public Tag(String name, String format, double price, EconomyType economyType, List<String> description, boolean exclusive, Rarity rarity) {
        this.name = name;
        this.format = format;
        this.price = price;
        this.economyType = economyType;
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

    public EconomyType getEconomyType() {
        return economyType;
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
        lore.addAll(Arrays.asList("", "§7Informações sobre esta tag:", "", "§7Descrição:"));
        lore.addAll(description.stream().map(s -> "§7" + s).collect(Collectors.toList()));
        if (user.getTagsInUse().contains(this)) {
            lore.addAll(Arrays.asList("", "§7Formato da tag: §e" + this.getFormat(), "§7Raridade: §f" + this.getRarity().getFormat()
                    , "§7Valor: §6$ §7" + this.getPrice(), "", "§8Você está com esta tag selecionada, clique para retira-la."));
            return new ItemBuilder(Material.STORAGE_MINECART).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addFlags(ItemFlag.HIDE_ENCHANTS).setName("§7Tag: §e" + this.getName()).setLore(lore).build();
        } else if (!user.getTags().contains(this)) {
            lore.addAll(Arrays.asList("", "§7Formato da tag: §e" + this.getFormat(), "§7Raridade: §f" + this.getRarity().getFormat()
                    , "§7Valor: §6$ §7" + this.getPrice(), "", "§aVocê não possui esta tag, clique para comprar-la."));
            return new ItemBuilder(Material.MINECART).setName("§7Tag: §e" + this.getName())
                    .setLore(lore).build();
        } else {
            lore.addAll(Arrays.asList("", "§7Formato da tag: §e" + this.getFormat(), "§7Raridade: §f" + this.getRarity().getFormat()
                    , "§7Valor: §6$ §7" + this.getPrice(), "", "§8Você possui esta tag, clique para selecionar-la."));
            return new ItemBuilder(Material.STORAGE_MINECART).setName("§7Tag: §e" + this.getName())
                    .setLore(lore).build();
        }
    }
}
