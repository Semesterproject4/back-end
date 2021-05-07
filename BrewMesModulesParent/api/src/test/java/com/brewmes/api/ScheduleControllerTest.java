package com.brewmes.api;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.util.Products;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    @Mock
    IScheduleService service;

    @InjectMocks
    ScheduleController controller;

    @Test
    void addToQueue() {
        Mockito.when(service.addToQueue(new ScheduledBatch(100, Products.ALE, 100))).thenReturn(1);
        Mockito.when(service.addToqueue(new ScheduledBatch(50, Products.ALCOHOL_FREE, 10))).thenReturn(-1);

        ResponseEntity<String> response = controller.addToQueue(new ScheduledBatch(100, Products.ALE, 100));
        ResponseEntity<String> response1 = controller.addToQueue(new ScheduledBatch(50, Products.ALCOHOL_FREE, 10));

        assertEquals(200, response.getStatusCode().value());
        assertEquals(500, response1.getStatusCode().value());
    }

    @Test
    void removeFromQueue() {
        Mockito.when(service.removeFromQueue("goodid")).thenReturn(true);
        Mockito.when(service.removeFromQueue("badid")).thenReturn(false);

        assertEquals(true, controller.removeFromQueue("goodid"));
        assertEquals(false, controller.removeFromQueue("badid"));
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
        Mockito.when(service.prioritizeUpInQueue("goodID")).thenReturn(true);
        Mockito.when(service.prioritizeUpInQueue("badID")).thenReturn(false);

        ResponseEntity<String> response = controller.prioritizeUpInQueue("goodID");
        ResponseEntity<String> response1 = controller.prioritizeUpInQueue("badID");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(500, response1.getStatusCode().value());
    }

    @Test
    void prioritizeDownInQueue() {
        Mockito.when(service.prioritizeDownInQueue("goodID")).thenReturn(true);
        Mockito.when(service.prioritizeDownInQueue("badID")).thenReturn(false);

        ResponseEntity<String> response = controller.prioritizeDownInQueue("goodID");
        ResponseEntity<String> response1 = controller.prioritizeDownInQueue("badID");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(500, response1.getStatusCode().value());
    }
}