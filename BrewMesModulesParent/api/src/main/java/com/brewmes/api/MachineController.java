package com.brewmes.api;

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

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/machine")
public class MachineController {

    @Autowired(required = false)
    private IMachineService machineService;


    @GetMapping
    public ResponseEntity<Object> getConnections() {
        return new ResponseEntity<>(machineService.getConnections(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getConnection(@PathVariable("id") String id) {
        Optional<Connection> connection = Optional.ofNullable(machineService.getConnection(id));
        if (connection.isPresent()) {
            return new ResponseEntity<>(connection.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Sorry I don't know that machine :(", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> removeConnection(@PathVariable("id") String id) {
        if (machineService.removeConnection(id)) {
            return new ResponseEntity<>("Machine is removed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not remove machine", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<String> addConnection(@RequestBody String id, @RequestBody String name) {
        if (machineService.addConnection(id, name)) {
            return new ResponseEntity<>("Machine is added", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not add machine", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> controlMachine(@PathVariable("id") String id, @RequestParam String command) {
        if (machineService.controlMachine(Command.valueOf(command.toUpperCase()), id)) {
            return new ResponseEntity<>("Command successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Command unsuccessful", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Sets variables of the machine.
     *
     * @param id    is the {@code machineID}
     * @param input consists of three variables; {@code speed} ({@code double}), {@code beerType} ({@code String}) and {@code batchSize} ({@code int}).
     *              These should be mapped as {@code JSON objects}.
     * @return {@code 200 OK} if successful, {@code 404 NOT FOUND} if unsuccessful.
     */
    @PutMapping(value = "/{id}/variables")
    public ResponseEntity<String> setMachineVariables(@PathVariable("id") String id, @RequestBody String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        double speed = jsonObject.get("speed").getAsDouble();
        Products beerType = Products.valueOf(jsonObject.get("beerType").getAsString().toUpperCase());
        int batchSize = jsonObject.get("batchSize").getAsInt();

        if (machineService.setVariables(speed, beerType, batchSize, id)) {
            return new ResponseEntity<>("Machine Variables set", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not set Machine Variables", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}/autobrew/start")
    public ResponseEntity<String> startAutoBrew(@PathVariable("id") String id) {
        if (machineService.startAutoBrew(id)) {
            return new ResponseEntity<>("Auto Brew started", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to start Auto Brew", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}/autobrew/stop")
    public ResponseEntity<String> stopAutoBrew(@PathVariable("id") String id) {
        if (machineService.stopAutoBrew(id)) {
            return new ResponseEntity<>("Auto Brew stopped", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to stop Auto Brew", HttpStatus.NOT_FOUND);
        }
    }
}