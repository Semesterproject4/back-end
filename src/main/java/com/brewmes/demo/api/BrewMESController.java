package com.brewmes.demo.api;

import com.brewmes.demo.model.IBrewMES;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class BrewMESController {
    //allow spring to inject bean to field
    @Autowired
    private IBrewMES brewMes;

    //Get all machines
    @GetMapping(value = "/machines")
    public ResponseEntity<Object> getMachines() {
        return new ResponseEntity<>(brewMes.getMachines().values().toArray(), HttpStatus.OK);
    }

    //Get a specific machine based on it's id.
    @GetMapping(value = "/machines/{id}")
    public ResponseEntity<Object> getMachine(@PathVariable("id") UUID id) {
        if (brewMes.getMachines().containsKey(id)) {
            return new ResponseEntity<>(brewMes.getMachines().get(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new StringResponse("Sorry i do not know that machine :-)", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }


    //Delete and disconnect a machine from the system by specifying it's id.
    @DeleteMapping(value = "/machines/{id}")
    public ResponseEntity<Object> deleteMachine(@PathVariable("id") UUID id) {
        brewMes.disconnectMachine(id);
        return new ResponseEntity<>(new StringResponse("Machine is removed", HttpStatus.OK.value()), HttpStatus.OK);
    }

    // Sends a command to the machine. The command is send via put request with a json object that is notated with e.g.
    // {'command': 'start'}
    @PutMapping(value = "machines/{id}/command")
    public ResponseEntity<Object> controlMachine(@PathVariable("id") UUID id, @RequestBody String input) {
        JsonObject o = JsonParser.parseString(input).getAsJsonObject();
        String s = o.get("command").getAsString();
        ResponseEntity<Object> response = new ResponseEntity<>(new StringResponse("command updated", HttpStatus.OK.value()), HttpStatus.OK);
        if ("start".equals(s) || "abort".equals(s) || "reset".equals(s) || "clear".equals(s) || "stop".equals(s)) {
            brewMes.controlMachine(s, id);
        } else {
            response = new ResponseEntity<>(new StringResponse("I did not understand that.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    //Connect machine
    //handles post request with json: {"ip":"<ip to connect>"}
    @PostMapping(value = "/machines")
    public ResponseEntity<Object> addMachine(@RequestBody String input) {
        JsonObject o = JsonParser.parseString(input).getAsJsonObject();
        String ip = o.get("ip").getAsString();
        boolean success = brewMes.connectMachine(ip);
        if (success) {
            return new ResponseEntity<>(new StringResponse("Success", HttpStatus.OK.value()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new StringResponse("Failed, not a valid ip", HttpStatus.NOT_ACCEPTABLE.value()), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    //set machine variables
    @PutMapping(value = "/machines/{id}/variables")
    public ResponseEntity<Object> setMachineVariables(@PathVariable("id") UUID id, @RequestBody String input) {
        JsonObject o = JsonParser.parseString(input).getAsJsonObject();
        double speed = o.get("speed").getAsDouble();
        String beerType = o.get("beerType").getAsString();
        int batchSize = o.get("batchSize").getAsInt();
        ResponseEntity<Object> response = new ResponseEntity<>(new StringResponse("Variables set.", HttpStatus.OK.value()), HttpStatus.OK);
        switch (beerType) {
            case "pilsner" -> brewMes.setMachineVariables(speed * 600 / 100, beerType, batchSize, id);
            case "wheat" -> brewMes.setMachineVariables(speed * 300 / 100, beerType, batchSize, id);
            case "ipa" -> brewMes.setMachineVariables(speed * 150 / 100, beerType, batchSize, id);
            case "stout" -> brewMes.setMachineVariables(speed * 200 / 100, beerType, batchSize, id);
            case "ale" -> brewMes.setMachineVariables(speed, beerType, batchSize, id);
            case "alcohol_free" -> brewMes.setMachineVariables(speed * 125 / 100, beerType, batchSize, id);
            default -> new ResponseEntity<>(new StringResponse("I do not know that beer type.", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return response;
    }


    //make this method return last 10 batches
    @GetMapping(value = "/batches")
    public ResponseEntity<Map<String, Object>> getBatches(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        return new ResponseEntity<>(brewMes.getBatchesPage(page, size), HttpStatus.OK);
    }

    //make this method return a batch based on it's id
    @GetMapping(value = "/batches/{id}")
    public ResponseEntity<Object> getBatch(@PathVariable("id") UUID id) {
        if (brewMes.getBatch(id) != null) {
            return new ResponseEntity<>(brewMes.getBatch(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new StringResponse("No batch found with that ID", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }


    /**
     * returns ResponseEntity containing a InputStreamResource with the pdf file
     * corresponding to the filename.
     *
     * @param id the id on the id to be generated
     * @return ResponseEntity with InputStreamResource containing the file
     * @throws FileNotFoundException if thrown, return a ResponseEntity with 404 not found.
     */
    @GetMapping(value = "batches/{id}/get-report", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Object> getBatchReport(@PathVariable UUID id) {
        brewMes.generateReport(brewMes.getBatch(id));
        String fileName = "batch_report.pdf";
        File file = new File(fileName);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(fileName));

            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/pdf")).body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new StringResponse("File not found " + fileName, HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
    }

}
