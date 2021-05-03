package com.brewmes.common.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientsTest {
    Ingredients ingredients;

    @BeforeEach
    void setUp() {
        this.ingredients = new Ingredients(1000,999,100,1002,132);
    }

    @Test
    void getBarley() {
        assertEquals(1000, ingredients.getBarley());
    }

    @Test
    void setBarley() {
        double newBarley = 100.02;
        ingredients.setBarley(newBarley);

        assertEquals(newBarley, ingredients.getBarley());
    }

    @Test
    void getHops() {
        assertEquals(999, ingredients.getHops());
    }

    @Test
    void setHops() {
        double newHops = 102.02;
        ingredients.setHops(newHops);

        assertEquals(newHops, ingredients.getHops());
    }

    @Test
    void getMalt() {
        assertEquals(100, ingredients.getMalt());
    }

    @Test
    void setMalt() {
        double newMalt = 1221.02;
        ingredients.setMalt(newMalt);

        assertEquals(newMalt, ingredients.getMalt());
    }

    @Test
    void getWheat() {
        assertEquals(1002, ingredients.getWheat());
    }

    @Test
    void setWheat() {
        double newWheat = 100.02;
        ingredients.setWheat(newWheat);

        assertEquals(newWheat, ingredients.getWheat());
    }

    @Test
    void getYeast() {
        assertEquals(132, ingredients.getYeast());
    }

    @Test
    void setYeast() {
        double newYeast = 100.02;
        ingredients.setYeast(newYeast);

        assertEquals(newYeast, ingredients.getYeast());
    }
}