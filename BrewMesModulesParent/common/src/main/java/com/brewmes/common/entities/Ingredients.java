package com.brewmes.common.entities;

public class Ingredients {
    private double barley;
    private double hops;
    private double malt;
    private double wheat;
    private double yeast;

    public Ingredients(double barley, double hops, double malt, double wheat, double yeast) {
        this.barley = barley;
        this.hops = hops;
        this.malt = malt;
        this.wheat = wheat;
        this.yeast = yeast;
    }

    public double getBarley() {
        return barley;
    }

    public void setBarley(double barley) {
        this.barley = barley;
    }

    public double getHops() {
        return hops;
    }

    public void setHops(double hops) {
        this.hops = hops;
    }

    public double getMalt() {
        return malt;
    }

    public void setMalt(double malt) {
        this.malt = malt;
    }

    public double getWheat() {
        return wheat;
    }

    public void setWheat(double wheat) {
        this.wheat = wheat;
    }

    public double getYeast() {
        return yeast;
    }

    public void setYeast(double yeast) {
        this.yeast = yeast;
    }
}
