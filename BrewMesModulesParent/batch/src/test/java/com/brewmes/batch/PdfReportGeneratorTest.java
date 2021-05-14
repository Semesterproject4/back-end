package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.Ingredients;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common.util.MachineState;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PdfReportGeneratorTest {

    static DataOverTime dataOverTime;
    static Batch batch;
    static MachineData data;

    @Test
    void generatePdf() {
        dataOverTime = new DataOverTime();
        batch = new Batch("connection id :)", 3, 50, 75.0);
        data = new MachineData(10.0, MachineState.STOPPED, 27.0, 1.0, 3.2, new Ingredients(100, 100, 100, 100, 100), 2, 0, 2, 50, LocalDateTime.now());
        batch.addMachineData(data);
        dataOverTime.setBatch(batch);

        File file = new File("batch_report.pdf");
        file.delete();

        PdfReportGenerator.generatePdf(dataOverTime, "batch_report.pdf");

        assertTrue(file.exists());
    }
}