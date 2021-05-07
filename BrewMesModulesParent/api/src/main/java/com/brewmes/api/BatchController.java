package com.brewmes.api;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.services.IBatchGetterService;
import com.brewmes.common.services.IBatchReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired(required = false)
    @Qualifier("dashboard")
    IBatchReportService dashboardService;

    @Autowired(required = false)
    @Qualifier("pdf")
    IBatchReportService pdfService;

    @Autowired
    IBatchGetterService getter;

    /**
     * Gets a paged view of the database
     * @param page The page that is wanted
     * @param size The size of the page that is wanted
     * @return The {@code ResponseEntity} with the {@code Batches} in a paginated format
     */
    @GetMapping
    public ResponseEntity<List<Batch>> getBatches(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(getter.getBatches(page, size), HttpStatus.OK);
    }

    /**
     * Gets the object for the Dashboard Batch report
     * @param id The {@code ID} of the {@code Batch}
     * @return The {@code ResponseEntity} containing the data needed for the Dashboard Batch view
     */
    @GetMapping(value = "/{id}/dashboard")
    public ResponseEntity<String> getBatch(@PathVariable("id") String id) {
        if (getter.containsId(id)) {
            return new ResponseEntity<>(dashboardService.prepareBatchReportService(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No batch found with that id", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Starts the generation of the PDF report and gets the path for the PDF report
     * @param id The {@code ID} of the {@code Batch}
     * @return The {@code ResponseEntity} containing the path for the PDF report download
     */
    @GetMapping(value = "/{id}/pdf")
    public ResponseEntity<String> getBatchPdfReport(@PathVariable("id") String id) {
        if (getter.containsId(id)) {
            return new ResponseEntity<>(pdfService.prepareBatchReportService(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No batch found with that id", HttpStatus.NOT_FOUND);
        }
    }
}
