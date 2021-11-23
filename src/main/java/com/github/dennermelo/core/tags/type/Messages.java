package com.github.dennermelo.core.tags.type;

import com.github.dennermelo.core.tags.CoreTags;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Messages {

    INVENTORIES_TAG_EQUIPPED("Inventories.Tag.equipped"),
    INVENTORIES_TAG_OWNED("Inventories.Tag.owned"),
    INVENTORIES_TAG_NOT_OWNED("Inventories.Tag.not-owned"),
    INVENTORIES_NAME_MAIN("Inventories.Name.main"),
    INVENTORIES_NAME_RARITIES("Inventories.Name.rarities"),
    TAG_COMMAND_USAGE("Messages.Tag.Command.Usage.tag"),
    TAG_COMMAND_USAGE_GIVE("Messages.Tag.Command.Usage.give"),
    TAG_COMMAND_USAGE_TAKE("Messages.Tag.Command.Usage.take"),
    TAG_COMMAND_GIVE("Messages.Tag.Command.give"),
    TAG_COMMAND_TAKE("Messages.Tag.Command.take"),
    TAG_EQUIPPED("Messages.Tag.equipped"),
    TAG_UNEQUIPPED("Messages.Tag.unequipped"),
    TAG_OWNED("Messages.Tag.owned"),
    TAG_MAX_IN_USE("Messages.Tag.max-in-use"),
    ERROR_NOT_HAVE_ECONOMY("Messages.Error.not-have-economy"),
    ERROR_NOT_HAVE_PERMISSION("Messages.Error.without-permission"),
    ERROR_TAG_NOT_EXIST("Messages.Error.tag-not-exist"),
    ERROR_PLAYER_WITHOUT_TAG("Messages.Error.player-without-tag"),
    ERROR_PLAYER_NOT_EXIST("Messages.Error.player-not-exist"),
    ERROR_PLAYER_ALREADY_HAVE_TAG("Messages.Error.player-already-have-tag"),
    ;

    private final String path;
    private Object value;

    Messages(final String path) {
        this.path = path;
    }

    public static void load() {
        if (!new File(CoreTags.getInstance().getDataFolder() + File.separator + "lang", CoreTags.getInstance().getConfig().getString("Preferences.language") + ".yml").exists()) {
            CoreTags.getInstance().saveResource("lang/" + CoreTags.getInstance().getConfig().getString("Preferences.language") + ".yml", false);
        }
        File file = new File(CoreTags.getInstance().getDataFolder() + File.separator + "lang", CoreTags.getInstance().getConfig().getString("Preferences.language") + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Messages message : values()) {
            message.value = config.getString(message.path);
        }
    }

    public String asString() {
        return ((String) value).replace("&", "§");
    }
}
