package com.brewmes.livedata;

import com.brewmes.common.entities.MachineData;
import com.brewmes.common.services.ILiveDataService;
import com.brewmes.common.services.ISubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class LiveDataController {

    @Autowired
    private ISubscribeService subscribeService;

    @Autowired
    private ILiveDataService liveDataService;

    @MessageMapping("/connect/{id}")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "If-Match")
    public void liveData(@DestinationVariable("id") String id) {
        subscribeService.subscribeToMachineValues(id);
        MachineData data = subscribeService.getLatestMachineData(id);
        if (data != null) {
            liveDataService.publish(data, id);
        }
    }
}
