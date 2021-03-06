package com.github.dennermelo.core.tags.inventory.player;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.inventory.RaritiesInventory;
import com.github.dennermelo.core.tags.inventory.TagsInventory;
import com.github.dennermelo.core.tags.model.Tag;
import com.github.dennermelo.core.tags.model.User;
import com.github.dennermelo.core.tags.model.inventory.InventoryBuilder;
import com.github.dennermelo.core.tags.model.inventory.item.ItemList;
import com.github.dennermelo.core.tags.type.Messages;
import com.github.dennermelo.core.tags.util.TagUtil;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class NotOwnedTagsInventory {

    public void open(Player player) {
        User user = CoreTags.getUserManager().getUser(player.getName());

        new InventoryBuilder<Tag>(Messages.INVENTORIES_NAME_MAIN.asString(), 4)
                .addData("user", user)
                .withItem(15, ItemList.FILTERS_FILTER_ITEM.get().clone(), (event, builder, value) -> {
                    new RaritiesInventory().selector(player);
                })
                .withItem(14, ItemList.FILTERS_NOT_BOUGHT_ITEM_SELECTED.get().clone(), (event, builder, value) -> {
                    event.setCancelled(true);
                })
                .withItem(13, ItemList.FILTERS_EQUIPPED_ITEM.get().clone(), (event, builder, value) -> {
                    new UsingTagsInventory().open(player);
                })
                .withItem(12, ItemList.FILTERS_NOT_BOUGHT_ITEM.get().clone(), (event, builder, value) -> {
                    new OwnedTagsInventory().open(player);
                })
                .withItem(11, ItemList.FILTERS_ALL_ITEM.get().clone(), (event, builder, value) -> {
                    new TagsInventory().open(player);
                })
                .withItems(CoreTags.getTagManager().getTags().stream().filter(tag -> !user.getTags().contains(tag)).collect(Collectors.toList()), (event, builder, value) -> {
                    player.sendMessage(TagUtil.inventoryClick(player, value));
                })
                .withNextPage(25, ItemList.INVENTORIES_NEXT_PAGE.get().clone())
                .withBackPage(19, ItemList.INVENTORIES_PREVIOUS_PAGE.get().clone())
                .withPage(1, 5)
                .withSlotStart(20)
                .withSlotExit(24)
                .open(player);
    }
}
