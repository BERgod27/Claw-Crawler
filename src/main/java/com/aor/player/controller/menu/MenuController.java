package com.aor.player.controller.menu;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.elements.Wall;
import com.aor.player.model.menu.Menu;
import com.aor.player.states.GameState;

import java.io.IOException;
import java.util.ArrayList;

public class MenuController extends Controller<Menu> {
    public MenuController(Menu menu) {
        super(menu);
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
                    game.setState(new GameState(createDungeon()));
                break;
        }
    }

    private Dungeon createDungeon() {
        Dungeon dungeon = new Dungeon(20, 20);

        // Create player at center
        dungeon.setPlayer(new Player(10, 10));

        // Create walls around the dungeon perimeter
        ArrayList<Wall> walls = new ArrayList<>();
        for (int x = 0; x < 20; x++) {
            walls.add(new Wall(x, 0)); // Top wall
            walls.add(new Wall(x, 19)); // Bottom wall
        }
        for (int y = 1; y < 19; y++) {
            walls.add(new Wall(0, y)); // Left wall
            walls.add(new Wall(19, y)); // Right wall
        }
        dungeon.setWalls(walls);

        // Initialize empty list for monsters
        dungeon.setMonsters(new ArrayList<>());

        return dungeon;
    }
}
