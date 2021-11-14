package com.github.dennermelo.core.tags.inventory;

import com.github.dennermelo.core.tags.CoreTags;
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainTagsInventory implements Listener {

    private static HashMap<ItemStack, Tag> items = new HashMap<>();
    private static ItemStack filter, show_equiped, show_owned, not_owned, all_tags;
    private Scroller scroller;

    public void open(Player player) {
        User user = CoreTags.getUserManager().getUser(player.getName());
        items.clear();
        CoreTags.getTagManager().getTags().forEach(tag -> {
            tagList(player, tag);
        });
        filter = new ItemBuilder(Material.ANVIL).setName("§8Filtrar tags").setLore(Arrays.asList("", "§7Clique para filtrar tags por raridade.")).build();
        show_equiped = new ItemBuilder(Material.INK_SACK).setDurability((short) 8).setName("§aMostrar Tags Equipadas").setLore(Arrays.asList("", "§7Clique para mostrar tags equipadas.")).build();
        show_owned = new ItemBuilder(Material.INK_SACK).setDurability((short) 8).setName("§aMostrar Tags Compradas").setLore(Arrays.asList("", "§7Clique para mostrar tags compradas.")).build();
        not_owned = new ItemBuilder(Material.INK_SACK).setDurability((short) 8).setName("§cMostrar Tags Não Compradas").setLore(Arrays.asList("", "§7Clique para mostrar tags não compradas.")).build();
        all_tags = new ItemBuilder(Material.INK_SACK).setDurability((short) 10).setName("§aMostrando Todas as Tags").setLore(Arrays.asList("", "§7Você está visualizando todas as tags.")).build();

        Scroller scroller = new Scroller.Builder().withName("Tags: Menu Principal")
                .withSize(9 * 5)
                .withItems(items.keySet())
                .withAllowedSlots(Arrays.asList(29, 30, 31, 32, 33))
                .withCustomItem(24, filter)
                .withCustomItem(23, not_owned)
                .withCustomItem(22, show_equiped)
                .withCustomItem(21, show_owned)
                .withCustomItem(20, all_tags)
                .withPreviousPageSlot(28)
                .withNextPageSlot(34)
                .build();

        scroller.open(player);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getInventory().getName().equals("Tags: Menu Principal")) {
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem() != null) {
                items.forEach((item, tag) -> {
                    if (item.equals(e.getCurrentItem())) {
                        player.sendMessage("teste");
                        tagActionClick(player, tag);
                    }
                });
                if (e.getCurrentItem().equals(filter)) {
                    player.closeInventory();
                    new RaritiesInventory().openSelector(player);
                }
            }
            e.setCancelled(true);
        }
    }

    static void tagActionClick(Player player, Tag tag) {
        if (CoreTags.getUserManager().getUser(player.getName()).getTagsInUse().contains(tag)) {
            CoreTags.getUserManager().getUser(player.getName()).removeTagInUse(tag);
            player.sendMessage("§5§lYAY! §aVocê desequipou a tag §f" + tag.getFormat() + "§a!");
            player.closeInventory();
        } else if (CoreTags.getUserManager().getUser(player.getName()).getTags().contains(tag)) {
            if (CoreTags.getUserManager().getUser(player.getName()).getTagsInUse().size() < 3) {
                CoreTags.getUserManager().getUser(player.getName()).addTagInUse(tag);
                player.sendMessage("§5§lYAY! §aVocê equipou a tag §f" + tag.getFormat() + " §acom sucesso!");
            } else {
                player.sendMessage("§4§lOPS! §cVocê está com o máximo de tags equipadas, remova uma e tente novamente.");
            }
            player.closeInventory();
        } else if (!CoreTags.getUserManager().getUser(player.getName()).getTags().contains(tag)) {
            if (CoreTags.getUserManager().getUser(player.getName()).getTagsInUse().size() < 3) {
                CoreTags.getUserManager().getUser(player.getName()).addTagInUse(tag);
            }
            CoreTags.getUserManager().getUser(player.getName()).addTag(tag);
            player.sendMessage("§5§lYAY! §aVocê adquiriu a tag §f" + tag.getFormat() + " §acom sucesso!");
            player.closeInventory();
        }
    }

    static void tagList(Player player, Tag tag) {
        User user = CoreTags.getUserManager().getUser(player.getName());
        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList("", "§7Informações sobre esta tag:", "", "§7Descrição:"));
        lore.addAll(tag.getDescription().stream().map(s -> "§7" + s).collect(Collectors.toList()));
        if (user.getTagsInUse().contains(tag)) {
            lore.addAll(Arrays.asList("", "§7Formato da tag: §e" + tag.getFormat(), "§7Raridade: §f" + tag.getRarity().getFormat()
                    , "§7Valor: §6$ §7" + tag.getPrice(), "", "§8Você está com esta tag selecionada, clique para retira-la."));
            items.put(new ItemBuilder(Material.STORAGE_MINECART).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addFlags(ItemFlag.HIDE_ENCHANTS).setName("§7Tag: §e" + tag.getName())
                    .setLore(lore).build(), tag);
            return;
        }
        if (!user.getTags().contains(tag)) {
            lore.addAll(Arrays.asList("", "§7Formato da tag: §e" + tag.getFormat(), "§7Raridade: §f" + tag.getRarity().getFormat()
                    , "§7Valor: §6$ §7" + tag.getPrice(), "", "§aVocê não possui esta tag, clique para comprar-la."));
            items.put(new ItemBuilder(Material.MINECART).setName("§7Tag: §e" + tag.getName())
                    .setLore(lore).build(), tag);
        } else {
            lore.addAll(Arrays.asList("", "§7Formato da tag: §e" + tag.getFormat(), "§7Raridade: §f" + tag.getRarity().getFormat()
                    , "§7Valor: §6$ §7" + tag.getPrice(), "", "§8Você possui esta tag, clique para selecionar-la."));
            items.put(new ItemBuilder(Material.STORAGE_MINECART).setName("§7Tag: §e" + tag.getName())
                    .setLore(lore).build(), tag);
        }
    }
}
