package com.brewmes.machine;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.services.IMachineService;
import com.brewmes.common.services.ISubscribeService;
import com.brewmes.common.util.Command;
import com.brewmes.common_repository.BatchRepository;
import com.brewmes.common_repository.ConnectionRepository;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MachineServiceImplTest {
    @Mock
    ConnectionRepository connectionRepository;

    @Mock
    ISubscribeService subscribeService;

    @InjectMocks
    MachineServiceImpl machineService;

    Connection connection;
    OpcUaClient client;

    @BeforeEach
    void setUp() {
        String connectionID = "goodID";
        connection = new Connection("1.0.0.1", "name");
        connection.setId(connectionID);

        client = Mockito.mock(OpcUaClient.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void controlMachine() {

        Mockito.when(connectionRepository.findById(connection.getId())).thenReturn(Optional.of(connection));
        CompletableFuture completableFuture = Mockito.mock(CompletableFuture.class);
        Mockito.when(client.writeValue(new NodeId(6, "::Program:Cube.Command.CmdChangeRequest"), DataValue.valueOnly(new Variant(true)))).thenReturn(completableFuture);

        try {
            Mockito.when(completableFuture.get()).thenReturn(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        assertTrue(machineService.controlMachine(Command.START, "goodID"));
    }

    @Test
    void setVariables() {
    }

    @Test
    void getConnections() {
    }

    @Test
    void getConnection() {
    }

    @Test
    void addConnection() {
    }

    @Test
    void removeConnection() {
    }

    @Test
    void startAutoBrew() {
    }

    @Test
    void stopAutoBrew() {
    }
}