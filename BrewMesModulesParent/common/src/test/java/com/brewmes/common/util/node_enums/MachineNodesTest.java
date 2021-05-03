package com.brewmes.common.util.node_enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MachineNodesTest {

    @Test
    void values() {
        assertNotNull(MachineNodes.values());
    }

    @Test
    void valueOf() {
        assertEquals(MachineNodes.BARLEY, MachineNodes.valueOf("BARLEY"));
    }
}