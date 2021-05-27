package com.brewmes.livedata;

import com.brewmes.common.entities.MachineData;
import com.brewmes.common.services.ILiveDataService;
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

    @Mock
    ILiveDataService liveDataService;

    @InjectMocks
    LiveDataController liveDataController;

    @Test
    void liveDataTest_noData() {
        when(subscribeService.subscribeToMachineValues(ID)).thenReturn(true);
        liveDataController.liveData(ID);
        verify(subscribeService, times(1)).subscribeToMachineValues(ID);
    }

    @Test
    void liveDataTest_null() {
        when(subscribeService.subscribeToMachineValues(ID)).thenReturn(true);

        MachineData machineData = new MachineData();
        when(subscribeService.getLatestMachineData(ID)).thenReturn(machineData);

        doNothing().when(liveDataService).publish(machineData, ID);

        liveDataController.liveData(ID);

        verify(liveDataService, times(1)).publish(machineData, ID);
    }

}
