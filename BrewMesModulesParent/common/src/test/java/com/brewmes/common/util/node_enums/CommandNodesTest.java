package com.brewmes.common.util.node_enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandNodesTest {

    @Test
    void values() {
        assertNotNull(CommandNodes.values());
    }

    @Test
    void valueOf() {
        assertEquals(CommandNodes.SET_MACHINE_SPEED, CommandNodes.valueOf("SET_MACHINE_SPEED"));
    }
}