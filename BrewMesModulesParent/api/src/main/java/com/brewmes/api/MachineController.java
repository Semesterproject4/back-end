package com.brewmes.api;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.Connection;
import com.brewmes.common.services.IMachineService;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.Products;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/machine")
public class MachineController {

    @Autowired(required = false)
    private IMachineService machineService;


    @GetMapping
    public ResponseEntity<Object> getConnections() {
        machineService.getConnections();
        if (!machineService.getConnections().isEmpty()) {
            return new ResponseEntity<>(machineService.getConnections(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("List is empty", HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getConnection(@PathVariable("id") String id) {
        Optional<Connection> connection = Optional.ofNullable(machineService.getConnection(id));
        if (connection.isPresent()) {
            return new ResponseEntity<>("Connection obtained", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Sorry I don't know that machine :(", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteConnection(@PathVariable("id") String id) {
        if (machineService.removeConnection(id)) {
            return new ResponseEntity<>("Machine is removed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not remove machine", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<Object> addConnection(@PathVariable("id") String id, @RequestBody String name) {
        if(machineService.addConnection(id, name)) {
            return new ResponseEntity<>("Machine is added", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not add machine", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(value = "/{id}/control")
    public ResponseEntity<Object> controlMachine(@PathVariable("id") String id, @RequestBody String command) {
        if (machineService.controlMachine(Command.valueOf(command.toUpperCase()), id)) {
            return new ResponseEntity<>("Command successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Command unsuccessful", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(value = "/{id}/variables")
    public ResponseEntity<Object> setMachineVariables(@PathVariable("id") String id, @RequestBody String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        double speed = jsonObject.get("speed").getAsDouble();
        Products beerType = Products.valueOf(jsonObject.get("beerType").getAsString());
        int batchSize = jsonObject.get("batchSize").getAsInt();

        if (machineService.setVariables(speed, beerType, batchSize, id)) {
            return new ResponseEntity<>("Machine Variables set", HttpStatus.OK);
        } else {
         return new ResponseEntity<>("Could not set Machine Variables", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(value = "/{id}/start-autobrew")
    public ResponseEntity<Object> startAutoBrew(@PathVariable("id") String id) {
        if (machineService.startAutoBrew(id)) {
            return new ResponseEntity<>("Auto Brew started", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to start Auto Brew", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(value = "/{id}/stop-autobrew")
    public ResponseEntity<Object> stopAutoBrew(@PathVariable("id") String id) {
        if (machineService.stopAutoBrew(id)) {
            return new ResponseEntity<>("Auto Brew stopped", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to stop Auto Brew", HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
