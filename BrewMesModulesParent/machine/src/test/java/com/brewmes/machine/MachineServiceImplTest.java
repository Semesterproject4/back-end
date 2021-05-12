package com.brewmes.machine;

import com.brewmes.common.entities.Connection;
import com.brewmes.common_repository.ConnectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MachineServiceImplTest {
    @Mock
    ConnectionRepository connectionRepository;

    @InjectMocks
    MachineServiceImpl machineService;

    Connection connectionWithoutID;
    Connection connectionWithID;

    @BeforeEach
    void setUp() {
        String id = "id";
        connectionWithoutID = new Connection("0.0.0.1", "name");
        connectionWithID = new Connection("0.0.0.2", "name2");
        connectionWithID.setId(id);
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
}