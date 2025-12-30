package com.aor.player.util;

import com.aor.player.model.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SpriteLoaderTest {
    private SpriteLoader loader;

    @BeforeEach
    void setUp() {
        loader = SpriteLoader.getInstance();
        loader.clearCache();
    }

    @Test
    void testSingleton() {
        SpriteLoader loader1 = SpriteLoader.getInstance();
        SpriteLoader loader2 = SpriteLoader.getInstance();
        assertSame(loader1, loader2);
    }

    @Test
    void testLoadSpriteValid() throws IOException {
        String resourcePath = "sprites/Shop.png";
        Sprite sprite = loader.loadSprite(resourcePath);

        assertNotNull(sprite);
        assertTrue(sprite.getWidth() > 0);
        assertTrue(sprite.getHeight() > 0);
        assertNotNull(sprite.getPixels());
    }

    @Test
    void testLoadSpriteCaching() throws IOException {
        String resourcePath = "sprites/Shop.png";
        Sprite sprite1 = loader.loadSprite(resourcePath);
        Sprite sprite2 = loader.loadSprite(resourcePath);

        // Should return the exact same object reference
        assertSame(sprite1, sprite2);
    }

    @Test
    void testLoadSpriteInvalid() {
        String resourcePath = "sprites/nonexistent.png";
        assertThrows(IOException.class, () -> loader.loadSprite(resourcePath));
    }

    @Test
    void testClearCache() throws IOException {
        String resourcePath = "sprites/Shop.png";
        Sprite sprite1 = loader.loadSprite(resourcePath);

        loader.clearCache();

        Sprite sprite2 = loader.loadSprite(resourcePath);

        // After clearing cache, new object should be created
        assertNotSame(sprite1, sprite2);
        // But content should be similar (not checking content equality here, just
        // identity)
    }
}
