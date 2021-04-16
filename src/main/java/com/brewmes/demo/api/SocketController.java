package com.brewmes.demo.api;

import com.brewmes.demo.model.IBrewMES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;

@Controller
public class SocketController {

    @Autowired
    private IBrewMES brewMes;

    @MessageMapping("/connect/{id}")
    @SendTo("/topic/{id}/livedata")
    @CrossOrigin(origins = "*")
    public Object livedata(@DestinationVariable("id") UUID id) throws Exception {

        brewMes.getMachines().get(id).readLiveData();

        Thread.sleep(1000); // simulated delay
        return brewMes.getMachines().get(id);
    }
}
