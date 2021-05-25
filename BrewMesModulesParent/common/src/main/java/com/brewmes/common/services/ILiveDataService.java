package com.brewmes.common.services;

import com.brewmes.common.entities.MachineData;

public interface ILiveDataService {

    /** Publishes a message to the outbound channel of the messageBroker notifying all subscribing clients.
     * @param machineData The machineData object to publish.
     * @param connectionID The connectionID to publish to.
     */
    void publish(MachineData machineData, String connectionID);
}
