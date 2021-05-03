package com.brewmes.common.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MachineDataTest {

    MachineData machineData;
    MachineData machineDataNoArg;

    @BeforeEach
    void setUp() {
        machineData = new MachineData(50.0, 2, 2.0, 3.0, 4.0,
                new Ingredients(100.0, 200.0, 300.0, 400.0, 500.0),
                25, 10, 35, 20.0, LocalDateTime.of(1995, 5, 2, 3, 25));
        machineDataNoArg = new MachineData();
    }

    @Test
    void getNormSpeed() {
        assertEquals(50.0, machineData.getNormSpeed());
    }

    @Test
    void setNormSpeed() {
        machineData.setNormSpeed(100.0);
        assertEquals(100.0, machineData.getNormSpeed());
    }

    @Test
    void getState() {
        assertEquals(2, machineData.getState());
    }

    @Test
    void setState() {
        machineData.setState(17);
        assertEquals(17, machineData.getState());
    }

    @Test
    void getTemperature() {
        assertEquals(2.0, machineData.getTemperature());
    }

    @Test
    void setTemperature() {
        machineData.setTemperature(12.62);
        assertEquals(12.62, machineData.getTemperature());
    }

    @Test
    void getVibration() {
        assertEquals(3.0, machineData.getVibration());
    }

    @Test
    void setVibration() {
        machineData.setVibration(15.0);
        assertEquals(15.0, machineData.getVibration());
    }

    @Test
    void getHumidity() {
        assertEquals(4.0, machineData.getHumidity());
    }

    @Test
    void setHumidity() {
        machineData.setHumidity(420.69);
        assertEquals(420.69, machineData.getHumidity());
    }

    @Test
    void getIngredients() {
        assertEquals(100, machineData.getIngredients().getBarley());
        assertEquals(200, machineData.getIngredients().getHops());
        assertEquals(300, machineData.getIngredients().getMalt());
        assertEquals(400, machineData.getIngredients().getWheat());
        assertEquals(500, machineData.getIngredients().getYeast());
    }

    @Test
    void setIngredients() {
        machineData.setIngredients(new Ingredients(200, 300, 400, 500, 600));
        assertEquals(200, machineData.getIngredients().getBarley());
        assertEquals(300, machineData.getIngredients().getHops());
        assertEquals(400, machineData.getIngredients().getMalt());
        assertEquals(500, machineData.getIngredients().getWheat());
        assertEquals(600, machineData.getIngredients().getYeast());
    }

    @Test
    void getAcceptableProducts() {
        assertEquals(25, machineData.getAcceptableProducts());
    }

    @Test
    void setAcceptableProducts() {
        machineData.setAcceptableProducts(12);
        assertEquals(12, machineData.getAcceptableProducts());
    }

    @Test
    void getDefectProducts() {
        assertEquals(10, machineData.getDefectProducts());
    }

    @Test
    void setDefectProducts() {
        machineData.setDefectProducts(24);
        assertEquals(24, machineData.getDefectProducts());
    }

    @Test
    void getProcessed() {
        assertEquals(35, machineData.getProcessed());
    }

    @Test
    void setProcessed() {
        machineData.setProcessed(32);
        assertEquals(32, machineData.getProcessed());
    }

    @Test
    void getMaintenance() {
        assertEquals(20.0, machineData.getMaintenance());
    }

    @Test
    void setMaintenance() {
        machineData.setMaintenance(4.42);
        assertEquals(4.42, machineData.getMaintenance());
    }

    @Test
    void getTimestamp() {
        assertEquals(LocalDateTime.of(1995, 5, 2, 3, 25), machineData.getTimestamp());
    }

    @Test
    void setTimestamp() {
        machineData.setTimestamp(LocalDateTime.of(1998, 3, 7, 9, 5));
        assertEquals(LocalDateTime.of(1998, 3, 7, 9, 5), machineData.getTimestamp());
    }

    @Test
    void getNullValue() {
        assertEquals(0, machineDataNoArg.getState());
        assertEquals(0.0, machineDataNoArg.getMaintenance());
    }
}