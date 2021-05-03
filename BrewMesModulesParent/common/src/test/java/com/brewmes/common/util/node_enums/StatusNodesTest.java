package com.brewmes.common.util.node_enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusNodesTest {

    @Test
    void values() {
        assertNotNull(StatusNodes.values());
    }

    @Test
    void valueOf() {
        assertEquals(StatusNodes.HUMIDITY, StatusNodes.valueOf("HUMIDITY"));
    }
}