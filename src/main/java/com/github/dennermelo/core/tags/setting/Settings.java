package com.github.dennermelo.core.tags.setting;

import com.github.dennermelo.core.tags.CoreTags;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public enum Settings {

    MYSQL_USER("Connection.MySQL.username"),
    MYSQL_PASS("Connection.MySQL.password"),
    MYSQL_HOST("Connection.MySQL.host"),
    MYSQL_PORT("Connection.MySQL.port"),
    MYSQL_DATABASE("Connection.MySQL.database"),
    MAX_TAGS_IN_USE("Preferences.max-tags-in-use"),
    ECONOMY_CASH_FORMAT("Preferences.Economy.cash"),
    ECONOMY_COINS_FORMAT("Preferences.Economy.coins"),
    PERMISSION_ADMIN("Preferences.admin-permission"),
    TAG_INFORMATION("Format.Tag.information"),
    RARITY_ITEM_NAME("Format.Rarity.name"),
    RARITY_ITEM_LORE("Format.Rarity.information"),
    ;

    private final String path;
    public Object value;

    Settings(final String path) {
        this.path = path;
    }

    public static void load() {
        final FileConfiguration config = CoreTags.getPlugin(CoreTags.class).getConfig();
        for (final Settings settings : values()) {
            settings.value = config.get(settings.path);
        }
    }

    public String asString() {
        return ((String) this.value).replace("&", "§");
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

    public List<String> asList() {
        return (List<String>) this.value;
    }
}