package com.brewmes.common.util;

public enum StopReasons {
    EMPTY_INVENTORY("Empty Inventory", 10),
    MAINTENANCE_NEEDED("Maintenance Needed", 11),
    MANUAL_STOP( "Manual Stop", 12),
    MOTOR_POWER_LOSS("Motor Power Loss", 13),
    MANUAL_ABORT("Manual Abort", 14);

    public final String stopReason;
    public final int id;

    StopReasons(String stopReason, int id) {
        this.stopReason = stopReason;
        this.id = id;
    }
}
