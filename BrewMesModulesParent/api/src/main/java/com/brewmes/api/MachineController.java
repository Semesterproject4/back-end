package com.brewmes.api;

import com.brewmes.common.services.IMachineService;
import com.brewmes.common.util.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/machines")
public class MachineController {

    @Autowired(required = false)
    private IMachineService machineService;


    @GetMapping
    public ResponseEntity<Object> getConnections() {
        return new ResponseEntity<>(machineService.getConnections(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getConnection(@PathVariable("id") int id) {
        if (machineService.getConnections().contains(id)) {
            return new ResponseEntity<>(machineService.getConnections().get(id), HttpStatus.OK);
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
        machineService.controlMachine(Command.START, id); //Change to general command
        return new ResponseEntity<>("command updated", HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/variables")
    public ResponseEntity<Object> setMachineVariables(@PathVariable("id") int id, @RequestBody String input) {
        throw new UnsupportedOperationException();
    }

    @PutMapping(value = "/{id}/start-autobrew")
    public ResponseEntity<Object> startAutoBrew(@PathVariable("id") int id) {
        throw new UnsupportedOperationException();
    }

    @PutMapping(value = "/{id}/stop-autobrew")
    public ResponseEntity<Object> stopAutoBrew(@PathVariable("id") int id) {
        throw new UnsupportedOperationException();
    }
}
