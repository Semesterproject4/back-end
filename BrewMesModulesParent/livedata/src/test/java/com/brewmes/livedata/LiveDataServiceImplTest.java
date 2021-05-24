package com.brewmes.livedata;

import com.brewmes.common.entities.MachineData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiveDataServiceImplTest {

    @Mock
    private SimpMessagingTemplate template;

    @InjectMocks
    private LiveDataServiceImpl liveDataService;

    private MachineData data;

    @BeforeEach
    void setUp() {
        data = new MachineData();
    }


    @Test
    void publishTest() {
        doNothing().when(template).convertAndSend("/topic/1/livedata", data);
        liveDataService.publish(data, "1");
        verify(template, times(1)).convertAndSend("/topic/1/livedata", data);
    }
}