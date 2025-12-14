package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.inventory.InventoryController;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.inventory.InventoryViewer;

public class InventoryState extends State<Dungeon> {
    private final InventoryController controller;
    private final InventoryViewer viewer;
    private final StateFactory stateFactory;

    public InventoryState(Dungeon dungeon, StateFactory stateFactory) {
        super(dungeon);
        this.stateFactory = stateFactory;
        this.controller = new InventoryController(dungeon, stateFactory);
        this.viewer = new InventoryViewer(dungeon);
    }

    @Override
    public Viewer<Dungeon> getViewer() {
        // Update viewer with current controller state
        viewer.setSelectedIndex(controller.getSelectedIndex());
        return viewer;
    }

    @Override
    public Controller<Dungeon> getController() {
        return controller;
    }
}
