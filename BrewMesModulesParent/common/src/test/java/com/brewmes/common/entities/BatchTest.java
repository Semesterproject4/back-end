package com.brewmes.common.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BatchTest {

    private Batch batch;
    private Batch batchNoArg;


    @BeforeEach
    void setUp() {
        this.batch = new Batch("1", 1, 100, 100.0);
        this.batchNoArg = new Batch();
    }

    void testNoArg() {
        assertNotNull(batchNoArg);
    }

    @Test
    void getID() {
        this.batch.setID("1");
        String id = this.batch.getID();
        assertEquals("1", id);
    }

    @Test
    void setId() {
        String id = "2";
        this.batch.setID("2");
        assertEquals(id, this.batch.getID());
    }

    @Test
    void getConnectionID() {
        String connectionID = this.batch.getConnectionID();
        assertEquals("1", connectionID);
    }

    @Test
    void setConnectionID() {
        String connectionID = "2";
        this.batch.setConnectionID(connectionID);
        assertEquals(connectionID, this.batch.getConnectionID());
    }

    @Test
    void getProductType() {
        int productType = this.batch.getProductType();
        assertEquals(1, productType);
    }

    @Test
    void setProductType() {
        int productType = 2;
        this.batch.setProductType(productType);
        assertEquals(productType, this.batch.getProductType());
    }

    @Test
    void getAmountToProduce() {
        int atp = this.batch.getAmountToProduce();
        assertEquals(100, atp);
    }

    @Test
    void setAmountToProduce() {
        int atp = 50;
        this.batch.setAmountToProduce(atp);
        assertEquals(atp, this.batch.getAmountToProduce());
    }

    @Test
    void getOee() {
        this.batch.setOee(1.0);
        double oee = this.batch.getOee();
        assertEquals(1, oee);
    }

    @Test
    void setOee() {
        double oee = 10.0;
        this.batch.setOee(oee);
        assertEquals(oee, this.batch.getOee());
    }

    @Test
    void getDesiredSpeed() {
        double ds = this.batch.getDesiredSpeed();
        assertEquals(100.0, ds);
    }

    @Test
    void setDesiredSpeed() {
        double ds = 150.0;
        this.batch.setDesiredSpeed(ds);
        assertEquals(ds, this.batch.getDesiredSpeed());

    }

    @Test
    void getData() {
        assertNotNull(this.batch.getData());
    }

    @Test
    void setData() {
        List<MachineData> data = new ArrayList<>();
        this.batch.setData(data);
        assertEquals(data, this.batch.getData());
    }

    @Test
    void addMachineData() {
        MachineData data = new MachineData();
        this.batch.addMachineData(data);
        assertEquals(1, this.batch.getData().size());
    }
}
