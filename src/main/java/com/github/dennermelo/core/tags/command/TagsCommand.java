package com.github.dennermelo.core.tags.command;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.inventory.TagsInventory;
import com.github.dennermelo.core.tags.model.Tag;
import com.github.dennermelo.core.tags.setting.Settings;
import com.github.dennermelo.core.tags.type.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("tags")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (!player.hasPermission(Settings.PERMISSION_ADMIN.asString())) {
                    new TagsInventory().open(player);
                    return true;
                }
            }
            if (args.length == 0) {
                if (commandSender instanceof Player) {
                    new TagsInventory().open((Player) commandSender);
                    return true;
                }
                commandSender.sendMessage(Messages.TAG_COMMAND_USAGE.asString());
                return true;
            }
            if (args.length == 1 || args.length == 2) {
                switch (args[0]) {
                    case "help":
                        commandSender.sendMessage(Messages.TAG_COMMAND_USAGE.asString());
                        return true;
                    case "give":
                        commandSender.sendMessage(Messages.TAG_COMMAND_USAGE_GIVE.asString());
                        return true;
                    case "take":
                        commandSender.sendMessage(Messages.TAG_COMMAND_USAGE_TAKE.asString());
                        return true;
                }
            }
            if (args.length == 3) {
                switch (args[0]) {
                    case "give":
                        if (CoreTags.getUserManager().hasUser(args[1])) {
                            Tag tag = CoreTags.getTagManager().getTag(args[2]);
                            if (tag != null) {
                                if (CoreTags.getUserManager().getUser(args[1]).getTags().contains(tag)) {
                                    commandSender.sendMessage(Messages.ERROR_PLAYER_ALREADY_HAVE_TAG.asString()
                                            .replace("%player%", args[1])
                                            .replace("%tag%", tag.getFormat()));
                                    return true;
                                }
                                CoreTags.getUserManager().getUser(args[1]).addTag(tag);
                                if (CoreTags.getUserManager().getUser(args[1]).getTagsInUse().size() < Settings.MAX_TAGS_IN_USE.asInteger()) {
                                    CoreTags.getUserManager().getUser(args[1]).addTagInUse(tag);
                                }
                                commandSender.sendMessage(Messages.TAG_COMMAND_GIVE.asString()
                                        .replace("%player%", args[1])
                                        .replace("%tag%", tag.getFormat()));
                            } else {
                                commandSender.sendMessage(Messages.ERROR_TAG_NOT_EXIST.asString()
                                        .replace("%tag%", args[2]));
                            }
                        } else {
                            commandSender.sendMessage(Messages.ERROR_PLAYER_NOT_EXIST.asString()
                                    .replace("%player%", args[1]));
                        }
                        return true;
                    case "take":
                        if (CoreTags.getUserManager().hasUser(args[1])) {
                            Tag tag = CoreTags.getTagManager().getTag(args[2]);
                            if (tag != null) {
                                if (!CoreTags.getUserManager().getUser(args[1]).getTags().contains(tag)) {
                                    commandSender.sendMessage(Messages.ERROR_PLAYER_WITHOUT_TAG.asString()
                                            .replace("%tag%", tag.getFormat())
                                            .replace("%player%", args[1]));
                                    return true;
                                }
                                CoreTags.getUserManager().getUser(args[1]).removeTag(tag);
                                if (CoreTags.getUserManager().getUser(args[1]).getTagsInUse().contains(tag)) {
                                    CoreTags.getUserManager().getUser(args[1]).removeTagInUse(tag);
                                }
                                commandSender.sendMessage(Messages.TAG_COMMAND_TAKE.asString()
                                        .replace("%player%", args[1])
                                        .replace("%tag%", tag.getFormat()));
                            } else {
                                commandSender.sendMessage(Messages.ERROR_TAG_NOT_EXIST.asString()
                                        .replace("%player%", args[1])
                                        .replace("%tag%", args[2]));
                            }
                        } else {
                            commandSender.sendMessage(Messages.ERROR_PLAYER_NOT_EXIST.asString()
                                    .replace("%player%", args[1]));
                        }
                        return true;
                }
            }
        }
        return false;
    }
}
