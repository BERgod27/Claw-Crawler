package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.game.DungeonController;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.game.GameViewer;

public class GameState extends State<Dungeon> {
    private final GameViewer viewer;
    private final DungeonController controller;

    public GameState(Dungeon dungeon) {
        super(dungeon);
        this.viewer = new GameViewer(dungeon);
        this.controller = new DungeonController(dungeon);
    }

    @Override
    protected Viewer<Dungeon> getViewer() {
        return viewer;
    }

    @Override
    protected Controller<Dungeon> getController() {
        return controller;
    }

}
