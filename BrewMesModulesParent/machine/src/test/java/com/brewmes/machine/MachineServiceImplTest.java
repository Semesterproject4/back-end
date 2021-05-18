package com.brewmes.machine;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.MachineState;
import com.brewmes.common.util.Products;
import com.brewmes.common_repository.ConnectionRepository;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MachineServiceImplTest {
    private static Connection connectionWithoutID;
    private static Connection connectionWithID;
    private static Map<String, OpcUaClient> opcUaClients = new HashMap<>();
    private static OpcUaClient client;
    private static DataValue valueReset;
    private static DataValue valueTrue;
    private static DataValue valueBeerType;
    private static DataValue valueSpeed;
    private static DataValue valueAmount;
    private static CompletableFuture<StatusCode> cf;
    @Mock
    private ConnectionRepository connectionRepository;
    @InjectMocks
    private MachineServiceImpl machineService;

    @BeforeAll
    static void setUpAll() {
        String id = "id";
        connectionWithoutID = new Connection("0.0.0.1", "name");
        connectionWithID = new Connection("0.0.0.2", "name2");
        connectionWithID.setId(id);

        client = Mockito.mock(OpcUaClient.class);
        opcUaClients.put("1234", client);

        valueReset = DataValue.valueOnly(new Variant(Command.RESET.label));
        valueTrue = DataValue.valueOnly(new Variant(true));
        valueBeerType = DataValue.valueOnly(new Variant((float) Products.PILSNER.productType));
        valueSpeed = DataValue.valueOnly(new Variant((float) 20.0));
        valueAmount = DataValue.valueOnly(new Variant((float) 50));

        cf = Mockito.mock(CompletableFuture.class);
    }

    @BeforeEach
    void setUpEach() {
        machineService.setOpcUaClients(opcUaClients);
    }

    @Test
    void controlmachine() {
        NodeId nodeIDCntrlCmd = new NodeId(6, "::Program:Cube.Command.CntrlCmd");
        NodeId nodeIDChangeRequest = new NodeId(6, "::Program:Cube.Command.CmdChangeRequest");

        Mockito.when(client.writeValue(nodeIDCntrlCmd, valueReset)).thenReturn(cf);
        Mockito.when(client.writeValue(nodeIDChangeRequest, valueTrue)).thenReturn(cf);
        try {
            Mockito.when(cf.get()).thenReturn(new StatusCode(200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        assertTrue(machineService.controlMachine(Command.RESET, "1234"));
    }

    @Test
    void controlmachine_throwInterruptedException() {
        NodeId nodeIDCntrlCmd = new NodeId(6, "::Program:Cube.Command.CntrlCmd");

        Mockito.when(client.writeValue(nodeIDCntrlCmd, valueReset)).thenReturn(cf);
        try {
            Mockito.when(cf.get()).thenThrow(InterruptedException.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        assertFalse(machineService.controlMachine(Command.RESET, "1234"));
    }

    @Test
    void controlmachine_throwExecutionException() {
        NodeId nodeIDCntrlCmd = new NodeId(6, "::Program:Cube.Command.CntrlCmd");

        Mockito.when(client.writeValue(nodeIDCntrlCmd, valueReset)).thenReturn(cf);
        try {
            Mockito.when(cf.get()).thenThrow(ExecutionException.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        assertFalse(machineService.controlMachine(Command.RESET, "1234"));
    }


    @Test
    void setMachineVariables() {
        NodeId setBeerType = new NodeId(6, "::Program:Cube.Command.Parameter[1].Value");
        NodeId setSpeed = new NodeId(6, "::Program:Cube.Command.MachSpeed");
        NodeId setBatchSize = new NodeId(6, "::Program:Cube.Command.Parameter[2].Value");

        Mockito.when(client.writeValue(setBeerType, valueBeerType)).thenReturn(cf);
        Mockito.when(client.writeValue(setSpeed, valueSpeed)).thenReturn(cf);
        Mockito.when(client.writeValue(setBatchSize, valueAmount)).thenReturn(cf);
        try {
            Mockito.when(cf.get()).thenReturn(new StatusCode(200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        assertTrue(machineService.setMachineVariables(20.0, Products.PILSNER, 50, "1234"));
    }


    @Test
    void setMachineVariablesThrowInterruptedException() {
        NodeId setBeerType = new NodeId(6, "::Program:Cube.Command.Parameter[1].Value");
        NodeId setSpeed = new NodeId(6, "::Program:Cube.Command.MachSpeed");
        NodeId setBatchSize = new NodeId(6, "::Program:Cube.Command.Parameter[2].Value");

        Mockito.when(client.writeValue(setBeerType, valueBeerType)).thenReturn(cf);
        Mockito.when(client.writeValue(setSpeed, valueSpeed)).thenReturn(cf);
        Mockito.when(client.writeValue(setBatchSize, valueAmount)).thenReturn(cf);
        try {
            Mockito.when(cf.get()).thenThrow(InterruptedException.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        assertFalse(machineService.setMachineVariables(20.0, Products.PILSNER, 50, "1234"));
    }


    @Test
    void setMachineVariablesThrowExecutionException() {
        NodeId setBeerType = new NodeId(6, "::Program:Cube.Command.Parameter[1].Value");
        NodeId setSpeed = new NodeId(6, "::Program:Cube.Command.MachSpeed");
        NodeId setBatchSize = new NodeId(6, "::Program:Cube.Command.Parameter[2].Value");

        Mockito.when(client.writeValue(setBeerType, valueBeerType)).thenReturn(cf);
        Mockito.when(client.writeValue(setSpeed, valueSpeed)).thenReturn(cf);
        Mockito.when(client.writeValue(setBatchSize, valueAmount)).thenReturn(cf);
        try {
            Mockito.when(cf.get()).thenThrow(ExecutionException.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        assertFalse(machineService.setMachineVariables(20.0, Products.PILSNER, 50, "1234"));
    }


    @Test
    void getConnections() {
        List<Connection> list = new ArrayList<>();
        list.add(connectionWithoutID);

        Mockito.when(connectionRepository.findAll()).thenReturn(list);
        assertEquals(list, machineService.getConnections());
    }

    @Test
    void getConnection() {
        Mockito.when(connectionRepository.findById(connectionWithID.getId())).thenReturn(Optional.of(connectionWithID));
        assertEquals(connectionWithID, machineService.getConnection(connectionWithID.getId()));
    }

    @Test
    void addConnection() {
        Mockito.when(connectionRepository.insert(connectionWithoutID)).thenReturn(connectionWithID);
        assertTrue(machineService.addConnection(connectionWithoutID));
    }

    @Test
    void addConnection_error() {
        Mockito.when(connectionRepository.insert(connectionWithoutID)).thenReturn(connectionWithoutID);
        assertFalse(machineService.addConnection(connectionWithoutID));
    }

    @Test
    void removeConnection() {
        Mockito.doNothing().when(connectionRepository).deleteById(connectionWithID.getId());
        Mockito.when(connectionRepository.findById(connectionWithID.getId())).thenReturn(Optional.empty());

        assertTrue(machineService.removeConnection(connectionWithID.getId()));
    }

    @Test
    void getProducts() {
        List<Products> expected = new ArrayList<>();

        expected.add(Products.PILSNER);
        expected.add(Products.WHEAT);
        expected.add(Products.IPA);
        expected.add(Products.STOUT);
        expected.add(Products.ALE);
        expected.add(Products.ALCOHOL_FREE);

        assertEquals(expected, machineService.getProducts());
    }

    @Test
    void getStates() {
        List<MachineState> expected = new ArrayList<>();

        expected.add(MachineState.DEACTIVATED);
        expected.add(MachineState.CLEARING);
        expected.add(MachineState.STOPPED);
        expected.add(MachineState.STARTING);
        expected.add(MachineState.IDLE);
        expected.add(MachineState.SUSPENDED);
        expected.add(MachineState.EXECUTE);
        expected.add(MachineState.STOPPING);
        expected.add(MachineState.ABORTING);
        expected.add(MachineState.ABORTED);
        expected.add(MachineState.HOLDING);
        expected.add(MachineState.HELD);
        expected.add(MachineState.RESETTING);
        expected.add(MachineState.COMPLETING);
        expected.add(MachineState.COMPLETE);
        expected.add(MachineState.DEACTIVATING);
        expected.add(MachineState.ACTIVATING);

        assertEquals(expected, machineService.getStates());
    }
}