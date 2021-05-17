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

    @Test
    void getNameByID() {
        assertEquals("Pilsner", Products.getNameByID(0));
    }

    @Test
    void getProductFromID(){
        assertEquals(Products.WHEAT, Products.getProductFromID(1));
    }
}