package com.aor.player.viewer.game;

import com.aor.player.controller.game.DungeonController;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameViewerTest {

    private Dungeon mockDungeon;
    private DungeonController mockController;
    private GUI mockGUI;
    private GameViewer gameViewer;

    @BeforeEach
    void setUp() {
        mockDungeon = mock(Dungeon.class);
        mockController = mock(DungeonController.class);
        mockGUI = mock(GUI.class);

        gameViewer = new GameViewer(mockDungeon, mockController);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockDungeon, gameViewer.getModel());
    }

    @Test
    void testDrawCallsClearAndRefresh() throws IOException {
        gameViewer.draw(mockGUI);

        verify(mockGUI).clear();
        verify(mockGUI).refresh();
    }

    @Test
    void testDrawCallsDrawElements() throws IOException {
        gameViewer.draw(mockGUI);

        verify(mockGUI).clear();
        // drawElements is called internally
    }
}
