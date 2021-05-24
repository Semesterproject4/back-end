package com.brewmes.common.services;

import com.brewmes.common.entities.MachineData;

public interface ILiveDataService {
    public void publish(MachineData machineData, String connectionID);
}
