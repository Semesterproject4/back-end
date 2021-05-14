package com.brewmes.subscriber;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.Connection;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common_repository.BatchRepository;
import com.brewmes.common_repository.ConnectionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MachineSubscriberTest {

    private static final String FAKE_ID = "Fake";
    private static final String GOOD_ID = "Good";
    private static Batch batch;
    private static Connection connection;
    @Mock
    BatchRepository batchRepo;
    @Mock
    ConnectionRepository conRepo;

    @Mock
    HashMap<String, Subscription> subMap;

    @InjectMocks
    MachineSubscriber subscribeService;

    @Mock
    Subscription subscription;

    @BeforeAll
    public static void setup() {
        connection = new Connection();
         batch = new Batch();
    }

    @Test
    void subscribeToMachineValuesSuccess() {
        MachineSubscriber subSpy = Mockito.spy(subscribeService);
        when(conRepo.findById(GOOD_ID)).thenReturn(Optional.of(connection));

        Mockito.doAnswer(invocation -> {
            Thread thread = new Thread(new SubscriptionStub());
            subscribeService.activeThreads.put(GOOD_ID, thread);
            return null;
        }).when(subSpy).createSubscription(GOOD_ID, connection);

        assertTrue(subSpy.subscribeToMachineValues(GOOD_ID));
        assertTrue(subSpy.activeThreads.containsKey(GOOD_ID));
    }


    @Test
    void subscribeToMachineValuesInterrupted() {
        MachineSubscriber subSpy = Mockito.spy(subscribeService);
        when(conRepo.findById(GOOD_ID)).thenReturn(Optional.of(connection));

        Thread thread = new Thread(new SubscriptionStub());
        subSpy.activeThreads.put(GOOD_ID, thread);
        thread.interrupt();

        Mockito.doAnswer(invocation -> {
            Thread thread1 = new Thread(new SubscriptionStub());
            subscribeService.activeThreads.put(GOOD_ID, thread1);
            return null;
        }).when(subSpy).createSubscription(GOOD_ID, connection);

        assertTrue(subSpy.subscribeToMachineValues(GOOD_ID));
        assertTrue(subSpy.activeThreads.containsKey(GOOD_ID));
    }


    @Test
    void subscribeToMachineValuesFail() {
        MachineSubscriber subSpy = Mockito.spy(subscribeService);
        when(conRepo.findById(FAKE_ID)).thenReturn(Optional.empty());

        assertFalse(subSpy.subscribeToMachineValues(FAKE_ID));
        assertFalse(subSpy.activeThreads.containsKey(FAKE_ID));
    }

    @Test
    void getLatestMachineDataFail() {
        MachineData data = subscribeService.getLatestMachineData(FAKE_ID);
        assertNull(data);
    }

    @Test
    void getLatestMachineDataSuccess() {
        MachineSubscriber subSpy = Mockito.spy(subscribeService);

        MachineData fakeData = new MachineData();
        fakeData.setNormSpeed(123.0);
        SubscriptionStub subscriptionStub = new SubscriptionStub();
        subscriptionStub.latestMachineData = fakeData;

        when(subscription.getLatestMachineData()).thenReturn(fakeData);
        subSpy.activeSubscriptions.put(GOOD_ID,subscription);

        MachineData data = subSpy.getLatestMachineData(GOOD_ID);

        assertEquals(fakeData, data);
        assertEquals(fakeData.getNormSpeed(), data.getNormSpeed());
    }

    private class SubscriptionStub implements Runnable {

        MachineData latestMachineData;

        public SubscriptionStub() {
        }

        public MachineData getLatestMachineData(){
            return latestMachineData;
        }

        @Override
        public void run() {

        }
    }
}
