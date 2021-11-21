package com.github.dennermelo.core.tags.inventory;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.inventory.player.NotOwnedTagsInventory;
import com.github.dennermelo.core.tags.inventory.player.OwnedTagsInventory;
import com.github.dennermelo.core.tags.inventory.player.UsingTagsInventory;
import com.github.dennermelo.core.tags.model.Tag;
import com.github.dennermelo.core.tags.model.User;
import com.github.dennermelo.core.tags.model.inventory.InventoryBuilder;
import com.github.dennermelo.core.tags.model.inventory.item.ItemList;
import com.github.dennermelo.core.tags.type.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class TagsInventory implements Listener {

    public static void tagClick(Player player, Tag tag) {
        User user = CoreTags.getUserManager().getUser(player.getName());
        player.closeInventory();
        if (user.getTagsInUse().contains(tag)) {
            user.removeTagInUse(tag);
            player.sendMessage(Messages.TAG_UNEQUIPPED.asString().replace("%tag%", tag.getFormat()));
            player.closeInventory();
        } else if (user.getTags().contains(tag)) {
            if (user.getTagsInUse().size() < 3) {
                user.getTagsInUse().add(tag);
            } else {
                player.sendMessage(Messages.TAG_MAX_IN_USE.asString());
                return;
            }
            user.getTags().add(tag);
            player.sendMessage(Messages.TAG_EQUIPPED.asString().replace("%tag%", tag.getFormat()));
        } else {
            if (user.getTagsInUse().size() < 3) {
                user.getTagsInUse().add(tag);
            }
            user.getTags().add(tag);
            player.sendMessage(Messages.TAG_OWNED.asString().replace("%tag%", tag.getFormat()));
        }
    }

    public void open(Player player) {
        User user = CoreTags.getUserManager().getUser(player.getName());

        new InventoryBuilder<Tag>(Messages.INVENTORIES_NAME_MAIN.asString(), 4)
                .addData("user", user)
                .withItem(15, ItemList.FILTERS_FILTER_ITEM.get().clone(), (event, builder, value) -> {
                    new RaritiesInventory().selector(player);
                })
                .withItem(14, ItemList.FILTERS_NOT_BOUGHT_ITEM.get().clone(), (event, builder, value) -> {
                    new NotOwnedTagsInventory().open(player);
                })
                .withItem(13, ItemList.FILTERS_EQUIPPED_ITEM.get().clone(), (event, builder, value) -> {
                    new UsingTagsInventory().open(player);

                })
                .withItem(12, ItemList.FILTERS_BOUGHT_ITEM.get().clone(), (event, builder, value) -> {
                    new OwnedTagsInventory().open(player);
                })
                .withItem(11, ItemList.FILTERS_ALL_ITEM_SELECTED.get().clone(), (event, builder, value) -> {
                    event.setCancelled(true);
                })
                .withItems(CoreTags.getTagManager().getTags(), (event, builder, value) -> {
                    tagClick(player, value);
                })
                .withNextPage(25, ItemList.INVENTORIES_NEXT_PAGE.get().clone())
                .withBackPage(19, ItemList.INVENTORIES_PREVIOUS_PAGE.get().clone())
                .withPage(1, 5)
                .withSlotStart(20)
                .withSlotExit(24)
                .withItems(CoreTags.getTagManager().getTags(), (event, builder, value) -> {

                })
                .open(player);
    }

}
