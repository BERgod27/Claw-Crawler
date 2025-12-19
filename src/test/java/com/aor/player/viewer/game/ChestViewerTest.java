package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.elements.Chest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ChestViewerTest {

    private Chest mockChest;
    private GUI mockGUI;
    private ChestViewer chestViewer;

    @BeforeEach
    void setUp() {
        mockChest = mock(Chest.class);
        mockGUI = mock(GUI.class);

        when(mockChest.getPosition()).thenReturn(new com.aor.player.model.Position(4, 6));
        when(mockChest.isOpened()).thenReturn(false);

        chestViewer = new ChestViewer();
    }

    @Test
    void testDrawClosedChest() {
        when(mockChest.isOpened()).thenReturn(false);

        chestViewer.draw(mockChest, mockGUI);

        verify(mockGUI).drawSprite(any(), any());
    }

    @Test
    void testDrawOpenedChest() {
        when(mockChest.isOpened()).thenReturn(true);

        chestViewer.draw(mockChest, mockGUI);

        verify(mockGUI).drawSprite(any(), any());
    }

    @Test
    void testDrawMultipleChests() {
        Chest chest2 = mock(Chest.class);
        when(chest2.getPosition()).thenReturn(new com.aor.player.model.Position(9, 11));
        when(chest2.isOpened()).thenReturn(true);

        chestViewer.draw(mockChest, mockGUI);
        chestViewer.draw(chest2, mockGUI);

        verify(mockGUI, times(2)).drawSprite(any(), any());
    }
}
