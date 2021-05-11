package com.brewmes.api;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.services.IBatchGetterService;
import com.brewmes.common.services.IBatchReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
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

    @Test
    void getBatches() {
        Mockito.when(getter.getBatches(5, 10)).thenReturn(new ArrayList<Batch>());

        ResponseEntity<List<Batch>> responseEntity = controller.getBatches(5, 10);

        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void getBatch() {
        Mockito.when(getter.containsID("goodID")).thenReturn(true);
        Mockito.when(getter.containsID("badID")).thenReturn(false);
        Mockito.when(dashboardService.prepareBatchReportService("goodID")).thenReturn("whatever");

        ResponseEntity<String> responseEntity = controller.getBatch("goodID");
        assertEquals(200, responseEntity.getStatusCode().value());

        ResponseEntity<String> responseEntity1 = controller.getBatch("badID");
        assertEquals(404, responseEntity1.getStatusCode().value());
    }

    @Test
    void getBatchPdfReport() {
        Mockito.when(getter.containsID("goodID")).thenReturn(true);
        Mockito.when(getter.containsID("badID")).thenReturn(false);
        Mockito.when(pdfService.prepareBatchReportService("goodID")).thenReturn("batch_report.pdf");

        ResponseEntity<Object> responseEntity = controller.getBatchPdfReport("goodID");
        assertEquals(200, responseEntity.getStatusCode().value());

        ResponseEntity<Object> responseEntity1 = controller.getBatchPdfReport("badID");
        assertEquals(404, responseEntity1.getStatusCode().value());
    }

    @Test
    void getBatchPdfReportBadName() {
        Mockito.when(getter.containsID("goodID")).thenReturn(true);
        Mockito.when(pdfService.prepareBatchReportService("goodID")).thenReturn("not_batch_report.pdf");

        ResponseEntity<Object> responseEntity = controller.getBatchPdfReport("goodID");
        assertEquals(404, responseEntity.getStatusCode().value());
    }
}