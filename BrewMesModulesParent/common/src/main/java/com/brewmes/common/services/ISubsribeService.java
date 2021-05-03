package com.brewmes.common.services;


import com.brewmes.common.entities.MachineData;

public interface ISubsribeService {

    /**
     * Sets up a subscribtion to the machine values based on a {@code connectionID} representing the machine.
     * @param connectionID The id og the connection to subscribe to.
     */
 public void subscibeToMachineValues(String connectionID);

    /**
     * Should return the latest {@code MachineData} measured.
     * @param connectionID the connectionID representing the machines connection.
     * @return Returns {@code MachineData} if there exists a current subscribtion on related to the {@code connectionID},
     * returns {@code null} if no subscribtions were found for the {@code connectionID}.
     */

 public MachineData getLatestMachineData(String connectionID);


}
