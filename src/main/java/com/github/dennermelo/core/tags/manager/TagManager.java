package com.github.dennermelo.core.tags.manager;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.enums.EconomyType;
import com.github.dennermelo.core.tags.model.Tag;
import com.github.dennermelo.core.tags.model.Rarity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TagManager {

    private final List<Tag> tags;

    public TagManager() {
        this.tags = new ArrayList<>();
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    public Tag getTag(String name) {
        for (Tag tag : this.tags) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public void load(CoreTags instance) {
        File file = new File(instance.getDataFolder(), "tags.yml");
        if (!file.exists()) {
            instance.saveResource("tags.yml", false);
        }
        FileConfiguration tagsConfiguration = YamlConfiguration.loadConfiguration(file);

        for (String tagKey : tagsConfiguration.getConfigurationSection("Tags").getKeys(false)) {
            String name = tagsConfiguration.get("Tags." + tagKey + ".name").toString();
            String format = tagsConfiguration.get("Tags." + tagKey + ".format").toString().replace("&", "§");
            double price = Double.parseDouble(tagsConfiguration.get("Tags." + tagKey + ".price").toString());
            EconomyType economyType = null;
            if (tagsConfiguration.getString("Tags." + tagKey + ".economy").equalsIgnoreCase("coins"))
                economyType = EconomyType.COINS;
            if (tagsConfiguration.getString("Tags." + tagKey + ".economy").equalsIgnoreCase("cash"))
                economyType = EconomyType.CASH;
            boolean exclusive = Boolean.parseBoolean(tagsConfiguration.get("Tags." + tagKey + ".exclusive").toString());
            Rarity rarity = CoreTags.getRarityManager().getRarity(tagsConfiguration.get("Tags." + tagKey + ".rarity").toString());
            List<String> description = tagsConfiguration.getStringList("Tags." + tagKey + ".description");
            this.addTag(new Tag(name, format, price, economyType, description, exclusive, rarity));
        }

    }

}
