package com.github.dennermelo.core.tags.inventory;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.inventory.player.NotOwnedTagsInventory;
import com.github.dennermelo.core.tags.inventory.player.OwnedTagsInventory;
import com.github.dennermelo.core.tags.inventory.player.UsingTagsInventory;
import com.github.dennermelo.core.tags.model.Tag;
import com.github.dennermelo.core.tags.model.User;
import com.github.dennermelo.core.tags.model.inventory.InventoryBuilder;
import com.github.dennermelo.core.tags.model.inventory.ItemBuilder;
import com.github.dennermelo.core.tags.model.inventory.item.ItemList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class TagsInventory implements Listener {

    public void open(Player player) {
        User user = CoreTags.getUserManager().getUser(player.getName());

        new InventoryBuilder<Tag>("Tags: Menu Principal", 4)
                .addData("user", user)
                .withItem(15, ItemList.filter, (event, builder, value) -> {
                    new RaritiesInventory().selector(player);
                })
                .withItem(14, ItemList.not_owned, (event, builder, value) -> {
                    new NotOwnedTagsInventory().open(player);
                })
                .withItem(13, ItemList.show_equipped, (event, builder, value) -> {
                    new UsingTagsInventory().open(player);

                })
                .withItem(12, ItemList.show_owned, (event, builder, value) -> {
                    new OwnedTagsInventory().open(player);
                })
                .withItem(11, new ItemBuilder(ItemList.all_tags.clone()).setDurability((short) 10).setName("§aMostrando todas as Tags").build(), (event, builder, value) -> {
                    event.setCancelled(true);
                })
                .withItems(CoreTags.getTagManager().getTags(), (event, builder, value) -> {
                    tagClick(player, value);
                })
                .withNextPage(25, ItemList.next_page)
                .withBackPage(19, ItemList.previous_page)
                .withPage(1, 5)
                .withSlotStart(20)
                .withSlotExit(24)
                .withItems(CoreTags.getTagManager().getTags(), (event, builder, value) -> {

                })
                .open(player);
    }

    public static void tagClick(Player player, Tag tag) {
        User user = CoreTags.getUserManager().getUser(player.getName());
        player.closeInventory();
        if (user.getTagsInUse().contains(tag)) {
            user.removeTagInUse(tag);
            player.sendMessage("§5§lYAY! §aVocê desequipou a tag §f" + tag.getFormat() + "§a!");
            player.closeInventory();
        } else if (user.getTags().contains(tag)) {
            if (user.getTagsInUse().size() < 3) {
                user.getTagsInUse().add(tag);
            } else {
                player.sendMessage("§4§lOPS! §cVocê está com o máximo de tags equipadas, remova uma e tente novamente.");
                return;
            }
            user.getTags().add(tag);
            player.sendMessage("§5§lYAY! §aVocê equipou a tag §f" + tag.getFormat() + " §acom sucesso!");
        } else {
            if (user.getTagsInUse().size() < 3) {
                user.getTagsInUse().add(tag);
            }
            user.getTags().add(tag);
            player.sendMessage("§5§lYAY! §aVocê comprou a tag §f" + tag.getFormat() + " §acom sucesso!");
        }
    }

}
