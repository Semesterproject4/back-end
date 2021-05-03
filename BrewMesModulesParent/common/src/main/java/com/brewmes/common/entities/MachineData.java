package com.brewmes.common.entities;

import java.time.LocalDateTime;

public class MachineData {
    private double normSpeed;
    private int state;
    private double temperature;
    private double vibration;
    private double humidity;
    private Ingredients ingredients;
    private int acceptableProducts;
    private int defectProducts;
    private int processed;
    private double maintenance;
    private LocalDateTime timestamp;

    public MachineData(double normSpeed, int state, double temperature, double vibration, double humidity, Ingredients ingredients, int acceptableProducts, int defectProducts, int processed, double maintenance, LocalDateTime timestamp) {
        this.normSpeed = normSpeed;
        this.state = state;
        this.temperature = temperature;
        this.vibration = vibration;
        this.humidity = humidity;
        this.ingredients = ingredients;
        this.acceptableProducts = acceptableProducts;
        this.defectProducts = defectProducts;
        this.processed = processed;
        this.maintenance = maintenance;
        this.timestamp = timestamp;
    }

    public double getNormSpeed() {
        return normSpeed;
    }

    public void setNormSpeed(double normSpeed) {
        this.normSpeed = normSpeed;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getVibration() {
        return vibration;
    }

    public void setVibration(double vibration) {
        this.vibration = vibration;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public Ingredients getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients;
    }

    public int getAcceptableProducts() {
        return acceptableProducts;
    }

    public void setAcceptableProducts(int acceptableProducts) {
        this.acceptableProducts = acceptableProducts;
    }

    public int getDefectProducts() {
        return defectProducts;
    }

    public void setDefectProducts(int defectProducts) {
        this.defectProducts = defectProducts;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    public double getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(double maintenance) {
        this.maintenance = maintenance;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
