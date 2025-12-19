package com.aor.player.viewer.victory;

import com.aor.player.gui.GUI;
import com.aor.player.model.victory.Victory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VictoryViewerTest {

    private Victory mockVictory;
    private GUI mockGUI;
    private VictoryViewer victoryViewer;

    @BeforeEach
    void setUp() {
        mockVictory = mock(Victory.class);
        mockGUI = mock(GUI.class);

        when(mockVictory.getFormattedTime()).thenReturn("12:34");
        when(mockVictory.getTotalGold()).thenReturn(500);
        when(mockVictory.getSlimesKilled()).thenReturn(25);

        victoryViewer = new VictoryViewer(mockVictory);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockVictory, victoryViewer.getModel());
    }

    @Test
    void testDrawElementsCallsClear() {
        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).clear();
    }

    @Test
    void testDrawElementsCallsRefresh() throws java.io.IOException {
        victoryViewer.drawElements(mockGUI);

        verify(mockGUI, atLeastOnce()).refresh();
    }

    @Test
    void testDrawDisplaysVictoryBanner() {
        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(28), eq(8), eq("==========================="), eq("#FFD700"));
        verify(mockGUI).drawText(eq(28), eq(9), eq("       VICTORY!            "), eq("#FFD700"));
        verify(mockGUI).drawText(eq(28), eq(10), eq("==========================="), eq("#FFD700"));
    }

    @Test
    void testDrawDisplaysVictoryMessage() {
        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(23), eq(12), eq("All bosses have been defeated!"), eq("#00FF00"));
        verify(mockGUI).drawText(eq(26), eq(13), eq("You are the champion!"), eq("#00FF00"));
    }

    @Test
    void testDrawDisplaysScoreboardHeader() {
        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(28), eq(16), eq("===== SCOREBOARD ====="), eq("#FFFF00"));
    }

    @Test
    void testDrawDisplaysFormattedTime() {
        when(mockVictory.getFormattedTime()).thenReturn("05:30");

        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(27), eq(18), eq("Time to Victory: 05:30"), eq("#FFFFFF"));
    }

    @Test
    void testDrawDisplaysTotalGold() {
        when(mockVictory.getTotalGold()).thenReturn(750);

        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(27), eq(20), eq("Total Gold: 750"), eq("#FFFFFF"));
    }

    @Test
    void testDrawDisplaysSlimesKilled() {
        when(mockVictory.getSlimesKilled()).thenReturn(42);

        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(27), eq(22), eq("Slimes Defeated: 42"), eq("#FFFFFF"));
    }

    @Test
    void testDrawDisplaysScoreboardFooter() {
        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(28), eq(25), eq("======================"), eq("#FFFF00"));
    }

    @Test
    void testDrawDisplaysControls() {
        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(26), eq(28), eq("ENTER: New Game | Q: Exit"), eq("#888888"));
    }

    @Test
    void testDrawWithDifferentStats() {
        when(mockVictory.getFormattedTime()).thenReturn("20:15");
        when(mockVictory.getTotalGold()).thenReturn(1000);
        when(mockVictory.getSlimesKilled()).thenReturn(100);

        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(27), eq(18), eq("Time to Victory: 20:15"), eq("#FFFFFF"));
        verify(mockGUI).drawText(eq(27), eq(20), eq("Total Gold: 1000"), eq("#FFFFFF"));
        verify(mockGUI).drawText(eq(27), eq(22), eq("Slimes Defeated: 100"), eq("#FFFFFF"));
    }

    @Test
    void testDrawWithZeroStats() {
        when(mockVictory.getFormattedTime()).thenReturn("00:00");
        when(mockVictory.getTotalGold()).thenReturn(0);
        when(mockVictory.getSlimesKilled()).thenReturn(0);

        victoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(27), eq(18), eq("Time to Victory: 00:00"), eq("#FFFFFF"));
        verify(mockGUI).drawText(eq(27), eq(20), eq("Total Gold: 0"), eq("#FFFFFF"));
        verify(mockGUI).drawText(eq(27), eq(22), eq("Slimes Defeated: 0"), eq("#FFFFFF"));
    }

    @Test
    void testDrawAllTextColors() {
        victoryViewer.drawElements(mockGUI);

        // Verify golden color for banners
        verify(mockGUI, atLeast(3)).drawText(anyInt(), anyInt(), anyString(), eq("#FFD700"));

        // Verify green for victory messages
        verify(mockGUI, atLeast(2)).drawText(anyInt(), anyInt(), anyString(), eq("#00FF00"));

        // Verify yellow for scoreboard headers
        verify(mockGUI, atLeast(2)).drawText(anyInt(), anyInt(), anyString(), eq("#FFFF00"));

        // Verify white for stats
        verify(mockGUI, atLeast(3)).drawText(anyInt(), anyInt(), anyString(), eq("#FFFFFF"));

        // Verify gray for controls
        verify(mockGUI).drawText(anyInt(), anyInt(), anyString(), eq("#888888"));
    }
}
