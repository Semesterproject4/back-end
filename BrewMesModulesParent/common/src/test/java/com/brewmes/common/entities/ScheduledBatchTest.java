package com.brewmes.common.entities;

import com.brewmes.common.util.Products;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduledBatchTest {
    ScheduledBatch batch;
    ScheduledBatch batchWithId;

    @BeforeEach
    void setUp() {
        this.batch = new ScheduledBatch(1, Products.ALCOHOL_FREE, 200);
        this.batchWithId = new ScheduledBatch("1", 1, Products.ALCOHOL_FREE, 200);
    }

    @Test
    void getId() {
        assertEquals("1", batchWithId.getId());
    }

    @Test
    void setId() {
        String newId = "2";
        batchWithId.setId(newId);

        assertEquals(newId, batchWithId.getId());
    }

    @Test
    void getSpeed() {
        assertEquals(1,batchWithId.getSpeed());
    }

    @Test
    void setSpeed() {
        int newSpeed = 200;
        batch.setSpeed(newSpeed);

        assertEquals(newSpeed, batch.getSpeed());
    }

    @Test
    void getType() {
        assertEquals(Products.ALCOHOL_FREE, batch.getType());
    }

    @Test
    void setType() {
        Products product = Products.ALE;
        batch.setType(product);

        assertEquals(Products.ALE, batch.getType());
    }

    @Test
    void getAmount() {
        assertEquals(200, batch.getAmount());
    }

    @Test
    void setAmount() {
        int newAmount = 20000;
        batch.setAmount(newAmount);

        assertEquals(newAmount, batch.getAmount());
    }
}