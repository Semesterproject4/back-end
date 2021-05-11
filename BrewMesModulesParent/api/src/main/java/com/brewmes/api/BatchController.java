package com.brewmes.api;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.services.IBatchGetterService;
import com.brewmes.common.services.IBatchReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/batches")
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
    public ResponseEntity<Object> getBatchPdfReport(@PathVariable("id") String id) {
        if (getter.containsId(id)) {
            String fileName = pdfService.prepareBatchReportService(id); //NOSONAR
            File file = new File(fileName); //NOSONAR
            InputStreamResource resource = null;

            try {
                resource = new InputStreamResource(new FileInputStream(fileName));

                HttpHeaders headers = new HttpHeaders(); //NOSONAR

                headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/pdf")).body(resource);
            } catch (FileNotFoundException e) {
                return new ResponseEntity<>("Error: File not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("No batch found with that id", HttpStatus.NOT_FOUND);
        }
    }
}
