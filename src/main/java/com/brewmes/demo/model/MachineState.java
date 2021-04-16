package com.brewmes.demo.model;

public enum MachineState {
    DEACTIVATED(0),
    CLEARING(1),
    STOPPED(2),
    STARTING(3),
    IDLE(4),
    SUSPENDED(5),
    EXECUTE(6),
    STOPPING(7),
    ABORTING(8),
    ABORTED(9),
    HOLDING(10),
    HELD(11),
    RESETTING(15),
    COMPLETING(16),
    COMPLETE(17),
    DEACTIVATING(18),
    ACTIVATING(19);

    public final int label;

    MachineState(int label) {
        this.label = label;
    }

    public static MachineState valueOfLabel(int label) {
        for (MachineState e : values()) {
            if (e.label == label) {
                return e;
            }
        }
        return null;
    }

}
