package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.Sprite;
import com.aor.player.model.game.elements.Chest;
import com.aor.player.util.SpriteLoader;

import java.io.IOException;

public class ChestViewer {
    private static Sprite chestSprite;

    public ChestViewer() {
        if (chestSprite == null) {
            try {
                chestSprite = SpriteLoader.getInstance().loadSprite("sprites/chest.png");
            } catch (IOException e) {
                // Sprite not found, will use fallback rendering
                chestSprite = null;
            }
        }
    }

    public void draw(Chest chest, GUI gui) {
        if (chestSprite != null) {
            gui.drawChest(chest.getPosition(), chestSprite, chest.isOpened());
        } else {
            // Fallback: use text characters if sprite doesn't exist
            gui.drawText(chest.getPosition(), chest.isOpened() ? "□" : "■",
                    chest.isOpened() ? "#808080" : "#DAA520");
        }
    }
}
