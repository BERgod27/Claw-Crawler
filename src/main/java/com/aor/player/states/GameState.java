package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.game.DungeonController;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.game.GameViewer;

<<<<<<< HEAD
public class GameState extends State<Dungeon> {
    public GameState(Dungeon dungeon) {
        super(dungeon);
=======
public class GameState extends State<Arena> {

    public GameState(Arena arena) {
        super(arena);
>>>>>>> 5c41a6de3b3a2a22ee4101e75cfd985f559a5926
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

