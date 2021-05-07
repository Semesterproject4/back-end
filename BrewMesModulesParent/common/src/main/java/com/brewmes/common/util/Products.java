package com.brewmes.common.util;

public enum Products {
    PILSNER(0, 600),
    WHEAT(1, 300),
    IPA(2, 150),
    STOUT(3, 200),
    ALE(4, 100),
    ALCOHOL_FREE(5, 125);

    public final int productType;
    public final int speedLimit;

    Products(int productType, int speedLimit) {
        this.productType = productType;
        this.speedLimit = speedLimit;
    }
}
