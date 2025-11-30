package com.aor.player.controller.game;

import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.Game;
import com.aor.player.model.game.Player;
import java.io.IOException;

public class PlayerController implements Controller<Player> {

    private final Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
    }
}
