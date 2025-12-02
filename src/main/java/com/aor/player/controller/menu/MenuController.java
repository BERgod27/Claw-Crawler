package com.aor.player.controller.menu;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.DungeonFactory;
import com.aor.player.model.menu.Instructions;
import com.aor.player.model.menu.Menu;
import com.aor.player.states.GameState;
import com.aor.player.states.InstructionsState;

import java.io.IOException;

public class MenuController extends Controller<Menu> {
    private final DungeonFactory dungeonFactory;

    public MenuController(Menu menu, DungeonFactory dungeonFactory) {
        super(menu);
        this.dungeonFactory = dungeonFactory;
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        switch (action) {
            case UP:
                getModel().previousEntry();
                break;
            case DOWN:
                getModel().nextEntry();
                break;
            case SELECT:
                if (getModel().isSelectedExit())
                    game.setState(null);
                if (getModel().isSelectedStart())
                    game.setState(new GameState(dungeonFactory.createDungeon()));
                if (getModel().isSelectedInstructions())
                    game.setState(new InstructionsState(new Instructions()));
                break;
            case QUIT:
                game.setState(null);
                break;
            case LEFT:
            case RIGHT:
            case NONE:
            default:
                // No action needed for these cases
                break;
        }
    }
}
