package com.aor.player.controller.game;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;

import java.io.IOException;

public class DungeonController extends Controller<Dungeon> {
    public DungeonController(Dungeon dungeon) {
        super(dungeon);
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        if (action == GUI.ACTION.UP)
            movePlayerUp();
        if (action == GUI.ACTION.RIGHT)
            movePlayerRight();
        if (action == GUI.ACTION.DOWN)
            movePlayerDown();
        if (action == GUI.ACTION.LEFT)
            movePlayerLeft();
        if (action == GUI.ACTION.QUIT)
            game.setState(null);
    }

    public void movePlayerLeft() {
        movePlayer(getModel().getPlayer().getPosition().getLeft());
    }

    public void movePlayerRight() {
        movePlayer(getModel().getPlayer().getPosition().getRight());
    }

    public void movePlayerUp() {
        movePlayer(getModel().getPlayer().getPosition().getUp());
    }

    public void movePlayerDown() {
        movePlayer(getModel().getPlayer().getPosition().getDown());
    }

    private void movePlayer(Position position) {
        if (getModel().isEmpty(position)) {
            getModel().getPlayer().setPosition(position);
            if (getModel().isMonster(position))
                getModel().getPlayer().decreaseEnergy();
        }
    }
}
