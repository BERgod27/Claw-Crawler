package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.Sprite;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.MonsterType;
import com.aor.player.util.SpriteLoader;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Viewer for rendering monsters with sprites.
 * Loads sprites based on MonsterType and caches them for efficient rendering.
 * Falls back to text-based rendering if sprites fail to load.
 */
public class MonsterViewer implements ElementViewer<Monster> {
    private static final Map<MonsterType, Sprite> monsterSprites = new EnumMap<>(MonsterType.class);

    static {
        // Load all monster sprites during class initialization
        for (MonsterType type : MonsterType.values()) {
            try {
                Sprite sprite = SpriteLoader.getInstance().loadSprite(type.getSpritePath());
                monsterSprites.put(type, sprite);
            } catch (IOException e) {
                System.err.println("Failed to load sprite for " + type.getDisplayName() + ": " + e.getMessage());
                monsterSprites.put(type, null); // Store null to indicate sprite loading failed
            }
        }
    }

    /**
     * Gets the cached sprite for a specific monster type.
     * Used by Monster class to retrieve sprite dimensions.
     * 
     * @param type Monster type
     * @return Sprite for the type, or null if not loaded
     */
    public static Sprite getSpriteForType(MonsterType type) {
        return monsterSprites.get(type);
    }

    @Override
    public void draw(Monster monster, GUI gui) {
        Sprite sprite = monsterSprites.get(monster.getType());

        if (sprite != null) {
            // Render using sprite
            gui.drawSprite(monster.getPosition(), sprite);
        } else {
            // Fallback to text-based rendering
            gui.drawMonster(monster.getPosition());
        }
    }
}