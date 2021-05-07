package com.brewmes.api;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.services.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/schedule")
public class ScheduleController {

    @Autowired(required = false)
    IScheduleService scheduleService;

    @PostMapping("/addToQueue")
    public ResponseEntity<String> addToQueue(@Valid @ModelAttribute("scheduledBatch") ScheduledBatch scheduledBatch) {
        int index = scheduleService.addToQueue(scheduledBatch);

        if (index != -1) {
            return new ResponseEntity<>("Scheduled Batch added to queue", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Scheduled Batch not added to queue", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/removeFromQueue")
    public ResponseEntity<String> removeFromQueue(@RequestBody String id) {
        boolean success = scheduleService.removeFromQueue(id);

        if (success) {
            return new ResponseEntity<>("Scheduled Batch removed from queue", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: Scheduled Batch removal failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/queue")
    public ResponseEntity<Object> getQueue() {
        List<ScheduledBatch> list = scheduleService.getQueue();

        if(list != null) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Error: something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/prioUp")
    public ResponseEntity<String> prioritizeUpInQueue(@PathVariable String id) {
        boolean success = scheduleService.moveUpInQueue(id);

        if (success) {
            return new ResponseEntity<>("Batch was moved up in the queue", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: Batch was not reprioritized", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/prioDown")
    public ResponseEntity<String> prioritizeDownInQueue(@PathVariable String id) {
        boolean success = scheduleService.moveDownInQueue(id);

        if (success) {
            return new ResponseEntity<>("Batch was moved down in the queue", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: Batch was not reprioritized", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
