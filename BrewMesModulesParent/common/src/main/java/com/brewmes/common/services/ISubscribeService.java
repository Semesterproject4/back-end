package com.brewmes.common.services;


import com.brewmes.common.entities.MachineData;

public interface ISubscribeService {

    /**
     * Sets up a subscribtion to the machine values based on a {@code connectionID} representing the machine.
     * @param connectionID The id of the connection to subscribe to.
     * @return {@code true} if the subscribtion went successfully, and {@code false} if something went wrong.
     */
 boolean subscribeToMachineValues(String connectionID);

    /**
     * Returns the latest {@code MachineData} measured from a given machine.
     * @param connectionID the connectionID representing the machines connection.
     * @return {@code MachineData} if there exists a current subscribtion on related to the {@code connectionID},
     * returns {@code null} if no subscribtions were found for the {@code connectionID}.
     */

 MachineData getLatestMachineData(String connectionID);


}
