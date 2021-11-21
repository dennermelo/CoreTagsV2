package com.github.dennermelo.core.tags.enums;

import com.github.dennermelo.core.tags.setting.Settings;

public enum EconomyType {

    CASH(Settings.ECONOMY_CASH_FORMAT.asString().replace("&", "§")),
    COINS(Settings.ECONOMY_COINS_FORMAT.asString().replace("&", "§"));

    private final String name;

    private EconomyType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
