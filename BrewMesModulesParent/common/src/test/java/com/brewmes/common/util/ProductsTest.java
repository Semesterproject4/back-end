package com.brewmes.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductsTest {

    @Test
    void values() {
        assertNotNull(Products.values());
    }

    @Test
    void valueOf() {
        assertEquals(Products.PILSNER, Products.valueOf("PILSNER"));
    }
}