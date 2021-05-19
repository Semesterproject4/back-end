package com.brewmes.api;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.services.IBatchGetterService;
import com.brewmes.common.services.IBatchReportService;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BatchControllerTest {
    @Mock
    IBatchGetterService getter;

    @Mock
    IBatchReportService dashboardService;

    @Mock
    IBatchReportService pdfService;

    @InjectMocks
    BatchController controller;

    static File file;

    @BeforeAll
    static void setUp() {
        file = new File("test.pdf");

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @AfterAll
    static void tearDown() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void getBatches() {
        Batch batch = Mockito.mock(Batch.class);
        List<Batch> batches = new ArrayList<>();
        batches.add(batch);

        Mockito.when(getter.getBatches(5, 10)).thenReturn(new PageImpl<Batch>(batches) {
        });

        ResponseEntity<Page<Batch>> responseEntity = controller.getBatches(5, 10);

        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void getBatch() {
        Mockito.when(dashboardService.prepareBatchReportService("id")).thenReturn("text");

        ResponseEntity<String> responseEntity = controller.getBatch("id");

        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void getBatch_wrong_id() {
        Mockito.when(dashboardService.prepareBatchReportService("id")).thenReturn(null);

        ResponseEntity<String> responseEntity = controller.getBatch("id");

        assertEquals(404, responseEntity.getStatusCode().value());
    }

    @Test
    void getBatchPdfReport() {
        Mockito.when(pdfService.prepareBatchReportService("id")).thenReturn("test.pdf");

        ResponseEntity<Object> responseEntity = controller.getBatchPdfReport("id");
        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void getBatchPdfReportBadName() {
        Mockito.when(pdfService.prepareBatchReportService("id")).thenReturn("not_test.pdf");

        ResponseEntity<Object> responseEntity = controller.getBatchPdfReport("id");
        assertEquals(404, responseEntity.getStatusCode().value());
    }

    @Test
    void getStaticBatchVariablesForMachine_success() {
        Mockito.when(getter.getStaticBatchVariables("id")).thenReturn(new JsonObject());

        ResponseEntity<Object> responseEntity = controller.getStaticBatchVariablesForMachine("id");
        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void getStaticBatchVariablesForMachine_failure() {
        Mockito.when(getter.getStaticBatchVariables("id")).thenReturn(null);

        ResponseEntity<Object> responseEntity = controller.getStaticBatchVariablesForMachine("id");
        assertEquals(404, responseEntity.getStatusCode().value());
    }
}