package com.aor.player.controller.instructions;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.instructions.Instructions;
import com.aor.player.model.menu.Menu;
import com.aor.player.states.MenuState;

import java.io.IOException;

public class InstructionsController extends Controller<Instructions> {
    public InstructionsController(Instructions instructions) {
        super(instructions);
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        // Any key press returns to menu
        if (action != GUI.ACTION.NONE) {
            game.setState(new MenuState(new Menu()));
        }
    }
}
