package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common_repository.BatchRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BatchGetterImplTest {
    @Mock
    BatchRepository batchRepository;

    @InjectMocks
    BatchGetterImpl batchGetterService;

    private static List<Batch> expected = new ArrayList<>();

    @BeforeAll
    static void setUp() {
        Batch batch = new Batch("connectionID", 0, 20,100);
        Batch batch2 = new Batch("connectionID2", 1,200, 20);

        expected.add(batch);
        expected.add(batch2);
    }

    @Test
    void getBatches() {
        Mockito.when(batchRepository.findAll(PageRequest.of(0,2))).thenReturn(new PageImpl<>(expected));

        List<Batch> batches = batchGetterService.getBatches(0, 2);

        assertEquals(expected, batches);
    }

    @Test
    void containsId_correctID() {
        Mockito.when(batchRepository.existsById("testID")).thenReturn(true);

        assertTrue(batchGetterService.containsID("testID"));
    }

    @Test
    void containsId_incorrectID() {
        Mockito.when(batchRepository.existsById("testID")).thenReturn(false);

        assertFalse(batchGetterService.containsID("testID"));
    }
}