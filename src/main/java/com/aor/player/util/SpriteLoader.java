package com.aor.player.util;

import com.aor.player.model.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SpriteLoader {
    private static SpriteLoader instance;
    private final Map<String, Sprite> spriteCache;

    private SpriteLoader() {
        this.spriteCache = new HashMap<>();
    }

    public static SpriteLoader getInstance() {
        if (instance == null) {
            instance = new SpriteLoader();
        }
        return instance;
    }

    public Sprite loadSprite(String resourcePath) throws IOException {
        // Check cache first
        if (spriteCache.containsKey(resourcePath)) {
            return spriteCache.get(resourcePath);
        }

        // Load image from resources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Could not find resource: " + resourcePath);
        }

        BufferedImage image = ImageIO.read(inputStream);
        inputStream.close();

        // Convert BufferedImage to Sprite
        int width = image.getWidth();
        int height = image.getHeight();
        Color[][] pixels = new Color[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                pixels[y][x] = new Color(rgb, true); // true for alpha channel
            }
        }

        Sprite sprite = new Sprite(width, height, pixels);
        spriteCache.put(resourcePath, sprite);
        return sprite;
    }

    public void clearCache() {
        spriteCache.clear();
    }
}
