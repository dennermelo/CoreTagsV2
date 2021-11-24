package com.github.dennermelo.core.tags;

import com.github.dennermelo.core.tags.command.TagsCommand;
import com.github.dennermelo.core.tags.hook.HookManager;
import com.github.dennermelo.core.tags.listener.ListenerManager;
import com.github.dennermelo.core.tags.listener.player.PlayerGeneralListener;
import com.github.dennermelo.core.tags.manager.RarityManager;
import com.github.dennermelo.core.tags.manager.TagManager;
import com.github.dennermelo.core.tags.manager.UserManager;
import com.github.dennermelo.core.tags.model.inventory.InventoryListener;
import com.github.dennermelo.core.tags.model.inventory.item.ItemList;
import com.github.dennermelo.core.tags.setting.Settings;
import com.github.dennermelo.core.tags.sql.SQLManager;
import com.github.dennermelo.core.tags.type.Messages;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
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
    @Getter
    public static PlayerPointsAPI playerPointsAPI;
    @Getter
    public static Economy economy;
    @Getter
    private static HookManager hookManager;
    @Getter
    private static ListenerManager listenerManager;

    public static JavaPlugin getInstance() {
        return getPlugin(CoreTags.class);
    }

    @Override
    public void onEnable() {

        // Creating config file;
        saveDefaultConfig();

        // Load settings;
        Settings.load();

        // Loading messages;
        Messages.load();

        // Initializing managers;
        sqlManager = new SQLManager(this);
        rarityManager = new RarityManager();
        tagManager = new TagManager();
        userManager = new UserManager(this);
        listenerManager = new ListenerManager(this);

        // Loading configurations;
        rarityManager.load(this);
        tagManager.load(this);

        // Loading item cache;
        ItemList.load(getInstance().getConfig());

        // Enabling hooks;
        hookManager = new HookManager();
        hookManager.hook();

        // Registering events;

        listenerManager.add(new PlayerGeneralListener());
        listenerManager.add(new InventoryListener());
        listenerManager.register();

        // Registering commands;
        getCommand("tags").setExecutor(new TagsCommand());

        // Loading users;
        userManager.load();

        // Running auto-save;
        runAutoSave();
    }

    @Override
    public void onDisable() {
        // Saving all users;
        userManager.save();
    }

    // Auto-save;
    public void runAutoSave() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            getUserManager().save();
            getLogger().info("[Auto-Save] All users have been updated in database.");
        }, 480 * 20L);
    }

}
