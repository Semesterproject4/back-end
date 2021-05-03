package com.brewmes.common.util;

public enum MachineState {
    DEACTIVATED("Deactivated", 0),
    CLEARING("Clearing", 1),
    STOPPED("Stopped", 2),
    STARTING("Starting", 3),
    IDLE("Idle", 4),
    SUSPENDED("Suspended", 5),
    EXECUTE("Execute", 6),
    STOPPING("Stopping", 7),
    ABORTING("Aborting", 8),
    ABORTED("Aborted", 9),
    HOLDING("Holding", 10),
    HELD("Held", 11),
    RESETTING("Resetting", 15),
    COMPLETING("Completing", 16),
    COMPLETE("Complete", 17),
    DEACTIVATING("Deactivating", 18),
    ACTIVATING("Activating", 19);

    public final String state;
    public final int value;

    MachineState(String state, int value) {
        this.state = state;
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
