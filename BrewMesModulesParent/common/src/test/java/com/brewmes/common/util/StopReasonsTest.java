package com.brewmes.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StopReasonsTest {

    @Test
    void values() {
        assertNotNull(StopReasons.values());
    }

    @Test
    void valueOf() {
        assertEquals(StopReasons.MAINTENANCE_NEEDED, StopReasons.valueOf("MAINTENANCE_NEEDED"));
    }
}