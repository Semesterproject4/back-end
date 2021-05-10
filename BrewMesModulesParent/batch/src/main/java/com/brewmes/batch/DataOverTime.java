package com.brewmes.batch;

import com.brewmes.common.entities.Batch;

import java.time.LocalDateTime;
import java.util.TreeMap;

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

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public double getAvgVibration() {
        return avgVibration;
    }

    public void setAvgVibration(double avgVibration) {
        this.avgVibration = avgVibration;
    }

    public double getMinVibration() {
        return minVibration;
    }

    public void setMinVibration(double minVibration) {
        this.minVibration = minVibration;
    }

    public double getMaxVibration() {
        return maxVibration;
    }

    public void setMaxVibration(double maxVibration) {
        this.maxVibration = maxVibration;
    }

    public double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(double avgTemp) {
        this.avgTemp = avgTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(double avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public double getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(double minHumidity) {
        this.minHumidity = minHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(double maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public double getOee() {
        return oee;
    }

    public void setOee(double oee) {
        this.oee = oee;
    }
}
