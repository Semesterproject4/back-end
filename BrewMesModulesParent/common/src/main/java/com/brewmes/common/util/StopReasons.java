package com.brewmes.common.util;

public enum StopReasons {
    EMPTY_INVENTORY(10,"Empty Inventory"),
    MAINTENANCE_NEEDED(11,"Maintenance Needed"),
    MANUAL_STOP(12, "Manual Stop"),
    MOTOR_POWER_LOSS(13, "Motor Power Loss"),
    MANUAL_ABORT(14, "Manual Abort");

    public final int id;
    public final String output;

    StopReasons(int id, String output) {
        this.id = id;
        this.output = output;
    }
}
