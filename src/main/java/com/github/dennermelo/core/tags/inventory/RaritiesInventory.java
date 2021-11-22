package com.github.dennermelo.core.tags.inventory;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.model.Rarity;
import com.github.dennermelo.core.tags.model.Tag;
import com.github.dennermelo.core.tags.model.User;
import com.github.dennermelo.core.tags.model.inventory.InventoryBuilder;
import com.github.dennermelo.core.tags.model.inventory.item.ItemList;
import com.github.dennermelo.core.tags.type.Messages;
import com.github.dennermelo.core.tags.util.TagUtil;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class RaritiesInventory {

    public void selector(Player player) {
        new InventoryBuilder<Rarity>(Messages.INVENTORIES_NAME_RARITIES.asString(), 3)
                .withItems(CoreTags.getRarityManager().getRarities(), (event, builder, value) -> {
                    open(player, value);
                })
                .withSlotStart(11)
                .withSlotExit(15)
                .withPage(1, 5)
                .withNextPage(16, ItemList.INVENTORIES_NEXT_PAGE.get().clone())
                .withBackPage(10, ItemList.INVENTORIES_PREVIOUS_PAGE.get().clone())
                .withItem(22, ItemList.INVENTORIES_BACK.get().clone(), (event, builder, value) -> {
                    new TagsInventory().open(player);
                })
                .open(player);
    }

    public void open(Player player, Rarity rarity) {
        User user = CoreTags.getUserManager().getUser(player.getName());
        new InventoryBuilder<Tag>("§7Filtrando: " + rarity.getFormat(), 3)
                .addData("user", user)
                .withItems(CoreTags.getTagManager().getTags().stream().filter(tag -> tag.getRarity() == rarity).collect(Collectors.toList()), (event, builder, value) -> {
                    player.sendMessage(TagUtil.inventoryClick(player, value));
                })
                .withSlotStart(11)
                .withSlotExit(15)
                .withPage(1, 5)
                .withNextPage(16, ItemList.INVENTORIES_NEXT_PAGE.get().clone())
                .withBackPage(10, ItemList.INVENTORIES_PREVIOUS_PAGE.get().clone())
                .withItem(22, ItemList.INVENTORIES_BACK.get().clone(), (event, builder, value) -> {
                    selector(player);
                })
                .open(player);
    }

}
