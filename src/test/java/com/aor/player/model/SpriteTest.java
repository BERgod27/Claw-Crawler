package com.aor.player.model;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class SpriteTest {

    @Test
    void testConstructorAndGetters() {
        Color[][] pixels = new Color[2][2];
        pixels[0][0] = Color.RED;
        Sprite sprite = new Sprite(2, 2, pixels);

        assertEquals(2, sprite.getWidth());
        assertEquals(2, sprite.getHeight());
        assertArrayEquals(pixels, sprite.getPixels());
    }

    @Test
    void testGetPixelValid() {
        Color[][] pixels = { { Color.RED, Color.BLUE }, { Color.GREEN, Color.YELLOW } };
        Sprite sprite = new Sprite(2, 2, pixels);

        assertEquals(Color.RED, sprite.getPixel(0, 0));
        assertEquals(Color.BLUE, sprite.getPixel(1, 0));
        assertEquals(Color.GREEN, sprite.getPixel(0, 1));
        assertEquals(Color.YELLOW, sprite.getPixel(1, 1));
    }

    @Test
    void testGetPixelOutOfBounds() {
        Color[][] pixels = { { Color.RED } };
        Sprite sprite = new Sprite(1, 1, pixels);

        assertNull(sprite.getPixel(-1, 0));
        assertNull(sprite.getPixel(0, -1));
        assertNull(sprite.getPixel(1, 0));
        assertNull(sprite.getPixel(0, 1));
        assertNull(sprite.getPixel(2, 2));
    }
}
