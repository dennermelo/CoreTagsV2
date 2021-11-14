package com.github.dennermelo.core.tags.manager;

import com.github.dennermelo.core.tags.CoreTags;
import com.github.dennermelo.core.tags.model.Rarity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RarityManager {

    private final List<Rarity> rarities;

    public RarityManager() {
        this.rarities = new ArrayList<>();
    }

    public void addRarity(Rarity rarity) {
        this.rarities.add(rarity);
    }

    public Rarity getRarity(String name) {
        for (Rarity rarity : this.rarities) {
            if (rarity.getName().equalsIgnoreCase(name)) {
                return rarity;
            }
        }
        return null;
    }

    public List<Rarity> getRarities() {
        return this.rarities;
    }

    public void load(CoreTags instance) {
        File file = new File(instance.getDataFolder(), "rarities.yml");
        if (!file.exists()) {
            instance.saveResource("rarities.yml", false);
        }
        FileConfiguration raritiesFile = YamlConfiguration.loadConfiguration(file);

        for (String rarityKey : raritiesFile.getConfigurationSection("Rarities").getKeys(false)) {
            String rarityName = raritiesFile.getString("Rarities." + rarityKey + ".name");
            String rarityFormat = raritiesFile.getString("Rarities." + rarityKey + ".format");
            String[] icon = raritiesFile.getString("Rarities." + rarityKey + ".icon").split(":");
            ItemStack itemStack = new ItemStack(Integer.parseInt(icon[0]), 1, Short.parseShort(icon[1]));
            this.addRarity(new Rarity(rarityName, rarityFormat, itemStack));
        }
    }
}
