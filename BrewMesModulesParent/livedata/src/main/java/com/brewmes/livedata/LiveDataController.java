package com.brewmes.livedata;

import com.brewmes.common.entities.MachineData;
import com.brewmes.common.services.ISubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class LiveDataController {

    @Autowired
    private ISubscribeService subscribeService;

    @MessageMapping("/connect/{id}")
    @SendTo("/topic/{id}/livedata")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "If-Match")
    public MachineData liveData(@DestinationVariable("id") String id) throws InterruptedException {

        MachineData machineData = subscribeService.getLatestMachineData(id);

        if (machineData == null) {
            subscribeService.subscribeToMachineValues(id);
            machineData = subscribeService.getLatestMachineData(id);
        }
        
        Thread.sleep(1000);
        return machineData;
    }
}
