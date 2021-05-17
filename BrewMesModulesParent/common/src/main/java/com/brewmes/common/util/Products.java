package com.brewmes.common.util;

import java.util.HashMap;
import java.util.Map;

public enum Products {
    PILSNER(0, 600, 439.64, "Pilsner"),
    WHEAT(1, 300, 127.74, "Wheat"),
    IPA(2, 150, 92.32, "IPA"),
    STOUT(3, 200, 200, "Stout"),
    ALE(4, 100, 92.75, "Ale"),
    ALCOHOL_FREE(5, 125, 64.75, "Alcohol free");

    private static final Map<Integer, Products> BY_LABEL = new HashMap<>();

    static {
        for (Products e : values()) {
            BY_LABEL.put(e.productType, e);
        }
    }

    public final int productType;
    public final int speedLimit;
    public final double optimalSpeed;
    public final String productName;

    Products(int productType, int speedLimit, double optimalSpeed, String name) {
        this.productType = productType;
        this.speedLimit = speedLimit;
        this.optimalSpeed = optimalSpeed;
        this.productName = name;
    }

    public static String getNameByID(int id) {
        return BY_LABEL.get(id).productName;
    }
}
