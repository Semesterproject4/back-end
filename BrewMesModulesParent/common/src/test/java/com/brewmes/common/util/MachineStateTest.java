package com.brewmes.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MachineStateTest {

    @Test
    void getValue() {
        assertEquals(4, MachineState.IDLE.getValue());
    }

    @Test
    void values() {
        assertNotNull(MachineState.values());
    }

    @Test
    void valueOf() {
       assertEquals(MachineState.RESETTING, MachineState.valueOf("RESETTING"));
    }
}