package com.brewmes.api;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.services.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/scheduled-batches")
@CrossOrigin(origins = "http://localhost:3000")
public class ScheduleController {

    @Autowired
    private IScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<String> addToQueue(@Valid @RequestBody ScheduledBatch scheduledBatch) {
        if (scheduleService.addToQueue(scheduledBatch)) {
            return new ResponseEntity<>("Scheduled Batch added to queue", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Scheduled Batch not added to queue", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeFromQueue(@PathVariable String id) {
        boolean success = scheduleService.removeFromQueue(id);

        if (success) {
            return new ResponseEntity<>("Scheduled Batch removed from queue", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: Scheduled Batch removal failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getQueue() {
        List<ScheduledBatch> list = scheduleService.getQueue();

        if (list != null) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
  
    @PatchMapping("/prioritizeQueue")
    public ResponseEntity<Object> prioritizeQueue(@RequestBody @Valid ScheduledBatch[] prioritizedList) {
        boolean success = scheduleService.prioritizeQueue(Arrays.asList(prioritizedList));

        if (success) {
            return new ResponseEntity<>("Queue has been updated based on the prioritization", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: Queue has not been prioritized", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
