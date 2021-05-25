package com.brewmes.livedata;

import com.brewmes.common.services.ISubscribeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiveDataControllerTest {

    private static final String ID = "SomeId";

    @Mock
    ISubscribeService subscribeService;

    @InjectMocks
    LiveDataController liveDataController;

    @Test
    void liveDataTest() {
        when(subscribeService.subscribeToMachineValues(ID)).thenReturn(true);
        liveDataController.liveData(ID);
        verify(subscribeService, times(1)).subscribeToMachineValues(ID);
    }


}