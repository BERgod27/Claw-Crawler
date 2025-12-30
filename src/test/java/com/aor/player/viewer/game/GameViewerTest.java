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
        verify(mockGUI).drawText(eq(new Position(2, 36)), contains("HP: 100"), eq("#FFD700"));
    }

    @Test
    void testDrawQuitConfirmation() throws IOException {
        when(mockController.isQuitConfirmation()).thenReturn(true);
        gameViewer.draw(mockGUI);
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

    @Test
    void testDrawWithShopKeeper() throws IOException {
        com.aor.player.model.game.dungeon.Room mockRoom = mock(com.aor.player.model.game.dungeon.Room.class);
        com.aor.player.model.game.elements.ShopKeeper mockShopKeeper = mock(
                com.aor.player.model.game.elements.ShopKeeper.class);
        when(mockDungeon.getCurrentRoom()).thenReturn(mockRoom);
        when(mockRoom.getShopKeeper()).thenReturn(mockShopKeeper);
        when(mockShopKeeper.getPosition()).thenReturn(new Position(10, 10));
        assertDoesNotThrow(() -> gameViewer.draw(mockGUI));
    }

    @Test
    void testDrawWithWalls() throws IOException {
        com.aor.player.model.game.elements.Wall mockWall = mock(com.aor.player.model.game.elements.Wall.class);
        when(mockWall.getPosition()).thenReturn(new Position(5, 5));
        when(mockDungeon.getWalls()).thenReturn(java.util.List.of(mockWall));
        assertDoesNotThrow(() -> gameViewer.draw(mockGUI));
    }

    @Test
    void testDrawWithMonsters() throws IOException {
        com.aor.player.model.game.elements.Monster mockMonster = mock(com.aor.player.model.game.elements.Monster.class);
        when(mockMonster.getPosition()).thenReturn(new Position(20, 20));
        when(mockMonster.getType()).thenReturn(com.aor.player.model.game.elements.MonsterType.SLIME);
        when(mockDungeon.getMonsters()).thenReturn(java.util.List.of(mockMonster));
        assertDoesNotThrow(() -> gameViewer.draw(mockGUI));
    }

    @Test
    void testDrawWithDoors() throws IOException {
        com.aor.player.model.game.elements.Door mockDoor = mock(com.aor.player.model.game.elements.Door.class);
        when(mockDoor.getPosition()).thenReturn(new Position(30, 30));
        when(mockDungeon.getDoors()).thenReturn(java.util.List.of(mockDoor));
        assertDoesNotThrow(() -> gameViewer.draw(mockGUI));
    }

    @Test
    void testDrawWithChests() throws IOException {
        com.aor.player.model.game.elements.Chest mockChest = mock(com.aor.player.model.game.elements.Chest.class);
        when(mockChest.getPosition()).thenReturn(new Position(40, 40));
        when(mockChest.isOpened()).thenReturn(false);
        when(mockDungeon.getChests()).thenReturn(java.util.List.of(mockChest));
        assertDoesNotThrow(() -> gameViewer.draw(mockGUI));
    }

    @Test
    void testDrawWithMultipleChests() throws IOException {
        com.aor.player.model.game.elements.Chest chest1 = mock(com.aor.player.model.game.elements.Chest.class);
        com.aor.player.model.game.elements.Chest chest2 = mock(com.aor.player.model.game.elements.Chest.class);
        when(chest1.getPosition()).thenReturn(new Position(10, 10));
        when(chest2.getPosition()).thenReturn(new Position(20, 10));
        when(chest1.isOpened()).thenReturn(false);
        when(chest2.isOpened()).thenReturn(true);
        when(mockDungeon.getChests()).thenReturn(java.util.List.of(chest1, chest2));
        assertDoesNotThrow(() -> gameViewer.draw(mockGUI));
    }

    @Test
    void testDrawNoPromptWhenNotNearAnything() throws IOException {
        when(mockDungeon.isPlayerNearShopKeeper()).thenReturn(false);
        when(mockDungeon.getChestNearPlayer()).thenReturn(null);
        when(mockController.isQuitConfirmation()).thenReturn(false);
        gameViewer.draw(mockGUI);
        verify(mockGUI, never()).drawText(eq(new Position(2, 37)), any(), any());
    }
}
