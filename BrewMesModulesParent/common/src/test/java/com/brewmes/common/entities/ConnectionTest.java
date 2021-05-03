package com.brewmes.common.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTest {
    Connection connection;

    @BeforeEach
    void setUp() {
        this.connection = new Connection(1,"128.0.0.1", "testMachine");
    }

    @Test
    void getId() {
        int id = connection.getId();

        assertEquals(1, id);
    }

    @Test
    void setId() {
        int newId = 2;
        connection.setId(newId);

        assertEquals(newId, connection.getId());
    }

    @Test
    void getIp() {
        assertEquals("128.0.0.1", connection.getIp());
    }

    @Test
    void setIp() {
        String newIp = "128.0.0.2";
        connection.setIp(newIp);

        assertEquals(newIp, connection.getIp());
    }

    @Test
    void getName() {
        assertEquals("testMachine", connection.getName());
    }

    @Test
    void setName() {
        String newName = "testMachine2";
        connection.setName(newName);

        assertEquals(newName, connection.getName());
    }
}