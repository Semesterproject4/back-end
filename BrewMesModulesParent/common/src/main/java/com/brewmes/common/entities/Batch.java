package com.brewmes.common.entities;

import java.util.ArrayList;
import java.util.List;

public class Batch {
    private String id;
    private String connectionID;
    private int productType;
    private int amountToProduce;
    private double oee;
    private double desiredSpeed;
    private List<MachineData> data;


    public Batch(String connectionID, int productType, int amountToProduce, double desiredSpeed) {
        this.data = new ArrayList<>();

        this.connectionID = connectionID;
        this.productType = productType;
        this.amountToProduce = amountToProduce;
        this.desiredSpeed = desiredSpeed;
    }

    public Batch() {
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(String connectionID) {
        this.connectionID = connectionID;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getAmountToProduce() {
        return amountToProduce;
    }

    public void setAmountToProduce(int amountToProduce) {
        this.amountToProduce = amountToProduce;
    }

    public double getOee() {
        return oee;
    }

    public void setOee(double oee) {
        this.oee = oee;
    }

    public double getDesiredSpeed() {
        return desiredSpeed;
    }

    public void setDesiredSpeed(double desiredSpeed) {
        this.desiredSpeed = desiredSpeed;
    }

    public List<MachineData> getData() {
        return data;
    }

    public void setData(List<MachineData> data) {
        this.data = data;
    }

    public void addMachineData(MachineData machineData) {
        data.add(machineData);
    }
}
