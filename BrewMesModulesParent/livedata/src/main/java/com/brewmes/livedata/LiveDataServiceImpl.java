package com.brewmes.livedata;

import com.brewmes.common.entities.MachineData;
import com.brewmes.common.services.ILiveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class LiveDataServiceImpl implements ILiveDataService {


    @Autowired
    @Qualifier("brokerMessagingTemplate")
    private SimpMessagingTemplate template;


    @Override
    public void publish(MachineData machineData, String connectionID) {
        template.convertAndSend("/topic/" + connectionID + "/livedata", machineData);
    }
}
