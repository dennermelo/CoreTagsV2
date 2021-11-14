package com.github.dennermelo.core.tags.inventory;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.model.Rarity;
import com.github.dennermelo.core.tags.model.Tag;
import com.github.dennermelo.core.tags.model.User;
import com.github.dennermelo.core.tags.model.inventory.ItemBuilder;
import com.github.dennermelo.core.tags.model.inventory.Scroller;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RaritiesInventory implements Listener {

    private Rarity rarity;
    private static HashMap<ItemStack, Rarity> rarities = new HashMap<>();
    private static HashMap<ItemStack, Tag> tags = new HashMap<>();


    public void openSelector(Player player) {
        User user = CoreTags.getUserManager().getUser(player.getName());
        rarities.clear();

        AtomicInteger i = new AtomicInteger();
        CoreTags.getRarityManager().getRarities().forEach(rarity -> {
            CoreTags.getTagManager().getTags().forEach(tag -> {
                        if (tag.getRarity().equals(rarity)) {
                            i.set(i.get() + 1);
                        }
                    }
            );
            rarities.put(new ItemBuilder(rarity.getIcon())
                    .setName("§7Raridade: §f" + rarity.getFormat())
                    .setLore(Arrays.asList("",
                            "§7Liste as tags classificadas com esta raridade.",
                            "§7Atualmente existem §f" + i.get() + " §7tags nesta raridade.",
                            "",
                            "§8Clique para visualizar."))
                    .build(), rarity);
        });

        Scroller scroller = new Scroller.Builder()
                .withName("Filtrar: Raridades")
                .withItems(rarities.keySet())
                .withAllowedSlots(Arrays.asList(11, 12, 13, 14, 15))
                .withSize(9 * 3)
                .withPreviousPageSlot(10)
                .withNextPageSlot(16).build();

        scroller.open(player);
    }

    public void open(Player player, Rarity rarity) {
        tags.clear();
        User user = CoreTags.getUserManager().getUser(player.getName());
        List<Tag> tagList = CoreTags.getTagManager().getTags().stream().filter(tag -> tag.getRarity().equals(rarity)).collect(
                java.util.stream.Collectors.toList());

        Scroller scroller = null;

        if (tagList.isEmpty()) {
            scroller = new Scroller.Builder()
                    .withName("Filtrar: " + rarity.getFormat())
                    .withCustomItem(13, new ItemBuilder(Material.BARRIER)
                            .setName("§cNão existem tags nesta raridade.")
                            .setLore(Arrays.asList("", "§7Não foram encontradas tags com esta raridade."))
                            .build())
                    .withSize(9 * 3)
                    .build();
            scroller.open(player);
        } else {
            tagList.forEach(tag -> {
                List<String> lore = new ArrayList<>();
                lore.addAll(Arrays.asList("", "§7Informações sobre esta tag:", "", "§7Descrição:"));
                lore.addAll(tag.getDescription().stream().map(s -> "§7" + s).collect(Collectors.toList()));
                if (user.getTagsInUse().contains(tag)) {
                    lore.addAll(Arrays.asList("", "§7Formato da tag: §e" + tag.getFormat(), "§7Raridade: §f" + tag.getRarity().getFormat()
                            , "§7Valor: §6$ §7" + tag.getPrice(), "", "§8Você está com esta tag selecionada, clique para retira-la."));
                    tags.put(new ItemBuilder(Material.STORAGE_MINECART).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addFlags(ItemFlag.HIDE_ENCHANTS).setName("§7Tag: §e" + tag.getName())
                            .setLore(lore).build(), tag);
                } else if (!user.getTags().contains(tag)) {
                    lore.addAll(Arrays.asList("", "§7Formato da tag: §e" + tag.getFormat(), "§7Raridade: §f" + tag.getRarity().getFormat()
                            , "§7Valor: §6$ §7" + tag.getPrice(), "", "§aVocê não possui esta tag, clique para comprar-la."));
                    tags.put(new ItemBuilder(Material.MINECART).setName("§7Tag: §e" + tag.getName())
                            .setLore(lore).build(), tag);
                } else {
                    lore.addAll(Arrays.asList("", "§7Formato da tag: §e" + tag.getFormat(), "§7Raridade: §f" + tag.getRarity().getFormat()
                            , "§7Valor: §6$ §7" + tag.getPrice(), "", "§8Você possui esta tag, clique para selecionar-la."));
                    tags.put(new ItemBuilder(Material.STORAGE_MINECART).setName("§7Tag: §e" + tag.getName())
                            .setLore(lore).build(), tag);
                }
            });
            scroller = new Scroller.Builder()
                    .withName("Filtrar: " + rarity.getFormat())
                    .withItems(tags.keySet())
                    .withAllowedSlots(Arrays.asList(11, 12, 13, 14, 15))
                    .withSize(9 * 3)
                    .withPreviousPageSlot(10)
                    .withNextPageSlot(16).build();
        }
        scroller.open(player);
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getName().equals("Filtrar: Raridades")) {
            rarities.forEach((item, rarity) -> {
                if (event.getCurrentItem().equals(item)) {
                    this.rarity = rarity;
                    open(player, rarity);
                    event.setCancelled(true);
                }
            });
        }
        if (rarity != null && event.getInventory().getName().equals("Filtrar: " + rarity.getFormat())) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                tags.forEach((item, tag) -> {
                    if (item.equals(event.getCurrentItem())) {
                        MainTagsInventory.tagList(player, tag);
                        event.setCancelled(true);
                    }
                });
            }
        }

    }
}
