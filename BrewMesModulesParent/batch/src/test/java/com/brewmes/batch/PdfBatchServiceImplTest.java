package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.Ingredients;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common_repository.BatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdfBatchServiceImplTest {


    @Mock
    BatchRepository batchRepo;

    @InjectMocks
    PdfBatchServiceImpl batchService;


    private static final String ID = "1";

    private Batch batch;
    private MachineData data;
    private File file;

    @BeforeEach
    void setUp() {

        batch = new Batch("connection id :)", 3, 50, 75.0);
        data = new MachineData(10.0, 2, 27.0, 1.0, 3.2, new Ingredients(100, 100, 100, 100, 100), 2, 0, 2, 50, LocalDateTime.now());
        batch.addMachineData(data);

    }
    @Test
    public void PreparePdfReportSucces() {

    }

    @Test
    public void PreparePdfReportFail() {

        when(batchRepo.findById(ID)).thenReturn(Optional.empty());

        batchService.prepareBatchReportService(ID);
        File somefile = new File();


    }

}