package com.github.dennermelo.core.tags.model;

import com.github.dennermelo.core.tags.enums.EconomyType;
import com.github.dennermelo.core.tags.model.Rarity;

import java.util.List;

public class Tag {

    private final String name, format;
    private final double price;
    private final EconomyType economyType;
    private final List<String> description;
    private final boolean exclusive;
    private final Rarity rarity;

    public Tag(String name, String format, double price, EconomyType economyType, List<String> description, boolean exclusive, Rarity rarity) {
        this.name = name;
        this.format = format;
        this.price = price;
        this.economyType = economyType;
        this.description = description;
        this.exclusive = exclusive;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public double getPrice() {
        return price;
    }

    public EconomyType getEconomyType() {
        return economyType;
    }

    public List<String> getDescription() {
        return description;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public Rarity getRarity() {
        return rarity;
    }


}
