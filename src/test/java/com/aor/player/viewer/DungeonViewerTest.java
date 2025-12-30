package com.aor.player.viewer;

import com.aor.player.controller.game.DungeonController;
import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.elements.Wall;
import com.aor.player.model.game.elements.MonsterType;
import com.aor.player.viewer.game.GameViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;

public class DungeonViewerTest {
    private GameViewer viewer;
    private GUI gui;
    private Dungeon dungeon;

    @BeforeEach
    void setUp() {
        dungeon = Mockito.mock(Dungeon.class);
        gui = Mockito.mock(GUI.class);
        DungeonController controller = Mockito.mock(DungeonController.class);
        viewer = new GameViewer(dungeon, controller);

        // Mock dungeon elements
        Mockito.when(dungeon.getWalls()).thenReturn(Arrays.asList(new Wall(1, 2), new Wall(2, 3)));
        Mockito.when(dungeon.getMonsters()).thenReturn(Arrays.asList(new Monster(4, 5, MonsterType.SLIME)));
        Mockito.when(dungeon.getPlayer()).thenReturn(new Player(5, 8));
        Mockito.when(dungeon.getDoors()).thenReturn(List.of()); // Empty for now
        Mockito.when(dungeon.getChests()).thenReturn(List.of()); // Empty for now
    }

    @Test
    void testDrawWalls() throws IOException {
        viewer.draw(gui);

        verify(gui).drawWall(new Position(1, 2));
        verify(gui).drawWall(new Position(2, 3));
    }

    @Test
    void testDrawMonsters() throws IOException {
        viewer.draw(gui);
        verify(gui, Mockito.atLeastOnce()).drawSprite(Mockito.eq(new Position(4, 5)), Mockito.any());
    }

    @Test
    void testDrawPlayer() throws IOException {
        viewer.draw(gui);
        verify(gui, Mockito.atLeastOnce()).drawSprite(Mockito.eq(new Position(5, 8)), Mockito.any());
    }
}
