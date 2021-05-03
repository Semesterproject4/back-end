package com.brewmes.common.services;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.Products;

import java.util.List;

public interface IMachineService {
    public void controlMachine(Command command, String machineId);

    public void setVariables(double speed, Products beertype, int batchSize, String machineId);

    public void subscibeToMachineValues(String machineId);

    public List<Connection> getConnections();

    public boolean addConnection(String ip, String name);

    public boolean removeConnection(String machineId);

    public void startAutoBrew(String machineId);

    public void stopAutoBrew(String machineId);
}
