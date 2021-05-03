package com.brewmes.common.services;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.Products;

import java.util.Map;

public interface IMachineService {
    public void controlMachine(Command command, int id);

    public void setVariables(double speed, Products beertype, int batchSize, int id);

    public void subscibeToMachineValues(int machineId);

    public Map<Integer, Connection> getConnections();

    public boolean addConnection(String ip, int id);

    public boolean removeConnection(int id);

    public void startAutoBrew(int machineId);

    public void stopAutoBrew(int machineId);
}
