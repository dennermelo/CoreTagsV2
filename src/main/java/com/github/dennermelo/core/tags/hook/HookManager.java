package com.github.dennermelo.core.tags.hook;


import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.listener.player.legendchat.PlayerLegendChatListener;
import com.github.dennermelo.core.tags.papi.TagsExpansion;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class HookManager {

    public Map<String, Boolean> hookMap;

    public HookManager() {
        this.hookMap = new HashMap<String, Boolean>() {{
            put("Vault", false);
            put("PlayerPoints", false);
            put("PlaceholderAPI", false);
            put("Legendchat", false);
        }};
    }

    public void hook() {
        this.hookMap.keySet().forEach(hook -> {
            if (Bukkit.getPluginManager().isPluginEnabled(hook)) {
                hookMap.put(hook, true);
                Bukkit.getLogger().info("Hook " + hook + " enabled in the plugin!");
            }
        });
        if (!isEnabled("Vault")) {
            Bukkit.getLogger().warning("Hook Vault not found, disabling plugin!");
            Bukkit.getPluginManager().disablePlugin(CoreTags.getInstance());
            return;
        }
        if (!isEnabled("PlayerPoints")) {
            Bukkit.getLogger().warning("Hook PlayerPoints not found, disabling plugin!");
            Bukkit.getPluginManager().disablePlugin(CoreTags.getInstance());
            return;
        }
        if (isEnabled("PlaceholderAPI")) {
            new TagsExpansion().register();
        }
        if (isEnabled("Legendchat")) {
            CoreTags.getListenerManager().add(new PlayerLegendChatListener());
            Bukkit.getPluginManager().registerEvents(new PlayerLegendChatListener(), CoreTags.getInstance());
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            CoreTags.economy = rsp.getProvider();
        }
        CoreTags.playerPointsAPI = ((PlayerPoints) Bukkit.getPluginManager().getPlugin("PlayerPoints")).getAPI();
    }


    public boolean isEnabled(String hook) {
        return hookMap.getOrDefault(hook, false);
    }
}
