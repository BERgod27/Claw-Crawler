package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.elements.ShopKeeper;

public class ShopKeeperViewer implements ElementViewer<ShopKeeper> {
    @Override
    public void draw(ShopKeeper shopKeeper, GUI gui) {
        gui.drawShopKeeper(shopKeeper.getPosition());
    }
}
