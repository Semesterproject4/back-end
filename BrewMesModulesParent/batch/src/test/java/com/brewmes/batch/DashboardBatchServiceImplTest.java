package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common.util.Products;
import com.brewmes.common_repository.BatchRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardBatchServiceImplTest {
    private static final String BATCH_ID = "1";
    private static final int ACCEPTED_PRODUCTS = 80;
    @Mock
    BatchRepository batchRepo;
    @InjectMocks
    DashboardBatchServiceImpl dashboardService;
    private Gson gson;
    private Batch batch;
    private PrepareData prepare;

    @BeforeEach
    void setUp() {
        this.gson = new Gson().newBuilder().serializeSpecialFloatingPointValues().create();
        this.batch = new Batch();
        batch.setID(BATCH_ID);
        batch.setData(new ArrayList<>());
        for (int i = 0; i < 3; i++) {
            MachineData data = new MachineData();
            data.setTimestamp(LocalDateTime.now());
            data.setHumidity(i);
            data.setTemperature(i);
            data.setVibration(i);
            data.setAcceptableProducts(ACCEPTED_PRODUCTS);
            batch.addMachineData(data);
            batch.setProductType(Products.WHEAT);
        }
        this.prepare = new PrepareData() {
            @Override
            public String prepareBatchReportService(String id) {
                return null;
            }
        };
    }

    @Test
    void prepareBatchReportService_success() {
        when(batchRepo.findById(BATCH_ID))
                .thenReturn(Optional.of(batch));

        String actual = dashboardService.prepareBatchReportService(BATCH_ID);
        String expected = gson.toJson(this.prepare.prepareData(batch));

        assertEquals(expected, actual);
    }

    @Test
    void prepareBatchReportService_fail() {
        when(batchRepo.findById(BATCH_ID))
                .thenReturn(Optional.empty());

        assertNull(dashboardService.prepareBatchReportService(BATCH_ID));

    }
}