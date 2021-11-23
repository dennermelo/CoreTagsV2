package com.github.dennermelo.core.tags.listener;

import com.github.dennermelo.core.tags.CoreTags;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {

    private final List<Listener> listeners;
    private final CoreTags instance;

    public ListenerManager(CoreTags instance) {
        this.listeners = new ArrayList<>();
        this.instance = instance;
    }

    public void add(Listener listener) {
        this.listeners.add(listener);
    }

    public void register() {
        listeners.forEach(listener -> {
            instance.getServer().getPluginManager().registerEvents(listener, instance);
            instance.getLogger().info("Registered listener: " + listener.getClass().getSimpleName());
        });
    }
}
