package com.aor.player.viewer;

import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.elements.Wall;
import com.aor.player.viewer.game.GameViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

public class DungeonViewerTest {
    private GameViewer viewer;
    private GUI gui;
    private Dungeon dungeon;

    @BeforeEach
    void setUp() {
        dungeon = Mockito.mock(Dungeon.class);
        gui = Mockito.mock(GUI.class);
        var controller = Mockito.mock(com.aor.player.controller.game.DungeonController.class);
        viewer = new GameViewer(dungeon, controller);

        // Mock dungeon elements
        Mockito.when(dungeon.getWalls()).thenReturn(Arrays.asList(new Wall(1, 2), new Wall(2, 3)));
        Mockito.when(dungeon.getMonsters())
                .thenReturn(Arrays.asList(com.aor.player.model.game.elements.MonsterFactory.createTrollKing(4, 5)));
        Mockito.when(dungeon.getPlayer()).thenReturn(new Player(5, 8));
    }

}
