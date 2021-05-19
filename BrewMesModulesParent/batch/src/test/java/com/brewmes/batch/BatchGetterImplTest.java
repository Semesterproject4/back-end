package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.util.Products;
import com.brewmes.common_repository.BatchRepository;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BatchGetterImplTest {
    private static List<Batch> expected = new ArrayList<>();
    private static List<Batch> unexpected = new ArrayList<>();
    @Mock
    BatchRepository batchRepository;
    @InjectMocks
    BatchGetterImpl batchGetterService;

    static String id;

    @BeforeAll
    static void setUp() {
        Batch batch = new Batch("connectionID", Products.PILSNER, 20, 100);
        Batch batch2 = new Batch("connectionID2", Products.WHEAT, 200, 20);
        id = "123";
        batch.setID(id);
        batch2.setID("1234");

        expected.add(batch);
        expected.add(batch2);
        unexpected.add(batch2);

    }

    @Test
    void getBatches() {
        Page<Batch> expectedPage = new PageImpl<>(expected);
        Mockito.when(batchRepository.findAll(PageRequest.of(0, 2))).thenReturn(expectedPage);

        Page<Batch> batches = batchGetterService.getBatches(0, 2);

        assertEquals(expectedPage, batches);
    }

    @Test
    void getStaticBatchVariables_success() {
        Mockito.when(batchRepository.findAll()).thenReturn(expected);
        Batch batch = batchGetterService.getStaticBatchVariables("connectionID");

        assertEquals(id, batch.getID());
        assertEquals(20, batch.getAmountToProduce());
        assertEquals("Pilsner", batch.getProductType().productName);
        assertEquals(100, batch.getDesiredSpeed());
    }

    @Test
    void getStaticBatchVariables_failure() {
        Mockito.when(batchRepository.findAll()).thenReturn(unexpected);

        assertNull(batchGetterService.getStaticBatchVariables("connectionID"));
    }
}