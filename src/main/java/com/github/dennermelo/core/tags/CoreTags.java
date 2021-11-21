package com.github.dennermelo.core.tags;

import br.com.devpaulo.legendchat.api.Legendchat;
import br.com.devpaulo.legendchat.channels.ChannelManager;
import com.github.dennermelo.core.tags.command.TagsCommand;
import com.github.dennermelo.core.tags.listener.ListenerManager;
import com.github.dennermelo.core.tags.listener.player.InventoryInteractListener;
import com.github.dennermelo.core.tags.listener.player.PlayerGeneralListener;
import com.github.dennermelo.core.tags.listener.player.legendchat.PlayerLegendChatListener;
import com.github.dennermelo.core.tags.manager.RarityManager;
import com.github.dennermelo.core.tags.manager.TagManager;
import com.github.dennermelo.core.tags.manager.UserManager;
import com.github.dennermelo.core.tags.model.inventory.InventoryListener;
import com.github.dennermelo.core.tags.model.inventory.item.ItemList;
import com.github.dennermelo.core.tags.setting.Settings;
import com.github.dennermelo.core.tags.sql.SQLManager;
import com.github.dennermelo.core.tags.type.Messages;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class CoreTags extends JavaPlugin {

    @Getter
    private static SQLManager sqlManager;
    @Getter
    private static RarityManager rarityManager;
    @Getter
    private static TagManager tagManager;
    @Getter
    private static UserManager userManager;

    public static JavaPlugin getInstance() {
        return getPlugin(CoreTags.class);
    }

    @SneakyThrows
    @Override
    public void onEnable() {

        // Creating config file;
        saveDefaultConfig();

        // Load settings;
        Settings.load();

        // Initializing managers;
        sqlManager = new SQLManager(this);
        rarityManager = new RarityManager();
        tagManager = new TagManager();
        userManager = new UserManager(this);
        ListenerManager listenerManager = new ListenerManager(this);

        // Loading configurations;
        rarityManager.load(this);
        tagManager.load(this);

        // Loading item cache;
        ItemList.load(getInstance().getConfig());

        Messages.load();

        // Checking if Legendchat plugin is installed;
        if (Bukkit.getPluginManager().isPluginEnabled("Legendchat")) {
            ChannelManager channelManager = Legendchat.getChannelManager();
            listenerManager.add(new PlayerLegendChatListener());
        }

        // Registering events;
        listenerManager.add(new PlayerGeneralListener());
        listenerManager.add(new InventoryInteractListener());
        listenerManager.add(new InventoryListener());
        listenerManager.register();

        // Registering commands;
        getCommand("tags").setExecutor(new TagsCommand());

        // Loading users;
        userManager.load();

        runAutoSave();
    }

    @Override
    public void onDisable() {
        // Saving all users;
        userManager.save();
    }

    public void runAutoSave() {
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                getUserManager().save();
                getLogger().info("[Auto-Save] All users have been updated in database.");
            }
        }, 480 * 20L);
    }

}
