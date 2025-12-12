package com.aor.player.model;

import java.awt.*;

public class Sprite {
    private final int width;
    private final int height;
    private final Color[][] pixels;

    public Sprite(int width, int height, Color[][] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getPixel(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return pixels[y][x];
    }

    public Color[][] getPixels() {
        return pixels;
    }
}
