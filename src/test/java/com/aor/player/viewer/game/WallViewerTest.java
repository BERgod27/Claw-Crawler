package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.elements.Wall;
import com.aor.player.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class WallViewerTest {

    private Wall mockWall;
    private GUI mockGUI;
    private WallViewer wallViewer;

    @BeforeEach
    void setUp() {
        mockWall = mock(Wall.class);
        mockGUI = mock(GUI.class);

        when(mockWall.getPosition()).thenReturn(new Position(5, 5));

        wallViewer = new WallViewer();
    }

    @Test
    void testDrawWall() {
        wallViewer.draw(mockWall, mockGUI);

        verify(mockGUI).drawWall(any());
    }

    @Test
    void testDrawMultipleWalls() {
        Wall wall2 = mock(Wall.class);
        when(wall2.getPosition()).thenReturn(new Position(10, 10));

        wallViewer.draw(mockWall, mockGUI);
        wallViewer.draw(wall2, mockGUI);

        verify(mockGUI, times(2)).drawWall(any());
    }
}
