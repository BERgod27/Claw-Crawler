package com.aor.player.gui;

import com.aor.player.model.Position;
import com.aor.player.model.Sprite;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LanternaGUITest {
    private Screen mockScreen;
    private TextGraphics mockGraphics;
    private LanternaGUI gui;

    @BeforeEach
    void setUp() {
        mockScreen = mock(Screen.class);
        mockGraphics = mock(TextGraphics.class);
        when(mockScreen.newTextGraphics()).thenReturn(mockGraphics);
        when(mockGraphics.setForegroundColor(any(TextColor.class))).thenReturn(mockGraphics);
        when(mockGraphics.setBackgroundColor(any(TextColor.class))).thenReturn(mockGraphics);
        gui = new LanternaGUI(mockScreen);
    }

    // ========== Constructor Tests ==========

    @Test
    void testConstructorWithScreen() throws IOException {
        LanternaGUI testGui = new LanternaGUI(mockScreen);
        when(mockScreen.pollInput()).thenReturn(null);
        assertEquals(GUI.ACTION.NONE, testGui.getNextAction());
    }

    // ========== Input Handling Tests ==========

    @Test
    void testGetNextActionReturnsUpForArrowUp() throws IOException {
        KeyStroke upKey = new KeyStroke(KeyType.ArrowUp);
        when(mockScreen.pollInput()).thenReturn(upKey);

        GUI.ACTION action = gui.getNextAction();

        assertEquals(GUI.ACTION.UP, action);
    }

    @Test
    void testGetNextActionReturnsDownForArrowDown() throws IOException {
        KeyStroke downKey = new KeyStroke(KeyType.ArrowDown);
        when(mockScreen.pollInput()).thenReturn(downKey);

        GUI.ACTION action = gui.getNextAction();

        assertEquals(GUI.ACTION.DOWN, action);
    }

    @Test
    void testGetNextActionReturnsLeftForArrowLeft() throws IOException {
        KeyStroke leftKey = new KeyStroke(KeyType.ArrowLeft);
        when(mockScreen.pollInput()).thenReturn(leftKey);

        GUI.ACTION action = gui.getNextAction();

        assertEquals(GUI.ACTION.LEFT, action);
    }

    @Test
    void testGetNextActionReturnsRightForArrowRight() throws IOException {
        KeyStroke rightKey = new KeyStroke(KeyType.ArrowRight);
        when(mockScreen.pollInput()).thenReturn(rightKey);

        GUI.ACTION action = gui.getNextAction();

        assertEquals(GUI.ACTION.RIGHT, action);
    }

    @Test
    void testGetNextActionReturnsSelectForEnter() throws IOException {
        KeyStroke enterKey = new KeyStroke(KeyType.Enter);
        when(mockScreen.pollInput()).thenReturn(enterKey);

        GUI.ACTION action = gui.getNextAction();

        assertEquals(GUI.ACTION.SELECT, action);
    }

    @Test
    void testGetNextActionReturnsQuitForQCharacter() throws IOException {
        KeyStroke qKey = new KeyStroke('q', false, false);
        when(mockScreen.pollInput()).thenReturn(qKey);

        GUI.ACTION action = gui.getNextAction();

        assertEquals(GUI.ACTION.QUIT, action);
    }

    @Test
    void testGetNextActionReturnsInventoryForICharacter() throws IOException {
        KeyStroke iKey = new KeyStroke('i', false, false);
        when(mockScreen.pollInput()).thenReturn(iKey);

        GUI.ACTION action = gui.getNextAction();

        assertEquals(GUI.ACTION.INVENTORY, action);
    }

    @Test
    void testGetNextActionReturnsQuitForEOF() throws IOException {
        KeyStroke eofKey = new KeyStroke(KeyType.EOF);
        when(mockScreen.pollInput()).thenReturn(eofKey);

        GUI.ACTION action = gui.getNextAction();

        assertEquals(GUI.ACTION.QUIT, action);
    }

    @Test
    void testGetNextActionReturnsNoneForNoInput() throws IOException {
        when(mockScreen.pollInput()).thenReturn(null);

        GUI.ACTION action = gui.getNextAction();

        assertEquals(GUI.ACTION.NONE, action);
    }

    @Test
    void testGetNextActionReturnsNoneForUnknownKey() throws IOException {
        KeyStroke unknownKey = new KeyStroke('x', false, false);
        when(mockScreen.pollInput()).thenReturn(unknownKey);

        GUI.ACTION action = gui.getNextAction();

        assertEquals(GUI.ACTION.NONE, action);
    }

    // ========== Drawing Tests ==========

    @Test
    void testDrawPlayerDrawsAtCorrectPosition() {
        Position position = new Position(10, 5);

        gui.drawPlayer(position);

        verify(mockScreen).newTextGraphics();
        verify(mockGraphics).putString(10, 5, "P");
    }

    @Test
    void testDrawPlayerUsesGoldColor() {
        Position position = new Position(1, 1);

        gui.drawPlayer(position);

        verify(mockGraphics).setForegroundColor(new TextColor.RGB(255, 215, 0));
        verify(mockGraphics).putString(1, 1, "P");
    }

    @Test
    void testDrawWallDrawsAtCorrectPosition() {
        Position position = new Position(15, 8);

        gui.drawWall(position);

        verify(mockScreen).newTextGraphics();
        verify(mockGraphics).putString(15, 8, "█");
    }

    @Test
    void testDrawWallUsesCorrectColors() {
        Position position = new Position(1, 1);

        gui.drawWall(position);

        verify(mockGraphics).setForegroundColor(new TextColor.RGB(139, 139, 139));
        verify(mockGraphics).setBackgroundColor(new TextColor.RGB(45, 45, 45));
        verify(mockGraphics).putString(1, 1, "█");
    }

    @Test
    void testDrawMonsterDrawsAtCorrectPosition() {
        Position position = new Position(20, 12);

        gui.drawMonster(position);

        verify(mockScreen).newTextGraphics();
        verify(mockGraphics).putString(20, 12, "M");
    }

    @Test
    void testDrawMonsterUsesCorrectColors() {
        Position position = new Position(1, 1);

        gui.drawMonster(position);

        verify(mockGraphics).setForegroundColor(new TextColor.RGB(255, 68, 68));
        verify(mockGraphics).setBackgroundColor(new TextColor.RGB(74, 0, 0));
        verify(mockGraphics).putString(1, 1, "M");
    }

    @Test
    void testDrawDoorDrawsAtCorrectPosition() {
        Position position = new Position(7, 3);

        gui.drawDoor(position);

        verify(mockScreen).newTextGraphics();
        verify(mockGraphics).putString(7, 3, "▓");
    }

    @Test
    void testDrawDoorUsesCorrectColors() {
        Position position = new Position(1, 1);

        gui.drawDoor(position);

        verify(mockGraphics).setForegroundColor(new TextColor.RGB(0, 255, 0));
        verify(mockGraphics).setBackgroundColor(new TextColor.RGB(0, 51, 0));
    }

    @Test
    void testDrawShopKeeperDrawsAtCorrectPosition() {
        Position position = new Position(25, 15);

        gui.drawShopKeeper(position);

        verify(mockScreen).newTextGraphics();
        verify(mockGraphics).putString(25, 15, "S");
    }

    @Test
    void testDrawShopKeeperUsesCorrectColors() {
        Position position = new Position(1, 1);

        gui.drawShopKeeper(position);

        verify(mockGraphics).setForegroundColor(new TextColor.RGB(255, 215, 0));
        verify(mockGraphics).setBackgroundColor(new TextColor.RGB(74, 56, 0));
    }

    @Test
    void testDrawTextWithPositionDrawsAtCorrectLocation() {
        Position position = new Position(5, 10);
        String text = "Hello World";
        String color = "#336699";

        gui.drawText(position, text, color);

        verify(mockScreen).newTextGraphics();
        verify(mockGraphics).setForegroundColor(new TextColor.RGB(51, 102, 153));
        verify(mockGraphics).putString(5, 10, text);
    }

    @Test
    void testDrawTextWithCoordinatesDrawsAtCorrectLocation() {
        int x = 12;
        int y = 7;
        String text = "Test Text";
        String color = "#FF0000";

        gui.drawText(x, y, text, color);

        verify(mockScreen).newTextGraphics();
        verify(mockGraphics).setForegroundColor(new TextColor.RGB(255, 0, 0));
        verify(mockGraphics).putString(x, y, text);
    }

    // ========== Sprite Drawing Tests ==========

    @Test
    void testDrawSpriteWithOpaquePixels() {
        Position position = new Position(5, 5);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(2);
        when(mockSprite.getHeight()).thenReturn(2);

        // Create opaque pixels (alpha > 128)
        Color opaqueColor = new Color(255, 0, 0, 200);
        when(mockSprite.getPixel(anyInt(), anyInt())).thenReturn(opaqueColor);

        gui.drawSprite(position, mockSprite);

        // Verify that putString was called for each pixel (2x2 = 4 times)
        verify(mockGraphics, times(4)).putString(anyInt(), anyInt(), eq("█"));
    }

    @Test
    void testDrawSpriteSkipsTransparentPixels() {
        Position position = new Position(0, 0);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(2);
        when(mockSprite.getHeight()).thenReturn(2);

        // Create transparent pixels (alpha <= 128)
        Color transparentColor = new Color(255, 0, 0, 100);
        when(mockSprite.getPixel(anyInt(), anyInt())).thenReturn(transparentColor);

        gui.drawSprite(position, mockSprite);

        // Verify that putString was NOT called (all pixels are transparent)
        verify(mockGraphics, never()).putString(anyInt(), anyInt(), anyString());
    }

    @Test
    void testDrawSpriteSkipsNullPixels() {
        Position position = new Position(0, 0);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(2);
        when(mockSprite.getHeight()).thenReturn(2);
        when(mockSprite.getPixel(anyInt(), anyInt())).thenReturn(null);

        gui.drawSprite(position, mockSprite);

        // Verify that putString was NOT called (all pixels are null)
        verify(mockGraphics, never()).putString(anyInt(), anyInt(), anyString());
    }

    @Test
    void testDrawSpriteUsesCorrectColorConversion() {
        Position position = new Position(0, 0);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(1);
        when(mockSprite.getHeight()).thenReturn(1);

        // Create a specific color (RGB: 255, 100, 50)
        Color specificColor = new Color(255, 100, 50, 255);
        when(mockSprite.getPixel(0, 0)).thenReturn(specificColor);

        gui.drawSprite(position, mockSprite);

        // Verify the correct RGB color was set
        verify(mockGraphics).setForegroundColor(new TextColor.RGB(255, 100, 50));
    }

    @Test
    void testDrawChestClosedUsesNormalColors() {
        Position position = new Position(10, 10);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(1);
        when(mockSprite.getHeight()).thenReturn(1);

        Color chestColor = new Color(200, 150, 100, 255);
        when(mockSprite.getPixel(0, 0)).thenReturn(chestColor);

        gui.drawChest(position, mockSprite, false);

        // Verify the normal color was used
        verify(mockGraphics).setForegroundColor(new TextColor.RGB(200, 150, 100));
    }

    @Test
    void testDrawChestOpenedUsesDarkenedColors() {
        Position position = new Position(10, 10);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(1);
        when(mockSprite.getHeight()).thenReturn(1);

        Color chestColor = new Color(200, 150, 100, 255);
        when(mockSprite.getPixel(0, 0)).thenReturn(chestColor);

        gui.drawChest(position, mockSprite, true);

        // When opened, colors are divided by 2: (200/2, 150/2, 100/2) = (100, 75, 50)
        verify(mockGraphics).setForegroundColor(new TextColor.RGB(100, 75, 50));
    }

    @Test
    void testDrawChestSkipsTransparentPixels() {
        Position position = new Position(0, 0);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(2);
        when(mockSprite.getHeight()).thenReturn(2);

        Color transparentColor = new Color(255, 0, 0, 50);
        when(mockSprite.getPixel(anyInt(), anyInt())).thenReturn(transparentColor);

        gui.drawChest(position, mockSprite, false);

        // Verify that putString was NOT called
        verify(mockGraphics, never()).putString(anyInt(), anyInt(), anyString());
    }

    @Test
    void testDrawChestAtBoundaryAlpha() {
        Position position = new Position(0, 0);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(1);
        when(mockSprite.getHeight()).thenReturn(1);

        // Alpha exactly at 128 should NOT be drawn (alpha > 128 is the condition)
        Color boundaryColor = new Color(255, 0, 0, 128);
        when(mockSprite.getPixel(0, 0)).thenReturn(boundaryColor);

        gui.drawChest(position, mockSprite, false);

        // Verify that putString was NOT called
        verify(mockGraphics, never()).putString(anyInt(), anyInt(), anyString());
    }

    @Test
    void testDrawSpriteAtBoundaryAlpha() {
        Position position = new Position(0, 0);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(1);
        when(mockSprite.getHeight()).thenReturn(1);

        // Alpha exactly at 128 should NOT be drawn (alpha > 128 is the condition)
        Color boundaryColor = new Color(255, 0, 0, 128);
        when(mockSprite.getPixel(0, 0)).thenReturn(boundaryColor);

        gui.drawSprite(position, mockSprite);

        // Verify that putString was NOT called
        verify(mockGraphics, never()).putString(anyInt(), anyInt(), anyString());
    }

    // ========== Screen Operation Tests ==========

    @Test
    void testClearCallsScreenClear() {
        gui.clear();

        verify(mockScreen).clear();
    }

    @Test
    void testRefreshCallsScreenRefresh() throws IOException {
        gui.refresh();

        verify(mockScreen).refresh();
    }

    @Test
    void testCloseCallsScreenClose() throws IOException {
        gui.close();

        verify(mockScreen).close();
    }

    // ========== Edge Case Tests ==========

    @Test
    void testMultipleDrawCallsCreateNewGraphics() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(5, 5);

        gui.drawPlayer(pos1);
        gui.drawMonster(pos2);

        // Verify newTextGraphics was called twice
        verify(mockScreen, times(2)).newTextGraphics();
    }

    @Test
    void testDrawSpriteAtOffset() {
        Position position = new Position(10, 10);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(2);
        when(mockSprite.getHeight()).thenReturn(2);

        Color opaqueColor = new Color(255, 0, 0, 255);
        when(mockSprite.getPixel(anyInt(), anyInt())).thenReturn(opaqueColor);

        gui.drawSprite(position, mockSprite);

        // Verify pixels are drawn at correct offsets
        verify(mockGraphics).putString(10, 10, "█");
        verify(mockGraphics).putString(11, 10, "█");
        verify(mockGraphics).putString(10, 11, "█");
        verify(mockGraphics).putString(11, 11, "█");
    }

    @Test
    void testDrawChestAtOffset() {
        Position position = new Position(15, 20);
        Sprite mockSprite = mock(Sprite.class);
        when(mockSprite.getWidth()).thenReturn(2);
        when(mockSprite.getHeight()).thenReturn(2);

        Color opaqueColor = new Color(100, 80, 60, 255);
        when(mockSprite.getPixel(anyInt(), anyInt())).thenReturn(opaqueColor);

        gui.drawChest(position, mockSprite, false);

        // Verify pixels are drawn at correct offsets
        verify(mockGraphics).putString(15, 20, "█");
        verify(mockGraphics).putString(16, 20, "█");
        verify(mockGraphics).putString(15, 21, "█");
        verify(mockGraphics).putString(16, 21, "█");
    }
}