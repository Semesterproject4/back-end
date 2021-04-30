package com.brewmes.common.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void values() {
        assertNotNull(Command.values());
    }

    @Test
    void valueOf() {
        assertEquals(Command.RESET, Command.valueOf("RESET"));
        assertEquals(1, Command.RESET.label);

        assertEquals(Command.START, Command.valueOf("START"));
        assertEquals(2, Command.START.label);

        assertEquals(Command.STOP, Command.valueOf("STOP"));
        assertEquals(3, Command.STOP.label);

        assertEquals(Command.ABORT, Command.valueOf("ABORT"));
        assertEquals(4, Command.ABORT.label);

        assertEquals(Command.CLEAR, Command.valueOf("CLEAR"));
        assertEquals(5, Command.CLEAR.label);
    }
}