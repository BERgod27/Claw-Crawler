package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.game.ArenaController;
import com.aor.player.model.game.arena.Arena;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.game.GameViewer;

public class GameState extends State<Arena> {

    public GameState(Arena arena) {
        super(arena);
    }


    @Override
    protected Viewer<Arena> getViewer() {
        return new GameViewer(getModel());
    }

    @Override
    protected Controller<Arena> getController() {
        return new ArenaController(getModel());
    }

}

