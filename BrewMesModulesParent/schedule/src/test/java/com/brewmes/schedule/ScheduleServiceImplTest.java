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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ScheduleServiceImplTest {

    private static ScheduledBatch scheduledBatch;
    private static ScheduledBatch scheduledBatch2;
    private static List<ScheduledBatch> scheduledBatches;
    private static List<ScheduledBatch> scheduledBatchesRemoved;
    private static List<ScheduledBatch> emptyScheduledBatches;
    private static List<ScheduledBatch> prioList;
    @Mock
    ScheduleRepository scheduleRepository;
    @InjectMocks
    ScheduleServiceImpl scheduleService;

    @BeforeAll
    static void setUp() {
        scheduledBatch = new ScheduledBatch(100, Products.PILSNER, 200);
        scheduledBatch2 = new ScheduledBatch(80, Products.PILSNER, 100);

        scheduledBatches = new ArrayList<>();
        scheduledBatches.add(scheduledBatch);
        scheduledBatches.add(scheduledBatch2);

        scheduledBatchesRemoved = new ArrayList<>();
        scheduledBatchesRemoved.add(scheduledBatch2);

        emptyScheduledBatches = new ArrayList<>();

        prioList = new ArrayList<>();
        prioList.add(scheduledBatch2);
        prioList.add(scheduledBatch);
    }

    @Test
    void addToQueue_success() {
        when(scheduleRepository.insert(scheduledBatch)).thenReturn(scheduledBatch);
        when(scheduleRepository.findAll()).thenReturn(scheduledBatches);

        int expected = scheduleRepository.findAll().indexOf(scheduledBatch);
        int actual = scheduleService.addToQueue(scheduledBatch);

        assertEquals(expected, actual);
    }

    @Test
    void addToQueue_failure() {
        when(scheduleRepository.findAll()).thenReturn(scheduledBatches);

        int expected = scheduleRepository.findAll().indexOf(scheduledBatch2);
        int actual = scheduleService.addToQueue(scheduledBatch);

        assertNotEquals(expected, actual);
    }

    @Test
    void removeFromQueue_success() {
        when(scheduleRepository.existsById("id")).thenReturn(false);

        boolean actual = scheduleService.removeFromQueue("id");

        assertTrue(actual);
    }

    @Test
    void removeFromQueue_failure() {
        when(scheduleRepository.existsById("id")).thenReturn(true);

        boolean actual = scheduleService.removeFromQueue("id");

        assertFalse(actual);
    }

    @Test
    void getQueue_success() {
        when(scheduleRepository.findAll()).thenReturn(scheduledBatches);

        List<ScheduledBatch> actual = scheduleService.getQueue();

        assertEquals(scheduledBatches, actual);
    }

    @Test
    void getQueue_failure() {
        when(scheduleRepository.findAll()).thenReturn(new ArrayList<>());

        List<ScheduledBatch> actual = scheduleService.getQueue();

        assertNotEquals(scheduledBatches, actual);
    }

    @Test
    void takeFirstInQueue_success() {
        when(scheduleRepository.findAll()).thenReturn(scheduledBatches, scheduledBatches, scheduledBatches, emptyScheduledBatches);
        when(scheduleRepository.saveAll(scheduledBatchesRemoved)).thenReturn(scheduledBatchesRemoved);
        doNothing().when(scheduleRepository).deleteAll();

        ScheduledBatch expected = scheduleRepository.findAll().get(0);

        ScheduledBatch actual = scheduleService.takeFirstInQueue();

        assertEquals(expected, actual);
    }

    @Test
    void takeFirstInQueue_another_failure() {
        when(scheduleRepository.findAll()).thenReturn(emptyScheduledBatches);

        ScheduledBatch expectNull = scheduleService.takeFirstInQueue();

        assertNull(expectNull);
    }

    @Test
    void queueIsEmpty_success() {
        when(scheduleRepository.findAll()).thenReturn(emptyScheduledBatches);

        boolean expected = scheduleService.queueIsEmpty();

        assertTrue(expected);
    }

    @Test
    void queueIsEmpty_failure() {
        when(scheduleRepository.findAll()).thenReturn(scheduledBatches);

        boolean expected = scheduleService.queueIsEmpty();

        assertFalse(expected);
    }

    @Test
    void prioritizeQueue_success() {
        when(scheduleRepository.saveAll(prioList)).thenReturn(prioList);
        when(scheduleRepository.findAll()).thenReturn(emptyScheduledBatches);
        doNothing().when(scheduleRepository).deleteAll();

        boolean expected = scheduleService.prioritizeQueue(prioList);

        assertTrue(expected);
    }

    @Test
    void prioritizeQueue_failure() {
        when(scheduleRepository.findAll()).thenReturn(emptyScheduledBatches);
        when(scheduleRepository.saveAll(scheduledBatches)).thenReturn(prioList);
        doNothing().when(scheduleRepository).deleteAll();

        boolean expected = scheduleService.prioritizeQueue(scheduledBatches);

        assertFalse(expected);
    }

    @Test
    void prioritizeQueue_another_failure() {
        when(scheduleRepository.findAll()).thenReturn(prioList);
        doNothing().when(scheduleRepository).deleteAll();

        boolean expected = scheduleService.prioritizeQueue(prioList);

        assertFalse(expected);
    }
}