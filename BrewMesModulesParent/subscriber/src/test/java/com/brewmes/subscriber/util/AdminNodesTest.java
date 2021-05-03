package com.brewmes.subscriber.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminNodesTest {

    @Test
    void values() {
        assertNotNull(AdminNodes.values());
    }

    @Test
    void valueOf() {
        assertEquals(AdminNodes.BATCH_PRODUCT_ID, AdminNodes.valueOf("BATCH_PRODUCT_ID"));
    }
}