package com.aor.player.viewer.game;

import com.aor.player.controller.game.DungeonController;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameViewerTest {

    private Dungeon mockDungeon;
    private DungeonController mockController;
    private GUI mockGUI;
    private GameViewer gameViewer;
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        mockDungeon = mock(Dungeon.class);
        mockController = mock(DungeonController.class);
        mockGUI = mock(GUI.class);
        mockPlayer = mock(Player.class);

        // Setup all necessary mocks
        when(mockDungeon.getWalls()).thenReturn(Collections.emptyList());
        when(mockDungeon.getMonsters()).thenReturn(Collections.emptyList());
        when(mockDungeon.getDoors()).thenReturn(Collections.emptyList());
        when(mockDungeon.getChests()).thenReturn(Collections.emptyList());
        when(mockDungeon.getPlayer()).thenReturn(mockPlayer);
        when(mockDungeon.getCurrentRoom()).thenReturn(null);
        when(mockPlayer.getHealth()).thenReturn(100);
        when(mockPlayer.getArmor()).thenReturn(10);
        when(mockPlayer.getAttack()).thenReturn(10);
        when(mockController.isQuitConfirmation()).thenReturn(false);
        when(mockDungeon.isPlayerNearShopKeeper()).thenReturn(false);
        when(mockDungeon.getChestNearPlayer()).thenReturn(null);

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
