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

        ResponseEntity<String> response =  controller.addToQueue(new ScheduledBatch(100, Products.ALE, 100));
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
        Mockito.when(service.getQueue()).thenReturn(new ArrayList<ScheduledBatch>());

        ResponseEntity<List<ScheduledBatch>> response = controller.getQueue();

        Mockito.when(service.getQueue()).thenReturn(null);
    }

    @Test
    void prioritizeUpInQueue() {
    }

    @Test
    void prioritizeDownInQueue() {
    }
}