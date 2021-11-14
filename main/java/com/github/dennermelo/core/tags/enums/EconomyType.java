package com.github.dennermelo.core.tags.enums;

public enum EconomyType {

    CASH("cash"),
    COINS("coins");

    private final String name;

    EconomyType(String economy) {
        this.name = economy;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}




