package com.brewmes.batch;

import com.brewmes.batch.DataOverTime;
import com.brewmes.batch.PrepareData;
import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common.util.Products;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrepareDataTest {
    private static final double DESIRED_SPEED = 200.0;
    private static final int AMOUNT_TO_PRODUCE = 100;
    private static final int PRODUCT_CODE = Products.PILSNER.productType;
    private static final int MAX_MACHINE_SPEED = Products.PILSNER.speedLimit;
    private static final int ACCEPTED_PRODUCTS = 80;
    private static DataOverTime overTime;
    private static TreeMap<LocalDateTime, Double> HUMIDITY_MAP = new TreeMap<>();
    private static TreeMap<LocalDateTime, Double> VIBRATION_MAP = new TreeMap<>();
    private static TreeMap<LocalDateTime, Double> TEMPERATURE_MAP = new TreeMap<>();
    private static TreeMap<LocalDateTime, Integer> TIME_IN_STATES_MAP = new TreeMap<>();

    @BeforeAll
    static void setUp() {
        Batch batch = new Batch();
        batch.setDesiredSpeed(DESIRED_SPEED);
        batch.setAmountToProduce(AMOUNT_TO_PRODUCE);
        batch.setProductType(PRODUCT_CODE);
        batch.setData(new ArrayList<>());

        for (int i = 0; i < 3; i++) {
            MachineData data = new MachineData();
            data.setHumidity(i);
            data.setTemperature(i);
            data.setVibration(i);
            data.setState(i);
            data.setAcceptableProducts(ACCEPTED_PRODUCTS);
            LocalDateTime now = LocalDateTime.now();
            data.setTimestamp(now);
            HUMIDITY_MAP.put(now, Double.valueOf(i));
            TEMPERATURE_MAP.put(now, Double.valueOf(i));
            VIBRATION_MAP.put(now, Double.valueOf(i));
            TIME_IN_STATES_MAP.put(now, i);
            batch.addMachineData(data);
        }

        PrepareData prepareData = new PrepareData() {
            @Override
            public String prepareBatchReportService(String id) {
                return null;
            }
        };

        overTime = prepareData.prepareData(batch);
    }

    @Test
    void calculateOeeTest() {
        double plannedProductionTime = (AMOUNT_TO_PRODUCE / DESIRED_SPEED) * 60.0;
        double idealCycleTime = (1.0 / MAX_MACHINE_SPEED) * 60;
        double expected = ((ACCEPTED_PRODUCTS * idealCycleTime) / plannedProductionTime) * 100;
        double actual = overTime.getOee();

        assertEquals(expected, actual);
    }

    @Test
    void findAvgTest() {
        double expected = ((0.0 + 1.0 + 2.0) / 3.0);
        double actual = overTime.getAvgTemp();

        assertEquals(expected, actual);
    }

    @Test
    void findMaxTest() {
        double expected = 2;
        double actual = overTime.getMaxTemp();

        assertEquals(expected, actual);
    }

    @Test
    void findMinTest() {
        double expected = 0;
        double actual = overTime.getMinTemp();

        assertEquals(expected, actual);
    }

    @Test
    void findMinHumidity() {
        double expected = 0;
        double actual = overTime.getMinHumidity();

        assertEquals(expected, actual);
    }

    @Test
    void findAvgHumidity() {
        double expected = 1;
        double actual = overTime.getAvgHumidity();

        assertEquals(expected, actual);
    }

    @Test
    void findMaxHumidity() {
        double expected = 2;
        double actual = overTime.getMaxHumidity();

        assertEquals(expected, actual);
    }

    @Test
    void findMinVibration() {
        double expected = 0;
        double actual = overTime.getMinVibration();

        assertEquals(expected, actual);
    }

    @Test
    void findAvgVibration() {
        double expected = 1;
        double actual = overTime.getAvgVibration();

        assertEquals(expected, actual);
    }

    @Test
    void findMaxVibration() {
        double expected = 2;
        double actual = overTime.getMaxVibration();

        assertEquals(expected, actual);
    }
}
