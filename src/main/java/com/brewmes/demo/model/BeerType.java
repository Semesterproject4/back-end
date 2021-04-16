package com.brewmes.demo.model;

import java.util.HashMap;
import java.util.Map;

public enum BeerType {
    PILSNER(0, 600),
    WHEAT(1, 300),
    IPA(2, 150),
    STOUT(3, 200),
    ALE(4, 100),
    ALCOHOL_FREE(5, 125);

    private static final Map<Integer, BeerType> BY_LABEL = new HashMap<>();

    static {
        for (BeerType e : values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    public final int label;
    public final int maxSpeed;

    BeerType(int label, int maxSpeed) {
        this.label = label;
        this.maxSpeed = maxSpeed;
    }

    public static BeerType valueOfLabel(int label) {
        return BY_LABEL.get(label);
    }
}
