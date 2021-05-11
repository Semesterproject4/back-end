package com.brewmes.common.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "batch")
public class Batch {
    @Id
    private String id;

    @NotNull
    private String connectionID;

    @NotNull
    private int productType;

    @NotNull
    private int amountToProduce;

    @NotNull
    private double desiredSpeed;

    private double oee;
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

    public String getID() {
        return id;
    }

    public void setID(String id) {
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
