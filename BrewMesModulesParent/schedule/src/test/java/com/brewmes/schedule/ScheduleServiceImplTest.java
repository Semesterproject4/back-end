package com.brewmes.schedule;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.util.Products;
import com.brewmes.common_repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock
    ScheduleRepository scheduleRepository;

    @InjectMocks
    ScheduleServiceImpl scheduleService;

    private static ScheduledBatch scheduledBatch;

    @BeforeAll
    static void setUp() {
        scheduledBatch = new ScheduledBatch(100, Products.PILSNER, 200);
    }

    @Test
    void addToQueue_success() {
        when(scheduleRepository.findAll().indexOf(scheduledBatch)).thenReturn(1);

        int actual = scheduleService.addToQueue(scheduledBatch);

        assertEquals(1, actual);
    }

    @Test
    void removeFromQueue() {
    }

    @Test
    void getQueue() {
    }

    @Test
    void getFirstInQueue() {
    }

    @Test
    void queueIsEmpty() {
    }

    @Test
    void prioritizeQueue() {
    }
}