package com.github.dennermelo.core.tags.util;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.enums.EconomyType;
import com.github.dennermelo.core.tags.model.Tag;
import com.github.dennermelo.core.tags.model.User;
import com.github.dennermelo.core.tags.setting.Settings;
import com.github.dennermelo.core.tags.type.Messages;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TagUtil {

    public static String inventoryClick(Player player, Tag tag) {
        User user = CoreTags.getUserManager().getUser(player.getName());
        player.closeInventory();
        if (user.getTagsInUse().contains(tag)) {
            user.removeTagInUse(tag);
            player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
            return Messages.TAG_UNEQUIPPED.asString();
        } else if (user.getTags().contains(tag)) {
            if (user.getTagsInUse().size() < Settings.MAX_TAGS_IN_USE.asInteger()) {
                user.addTagInUse(tag);
                player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                return Messages.TAG_EQUIPPED.asString();
            } else {
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                return Messages.TAG_MAX_IN_USE.asString();
            }
        } else {
            if (tag.getEconomy().equals(EconomyType.CASH)) {
                if (CoreTags.getPlayerPointsAPI().look(player.getUniqueId()) >= tag.getPrice()) {
                    CoreTags.getPlayerPointsAPI().take(player.getUniqueId(), (int) tag.getPrice());
                    user.addTag(tag);
                    if (user.getTagsInUse().size() < Settings.MAX_TAGS_IN_USE.asInteger()) user.addTagInUse(tag);
                    player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                    return Messages.TAG_OWNED.asString().replace("%tag%", tag.getFormat());
                } else {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                    return Messages.ERROR_NOT_HAVE_ECONOMY.asString().replace("%economy%", tag.getEconomy().toString());
                }
            } else if (tag.getEconomy().equals(EconomyType.COINS)) {
                if (CoreTags.getEconomy().has(player, tag.getPrice())) {
                    CoreTags.getEconomy().withdrawPlayer(player, tag.getPrice());
                    user.addTag(tag);
                    if (user.getTagsInUse().size() < Settings.MAX_TAGS_IN_USE.asInteger()) user.addTagInUse(tag);
                    player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                    return Messages.TAG_OWNED.asString().replace("%tag%", tag.getFormat());
                } else {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                    return Messages.ERROR_NOT_HAVE_ECONOMY.asString().replace("%economy%", tag.getEconomy().toString());
                }
            }
        }
        return "§c§lERRO: §7Ocorreu um erro, contate a administração para verificar.";
    }
}
