package com.github.dennermelo.core.tags.setting;

import com.github.dennermelo.core.tags.CoreTags;
import org.bukkit.configuration.file.FileConfiguration;

public enum Settings {

    MYSQL_USER("Connection.MySQL.username"),
    MYSQL_PASS("Connection.MySQL.password"),
    MYSQL_HOST("Connection.MySQL.host"),
    MYSQL_PORT("Connection.MySQL.port"),
    MYSQL_DATABASE("Connection.MySQL.database"),
    MAX_TAGS_IN_USE("Preferences.max-tags-in-use");

    private final String path;
    public Object value;

    private Settings(final String path) {
        this.path = path;
    }

    public static void load() {
        final FileConfiguration config = CoreTags.getPlugin(CoreTags.class).getConfig();
        for (final Settings settings : values()) {
            settings.value = config.get(settings.path);
        }
    }

    public String asString() {
        return (String) this.value;
    }

    public boolean asBoolean() {
        return (boolean) this.value;
    }

    public int asInteger() {
        return (int) this.value;
    }

    public double asDouble() {
        return (double) this.value;
    }
}