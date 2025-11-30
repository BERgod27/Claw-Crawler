package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.game.DungeonController;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.game.GameViewer;

public class GameState extends State<Dungeon> {
    public GameState(Dungeon dungeon) {
        super(dungeon);
    }

    @Override
    protected Viewer<Dungeon> getViewer() {
        return new GameViewer(getModel());
    }

    @Override
    protected Controller<Dungeon> getController() {
        return new DungeonController(getModel());
    }

}
