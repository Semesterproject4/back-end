package com.brewmes.api;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.services.IScheduleService;
import com.brewmes.common.util.Products;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    @Mock
    IScheduleService service;

    @InjectMocks
    ScheduleController controller;

    @Test
    void addToQueue() {
        ScheduledBatch scheduledBatch = new ScheduledBatch(100, Products.ALE, 100);
        ScheduledBatch scheduledBatch1 = new ScheduledBatch(100, Products.ALCOHOL_FREE, 1);

        Mockito.when(service.addToQueue(scheduledBatch)).thenReturn(1);
        Mockito.when(service.addToQueue(scheduledBatch)).thenReturn(1);


        ResponseEntity<String> response = controller.addToQueue(scheduledBatch);
        ResponseEntity<String> response1 = controller.addToQueue(scheduledBatch);


        assertEquals(200, response.getStatusCode().value());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void removeFromQueue() {
        Mockito.when(service.removeFromQueue("goodID")).thenReturn(true);
        Mockito.when(service.removeFromQueue("badID")).thenReturn(false);

        ResponseEntity<String> response = controller.removeFromQueue("goodID");
        ResponseEntity<String> response1 = controller.removeFromQueue("badID");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(500, response1.getStatusCode().value());
    }

    @Test
    void getQueue() {
        //Checks the best case scenario where a list is actually returned
        Mockito.when(service.getQueue()).thenReturn(new ArrayList<ScheduledBatch>());
        ResponseEntity<Object> response = controller.getQueue();
        assertEquals(200, response.getStatusCode().value());

        //Checks the worst case scenario where some error has happened in the backend and thereby it has returned null to the controller
        Mockito.when(service.getQueue()).thenReturn(null);
        response = controller.getQueue();
        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void prioritizeUpInQueue() {
        Mockito.when(service.moveUpInQueue("goodID")).thenReturn(true);
        Mockito.when(service.moveUpInQueue("badID")).thenReturn(false);

        ResponseEntity<String> response = controller.prioritizeUpInQueue("goodID");
        ResponseEntity<String> response1 = controller.prioritizeUpInQueue("badID");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(500, response1.getStatusCode().value());
    }

    @Test
    void prioritizeDownInQueue() {
        Mockito.when(service.moveDownInQueue("goodID")).thenReturn(true);
        Mockito.when(service.moveDownInQueue("badID")).thenReturn(false);

        ResponseEntity<String> response = controller.prioritizeDownInQueue("goodID");
        ResponseEntity<String> response1 = controller.prioritizeDownInQueue("badID");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(500, response1.getStatusCode().value());
    }
}