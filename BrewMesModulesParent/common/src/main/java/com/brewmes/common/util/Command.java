package com.brewmes.common.util;

public enum Command {
    RESET(1),
    START(2),
    STOP(3),
    ABORT(4),
    CLEAR(5);

    public final int label;

    Command(int label) {
        this.label = label;
    }
}