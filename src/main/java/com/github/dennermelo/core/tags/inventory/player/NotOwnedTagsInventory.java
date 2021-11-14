package com.github.dennermelo.core.tags.inventory.player;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.inventory.RaritiesInventory;
import com.github.dennermelo.core.tags.inventory.TagsInventory;
import com.github.dennermelo.core.tags.model.Tag;
import com.github.dennermelo.core.tags.model.User;
import com.github.dennermelo.core.tags.model.inventory.InventoryBuilder;
import com.github.dennermelo.core.tags.model.inventory.ItemBuilder;
import com.github.dennermelo.core.tags.model.inventory.item.ItemList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class NotOwnedTagsInventory {

    public void open(Player player) {
        User user = CoreTags.getUserManager().getUser(player.getName());

        new InventoryBuilder<Tag>("Tags: Menu Principal", 4)
                .addData("user", user)
                .withItem(15, ItemList.filter, (event, builder, value) -> {
                    new RaritiesInventory().selector(player);
                })
                .withItem(14, new ItemBuilder(ItemList.not_owned.clone()).setDurability((short) 10).setName("§aMostrando Tags não adquiridas").build(), (event, builder, value) -> {
                    event.setCancelled(true);
                })
                .withItem(13, ItemList.show_equipped, (event, builder, value) -> {
                    new UsingTagsInventory().open(player);
                })
                .withItem(12, ItemList.show_owned, (event, builder, value) -> {
                    new OwnedTagsInventory().open(player);
                })
                .withItem(11, ItemList.all_tags, (event, builder, value) -> {
                    new TagsInventory().open(player);
                })
                .withItems(CoreTags.getTagManager().getTags().stream().filter(tag -> !user.getTags().contains(tag)).collect(Collectors.toList()), (event, builder, value) -> {
                    TagsInventory.tagClick(player, value);
                })
                .withNextPage(25, ItemList.next_page)
                .withBackPage(19, ItemList.previous_page)
                .withPage(1, 5)
                .withSlotStart(20)
                .withSlotExit(24)
                .open(player);
    }
}
