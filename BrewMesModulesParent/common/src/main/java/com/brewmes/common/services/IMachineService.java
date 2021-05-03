package com.brewmes.common.services;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.Products;

import java.util.Map;

public interface IMachineService {
    public void controlMachine(Command command, String id);

    public void setVariables(double speed, Products beertype, int batchSize, String id);

    public void subscibeToMachineValues(String machineId);

    public Map<Integer, Connection> getConnections();

    public boolean addConnection(String ip, String name);

    public boolean removeConnection(String id);

    public void startAutoBrew(String machineId);

    public void stopAutoBrew(String machineId);
}
