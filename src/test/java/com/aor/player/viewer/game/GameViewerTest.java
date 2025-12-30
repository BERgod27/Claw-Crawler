package com.aor.player.viewer.game;

import com.aor.player.controller.game.DungeonController;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.Position;
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
    void testDrawStats() throws IOException {
        gameViewer.draw(mockGUI);

        // Stats drawn at row 36 with gold color
        verify(mockGUI).drawText(eq(new Position(2, 36)), contains("HP: 100"), eq("#FFD700"));
    }

    @Test
    void testDrawQuitConfirmation() throws IOException {
        when(mockController.isQuitConfirmation()).thenReturn(true);

        gameViewer.draw(mockGUI);

        // Quit prompt at row 37 with red color
        verify(mockGUI).drawText(eq(new Position(2, 37)), contains("Press Q again to quit"), eq("#FF0000"));
    }

    @Test
    void testDrawShopPrompt() throws IOException {
        when(mockDungeon.isPlayerNearShopKeeper()).thenReturn(true);

        gameViewer.draw(mockGUI);

        verify(mockGUI).drawText(eq(new Position(2, 37)), contains("Press ENTER to open shop"), eq("#00FF00"));
    }

    @Test
    void testDrawChestPrompt() throws IOException {
        when(mockDungeon.getChestNearPlayer()).thenReturn(mock(com.aor.player.model.game.elements.Chest.class));

        gameViewer.draw(mockGUI);

        verify(mockGUI).drawText(eq(new Position(2, 37)), contains("Press ENTER to open chest"), eq("#00FF00"));
    }

    @Test
    void testDrawBothPrompts() throws IOException {
        when(mockDungeon.isPlayerNearShopKeeper()).thenReturn(true);
        when(mockDungeon.getChestNearPlayer()).thenReturn(mock(com.aor.player.model.game.elements.Chest.class));

        gameViewer.draw(mockGUI);

        verify(mockGUI).drawText(eq(new Position(2, 37)), contains("open shop | open chest"), eq("#00FF00"));
    }
}
