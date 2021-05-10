package com.brewmes.common.util;

import java.util.HashMap;
import java.util.Map;

public enum Products {
    PILSNER(0, 600, "Pilsner"),
    WHEAT(1, 300, "Wheat"),
    IPA(2, 150, "IPA"),
    STOUT(3, 200, "Stout"),
    ALE(4, 100, "Ale"),
    ALCOHOL_FREE(5, 125, "Alcohol free");

    private static final Map<Integer, Products> BY_LABEL = new HashMap<>();

    static {
        for (Products e : values()) {
            BY_LABEL.put(e.productType, e);
        }
    }

    public final int productType;
    public final int speedLimit;
    public final String name;

    Products(int productType, int speedLimit, String name) {
        this.productType = productType;
        this.speedLimit = speedLimit;
        this.name = name;
    }

    public static String getNameByID(int id) {
        return BY_LABEL.get(id).name;
    }
}
