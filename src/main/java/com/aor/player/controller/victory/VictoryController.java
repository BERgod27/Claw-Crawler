package com.aor.player.controller.victory;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.victory.Victory;
import com.aor.player.states.StateFactory;

import java.io.IOException;

public class VictoryController extends Controller<Victory> {
    private final StateFactory stateFactory;

    public VictoryController(Victory victory, StateFactory stateFactory) {
        super(victory);
        this.stateFactory = stateFactory;
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        switch (action) {
            case SELECT:
            case QUIT:
                // Both ENTER and Q go to main menu for new game
                game.setState(stateFactory.createMenuState());
                break;
            default:
                break;
        }
    }
}
