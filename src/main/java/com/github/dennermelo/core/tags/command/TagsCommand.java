package com.github.dennermelo.core.tags.command;

import com.github.dennermelo.core.tags.inventory.TagsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("tags")) {
            Player player = (Player) commandSender;
            new TagsInventory().open(player);
            return true;
        }
        return false;
    }
}
