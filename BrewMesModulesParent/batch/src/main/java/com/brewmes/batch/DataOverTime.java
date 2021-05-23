package com.brewmes.batch;

import com.brewmes.common.entities.Batch;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DataOverTime {
    private Batch batch;
    private double oee;
    private double avgVibration;
    private double minVibration;
    private double maxVibration;
    private double avgTemp;
    private double minTemp;
    private double maxTemp;
    private double avgHumidity;
    private double minHumidity;
    private double maxHumidity;

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public double getAvgVibration() {
        return round(avgVibration, 2);
    }

    public void setAvgVibration(double avgVibration) {
        this.avgVibration = avgVibration;
    }

    public double getMinVibration() {
        return round(minVibration, 2);
    }

    public void setMinVibration(double minVibration) {
        this.minVibration = minVibration;
    }

    public double getMaxVibration() {
        return round(maxVibration, 2);
    }

    public void setMaxVibration(double maxVibration) {
        this.maxVibration = maxVibration;
    }

    public double getAvgTemp() {
        return round(avgTemp, 2);
    }

    public void setAvgTemp(double avgTemp) {
        this.avgTemp = avgTemp;
    }

    public double getMinTemp() {
        return round(minTemp, 2);
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return round(maxTemp, 2);
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getAvgHumidity() {
        return round(avgHumidity, 2);
    }

    public void setAvgHumidity(double avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public double getMinHumidity() {
        return round(minHumidity, 2);
    }

    public void setMinHumidity(double minHumidity) {
        this.minHumidity = minHumidity;
    }

    public double getMaxHumidity() {
        return round(maxHumidity, 2);
    }

    public void setMaxHumidity(double maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public double getOee() {
        return round(oee, 2);
    }

    public void setOee(double oee) {
        this.oee = oee;
    }
}
