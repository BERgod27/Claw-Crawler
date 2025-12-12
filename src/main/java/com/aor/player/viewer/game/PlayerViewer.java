package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.Sprite;
import com.aor.player.model.game.elements.Player;
import com.aor.player.util.SpriteLoader;

import java.io.IOException;

public class PlayerViewer implements ElementViewer<Player> {
    private static Sprite playerSprite;

    static {
        try {
            playerSprite = SpriteLoader.getInstance().loadSprite("sprites/characters/heroNude.png");
        } catch (IOException e) {
            System.err.println("Failed to load player sprite: " + e.getMessage());
            playerSprite = null;
        }
    }

    @Override
    public void draw(Player player, GUI gui) {
        if (playerSprite != null) {
            gui.drawSprite(player.getPosition(), playerSprite);
        } else {
            // Fallback to default character if sprite fails to load
            gui.drawPlayer(player.getPosition());
        }
    }
}
