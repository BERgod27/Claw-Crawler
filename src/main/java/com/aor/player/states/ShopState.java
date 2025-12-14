package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.shop.ShopController;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.shop.Shop;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.shop.ShopMenuViewer;

public class ShopState extends State<Shop> {
    private final Dungeon dungeon;
    private final ShopController controller;
    private final ShopMenuViewer viewer;
    private final StateFactory stateFactory;

    public ShopState(Shop shop, Dungeon dungeon, StateFactory stateFactory) {
        super(shop);
        this.dungeon = dungeon;
        this.stateFactory = stateFactory;
        this.controller = new ShopController(dungeon, stateFactory);
        this.viewer = new ShopMenuViewer(shop);
    }

    @Override
    public Viewer<Shop> getViewer() {
        // Update viewer with current controller state
        viewer.setSelectedIndex(controller.getSelectedIndex());
        viewer.setPlayerGold(dungeon.getPlayer().getGold());
        return viewer;
    }

    @Override
    public Controller<Shop> getController() {
        return controller;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }
}
