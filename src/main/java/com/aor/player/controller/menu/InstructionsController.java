package com.aor.player.controller.menu;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.menu.Instructions;
import com.aor.player.states.StateFactory;

import java.io.IOException;

public class InstructionsController extends Controller<Instructions> {
    private final StateFactory stateFactory;

    public InstructionsController(Instructions instructions, StateFactory stateFactory) {
        super(instructions);
        this.stateFactory = stateFactory;
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        if (action == GUI.ACTION.QUIT) {
            game.setState(stateFactory.createMenuState());
        }
    }
}
