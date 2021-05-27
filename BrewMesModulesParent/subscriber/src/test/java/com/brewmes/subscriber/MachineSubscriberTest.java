package com.brewmes.subscriber;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common_repository.ConnectionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MachineSubscriberTest {

    private static final String FAKE_ID = "Fake";
    private static final String GOOD_ID = "Good";
    private static Connection connection;
    private static MachineSubscriber subSpy;

    @Mock
    ConnectionRepository conRepo;

    @InjectMocks
    MachineSubscriber subscribeService;

    @Mock
    Subscription subscription;

    @BeforeAll
    static void setup() {
        connection = new Connection();
    }

    @BeforeEach
    void beforeEach() {
        subSpy = Mockito.spy(subscribeService);
    }

    @Test
    void subscribeToMachineValuesSuccess() {
        when(conRepo.findById(GOOD_ID)).thenReturn(Optional.of(connection));

        mockSubscription();

        assertTrue(subSpy.subscribeToMachineValues(GOOD_ID));
        assertTrue(subSpy.activeThreads.containsKey(GOOD_ID));
    }

    @Test
    void subscribeToMachineValuesInterrupted() {
        when(conRepo.findById(GOOD_ID)).thenReturn(Optional.of(connection));
        Thread thread = new Thread(new SubscriptionStub());
        subSpy.activeThreads.put(GOOD_ID, thread);
        thread.interrupt();

        mockSubscription();

        assertTrue(subSpy.subscribeToMachineValues(GOOD_ID));
        assertTrue(subSpy.activeThreads.containsKey(GOOD_ID));
    }

    private void mockSubscription() {
        Mockito.doAnswer(invocation -> {
            Thread thread1 = new Thread(new SubscriptionStub());
            subscribeService.activeThreads.put(GOOD_ID, thread1);
            return null;
        }).when(subSpy).createSubscription(GOOD_ID, connection);
    }

    @Test
    void subscribeToMachineValuesNotInterrupted() {
        when(conRepo.findById(GOOD_ID)).thenReturn(Optional.of(connection));

        Thread thread = new Thread(new SubscriptionStub());
        subSpy.activeThreads.put(GOOD_ID, thread);

        assertFalse(subSpy.subscribeToMachineValues(GOOD_ID));
        assertTrue(subSpy.activeThreads.containsKey(GOOD_ID));
    }

    @Test
    void subscribeToMachineValuesFail() {
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
        MachineData fakeData = new MachineData();
        fakeData.setNormSpeed(123.0);

        when(subscription.getLatestMachineData()).thenReturn(fakeData);
        subSpy.activeSubscriptions.put(GOOD_ID, subscription);

        MachineData data = subSpy.getLatestMachineData(GOOD_ID);

        assertEquals(fakeData, data);
        assertEquals(fakeData.getNormSpeed(), data.getNormSpeed());
    }

    private static class SubscriptionStub implements Runnable {
        @Override
        public void run() {}
    }
}
