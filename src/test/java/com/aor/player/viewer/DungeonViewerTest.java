package com.aor.player.viewer;

import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
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
        viewer = new GameViewer(dungeon);

        // Mock dungeon elements
        Mockito.when(dungeon.getWalls()).thenReturn(Arrays.asList(new Wall(1, 2), new Wall(2, 3)));
        Mockito.when(dungeon.getMonsters()).thenReturn(Arrays.asList(new Monster(4, 5)));
        Mockito.when(dungeon.getPlayer()).thenReturn(new Player(5, 8));
    }

    @Test
    void drawElements() {
        viewer.drawElements(gui);

        // Verify walls are drawn
        Mockito.verify(gui, Mockito.times(1)).drawWall(new Position(1, 2));
        Mockito.verify(gui, Mockito.times(1)).drawWall(new Position(2, 3));

        // Verify monsters are drawn
        Mockito.verify(gui, Mockito.times(1)).drawMonster(new Position(4, 5));

        // Verify player is drawn with sprite
        Mockito.verify(gui, Mockito.times(1)).drawSprite(
                Mockito.eq(new Position(5, 8)),
                Mockito.any());

        // Verify stats are drawn
        Mockito.verify(gui, Mockito.times(1)).drawText(new Position(1, 21), "Health: 10", "#FF0000");
        Mockito.verify(gui, Mockito.times(1)).drawText(new Position(1, 22), "Armor: 10", "#00FFFF");
        Mockito.verify(gui, Mockito.times(1)).drawText(new Position(1, 23), "Speed: 10", "#00FF00");
    }
}
