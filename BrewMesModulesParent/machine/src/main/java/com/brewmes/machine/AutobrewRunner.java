package com.brewmes.machine;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.services.IMachineService;
import com.brewmes.common.services.IScheduleService;
import com.brewmes.common.services.ISubscribeService;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.MachineState;
import org.springframework.beans.factory.annotation.Autowired;

public class AutobrewRunner implements Runnable {
    @Autowired(required = false)
    ISubscribeService subscribeService;

    @Autowired(required = false)
    IScheduleService scheduleService;

    @Autowired
    IMachineService machineService;

    String connectionID;

    public AutobrewRunner(String connectionID) {
        this.connectionID = connectionID;
    }

    @Override
    public void run() {
        while (true) {
            int state = subscribeService.getLatestMachineData(connectionID).getState();
            if (state == MachineState.IDLE.value) {
                if (!scheduleService.queueIsEmpty()) {
                    ScheduledBatch scheduledBatch = scheduleService.getFirstInQueue();
                    machineService.setMachineVariables(scheduledBatch.getSpeed(), scheduledBatch.getType(), scheduledBatch.getAmount(), connectionID);
                    machineService.controlMachine(Command.START, connectionID);
                }
            } else if (state == MachineState.COMPLETE.value) {
                machineService.controlMachine(Command.RESET, connectionID);
            } else if (state == MachineState.STOPPED.value) {
                machineService.controlMachine(Command.RESET, connectionID);
            } else if (state == MachineState.ABORTED.value) {
                machineService.stopAutoBrew(connectionID);
                break;
            }
        }
    }
}
