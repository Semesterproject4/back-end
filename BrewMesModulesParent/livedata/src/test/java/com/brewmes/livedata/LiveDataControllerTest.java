package com.brewmes.livedata;

import com.brewmes.common.entities.MachineData;
import com.brewmes.common.services.ISubscribeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LiveDataControllerTest {

    private static final String ID = "SomeId";
    private static MachineData machineData;
    @Mock
    ISubscribeService subscribeService;
    @InjectMocks
    LiveDataController liveDataController;

    @BeforeAll
    public static void setUp() {
        machineData = new MachineData();

    }

    @Test
    void livedataSuccess() throws InterruptedException {
        when(subscribeService.getLatestMachineData(ID)).thenReturn(machineData);

        MachineData actual = liveDataController.livedata(ID);
        MachineData expected = machineData;

        assertEquals(expected, actual);
    }

    @Test
    void livedataFail() throws InterruptedException {
        when(subscribeService.getLatestMachineData(ID)).thenReturn(null);

        assertNull(liveDataController.livedata(ID));
    }
}