package com.brewmes.common.entities;

import com.brewmes.common.util.Products;

import javax.validation.constraints.NotNull;

public class ScheduledBatch {
    private String id;

    @NotNull
    private Integer speed;

    @NotNull
    private Products type;

    @NotNull
    private Integer amount;

    public ScheduledBatch(int speed, Products type, int amount) {
        this.speed = speed;
        this.type = type;
        this.amount = amount;
    }

    public ScheduledBatch(String id, int speed, Products type, int amount) {
        this.id = id;
        this.speed = speed;
        this.type = type;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Products getType() {
        return type;
    }

    public void setType(Products type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
