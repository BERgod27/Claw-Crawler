package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.Sprite;
import com.aor.player.model.game.elements.ShopKeeper;
import com.aor.player.util.SpriteLoader;

import java.io.IOException;

public class ShopKeeperViewer implements ElementViewer<ShopKeeper> {
    private static Sprite shopSprite;

    public ShopKeeperViewer() {
        if (shopSprite == null) {
            try {
                shopSprite = SpriteLoader.getInstance().loadSprite("sprites/Shop.png");
            } catch (IOException e) {
                // Sprite not found, will use fallback rendering
                shopSprite = null;
            }
        }
    }

    @Override
    public void draw(ShopKeeper shopKeeper, GUI gui) {
        if (shopSprite != null) {
            gui.drawSprite(shopKeeper.getPosition(), shopSprite);
        } else {
            // Fallback to character if sprite fails to load
            gui.drawShopKeeper(shopKeeper.getPosition());
        }
    }
}
