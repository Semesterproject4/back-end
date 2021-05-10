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
    private TreeMap<LocalDateTime, Double> sortedHumidity = new TreeMap<>();
    private TreeMap<LocalDateTime, Double> sortedVibration = new TreeMap<>();
    private TreeMap<LocalDateTime, Double> sortedTemperature = new TreeMap<>();
    private TreeMap<LocalDateTime, Integer> sortedTimeInStates = new TreeMap<>();


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

    public TreeMap<LocalDateTime, Double> getSortedHumidity() {
        return sortedHumidity;
    }

    public void setSortedHumidity(TreeMap<LocalDateTime, Double> sortedHumidity) {
        this.sortedHumidity = sortedHumidity;
    }

    public TreeMap<LocalDateTime, Double> getSortedVibration() {
        return sortedVibration;
    }

    public void setSortedVibration(TreeMap<LocalDateTime, Double> sortedVibration) {
        this.sortedVibration = sortedVibration;
    }

    public TreeMap<LocalDateTime, Double> getSortedTemperature() {
        return sortedTemperature;
    }

    public void setSortedTemperature(TreeMap<LocalDateTime, Double> sortedTemperature) {
        this.sortedTemperature = sortedTemperature;
    }

    public TreeMap<LocalDateTime, Integer> getSortedTimeInStates() {
        return sortedTimeInStates;
    }

    public void setSortedTimeInStates(TreeMap<LocalDateTime, Integer> sortedTimeInStates) {
        this.sortedTimeInStates = sortedTimeInStates;
    }
}
