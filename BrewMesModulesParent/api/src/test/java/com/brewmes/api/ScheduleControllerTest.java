package com.brewmes.api;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.services.IScheduleService;
import com.brewmes.common.util.Products;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    @Mock
    IScheduleService service;

    @InjectMocks
    ScheduleController controller;

    private final ScheduledBatch[] prioQueue = new ScheduledBatch[3];

    @Test
    void addToQueue() {
        ScheduledBatch scheduledBatch = new ScheduledBatch(100, Products.ALE, 100);
        ScheduledBatch scheduledBatch1 = new ScheduledBatch(100, Products.ALCOHOL_FREE, 1);

        when(service.addToQueue(scheduledBatch)).thenReturn(1);
        when(service.addToQueue(scheduledBatch1)).thenReturn(-1);

        ResponseEntity<String> response = controller.addToQueue(scheduledBatch);
        ResponseEntity<String> response1 = controller.addToQueue(scheduledBatch1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(500, response1.getStatusCode().value());
    }

    @Test
    void removeFromQueue() {
        when(service.removeFromQueue("goodID")).thenReturn(true);
        when(service.removeFromQueue("badID")).thenReturn(false);

        ResponseEntity<String> response = controller.removeFromQueue("goodID");
        ResponseEntity<String> response1 = controller.removeFromQueue("badID");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(500, response1.getStatusCode().value());
    }

    @Test
    void getQueue() {
        //Checks the best case scenario where a list is actually returned
        when(service.getQueue()).thenReturn(new ArrayList<ScheduledBatch>());
        ResponseEntity<Object> response = controller.getQueue();
        assertEquals(200, response.getStatusCode().value());

        //Checks the worst case scenario where some error has happened in the backend and thereby it has returned null to the controller
        when(service.getQueue()).thenReturn(null);
        response = controller.getQueue();
        assertEquals(500, response.getStatusCode().value());
    }


    @Test
    void prioritizeQueue_succes() {
        when(service.prioritizeQueue(Arrays.asList(prioQueue))).thenReturn(true);
        ResponseEntity<Object> responseEntity = controller.prioritizeQueue(prioQueue);

        assertEquals(200, responseEntity.getStatusCode().value());
    }


    @Test
    void prioritizeQueue_failure() {
        when(service.prioritizeQueue(Arrays.asList(prioQueue))).thenReturn(false);
        ResponseEntity<Object> responseEntity = controller.prioritizeQueue(prioQueue);

        assertEquals(500, responseEntity.getStatusCode().value());
    }
}