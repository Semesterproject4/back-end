package com.brewmes.api;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.services.IMachineService;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.Products;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MachineControllerTest {

    @Mock
    IMachineService machineService;

    @InjectMocks
    MachineController machineController;


    @Test
    void getConnections() {
        List<Connection> connectionList = new ArrayList<>();
        connectionList.add(new Connection("123", "0.0.0.0", "Machine1"));


        Mockito.when(machineService.getConnections()).thenReturn(connectionList);
        ResponseEntity<Object> response = machineController.getConnections();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(connectionList, response.getBody());
    }

    @Test
    void getConnection_missing() {
        Mockito.when(machineService.getConnection("789")).thenReturn(null);
        ResponseEntity<Object> response = machineController.getConnection("789");


        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getConnection_present() {
        List<Connection> connectionList = new ArrayList<>();
        connectionList.add(new Connection("123", "0.0.0.0", "Machine1"));

        Mockito.when(machineService.getConnection("123")).thenReturn(connectionList.get(0));

        ResponseEntity<Object> response = machineController.getConnection("123");

        Connection responseConnection = (Connection) response.getBody();
        assertEquals(connectionList.get(0), responseConnection);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void deleteConnection_present() {
        Mockito.when(machineService.removeConnection("123")).thenReturn(true);

        ResponseEntity<String> response = machineController.removeConnection("123");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void deleteConnection_missing() {
        Mockito.when(machineService.removeConnection("123")).thenReturn(false);

        ResponseEntity<String> response = machineController.removeConnection("123");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void addConnection_succes() {
        Mockito.when(machineService.addConnection("1.2.3.4", "T-1000")).thenReturn(true);

        ResponseEntity<String> response = machineController.addConnection("1.2.3.4", "T-1000");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void addConnection_failure() {
        Mockito.when(machineService.addConnection("4.3.2.1", "T-800")).thenReturn(false);

        ResponseEntity<String> response = machineController.addConnection("4.3.2.1", "T-800");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void controlMachine_success() {
        Mockito.when(machineService.controlMachine(Command.START, "123")).thenReturn(true);

        ResponseEntity<String> response = machineController.controlMachine("123", "start");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void controlMachine_failure() {
        Mockito.when(machineService.controlMachine(Command.STOP, "123")).thenReturn(false);

        ResponseEntity<String> response = machineController.controlMachine("123", "stop");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void setMachineVariables_success() {
        Mockito.when(machineService.setVariables(50.0, Products.ALE, 100, "123")).thenReturn(true);

        double speed = 50.0;
        String beerType = "ale";
        int BatchSize = 100;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("speed", speed);
        jsonObject.addProperty("beerType", beerType);
        jsonObject.addProperty("batchSize", BatchSize);

        ResponseEntity<String> response = machineController.setMachineVariables("123", String.valueOf(jsonObject));

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void setMachineVariables_failure() {
        Mockito.when(machineService.setVariables(50.0, Products.ALE, 100, "789")).thenReturn(false);

        double speed = 50.0;
        String beerType = "ale";
        int BatchSize = 100;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("speed", speed);
        jsonObject.addProperty("beerType", beerType);
        jsonObject.addProperty("batchSize", BatchSize);

        ResponseEntity<String> response = machineController.setMachineVariables("789", String.valueOf(jsonObject));

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void startAutoBrew_success() {
        Mockito.when(machineService.startAutoBrew("123")).thenReturn(true);

        ResponseEntity<String> response = machineController.startAutoBrew("123");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void startAutoBrew_failure() {
        Mockito.when(machineService.startAutoBrew("789")).thenReturn(false);

        ResponseEntity<String> response = machineController.startAutoBrew("789");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void stopAutoBrew_success() {
        Mockito.when(machineService.stopAutoBrew("123")).thenReturn(true);

        ResponseEntity<String> response = machineController.stopAutoBrew("123");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void stopAutoBrew_failure() {
        Mockito.when(machineService.stopAutoBrew("789")).thenReturn(false);

        ResponseEntity<String> response = machineController.stopAutoBrew("789");

        assertEquals(404, response.getStatusCodeValue());
    }
}