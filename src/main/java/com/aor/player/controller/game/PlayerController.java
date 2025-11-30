package com.aor.player.controller.game;

import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.elements.Player;
import com.aor.player.Game;
import java.io.IOException;

public class PlayerController extends Controller<Player> {

    public PlayerController(Player player) {
        super(player);
    }

    public void step(Game game, GUI.ACTION action, long time) throws IOException {
    }
}
