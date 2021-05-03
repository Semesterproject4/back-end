package com.brewmes.common.util;

public enum Products {
    PILSNER("Pilsner", 600),
    WHEAT("Wheat", 300),
    IPA("IPA", 150),
    STOUT("Stout", 200),
    ALE("Ale", 100),
    ALCOHOL_FREE("Alcohol Free", 125);

    public final String productType;
    public final int speedLimit;

    Products(String productType, int speedLimit) {
        this.productType = productType;
        this.speedLimit = speedLimit;
    }
}
