package com.brewmes.common.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void assertReset() {
        assertEquals(Command.RESET, Command.valueOf("RESET"));
        assertEquals(1, Command.RESET.label);
    }

    @Test
    void assertStart() {
        assertEquals(Command.START, Command.valueOf("START"));
        assertEquals(2, Command.START.label);
    }

    @Test
    void assertStop() {
        assertEquals(Command.STOP, Command.valueOf("STOP"));
        assertEquals(3, Command.STOP.label);
    }

    @Test
    void assertAbort() {
        assertEquals(Command.ABORT, Command.valueOf("ABORT"));
        assertEquals(4, Command.ABORT.label);
    }

    @Test
    void assertClear() {
        assertEquals(Command.CLEAR, Command.valueOf("CLEAR"));
        assertEquals(5, Command.CLEAR.label);
    }
}
