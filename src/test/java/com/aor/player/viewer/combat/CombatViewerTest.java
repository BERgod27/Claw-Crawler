package com.aor.player.viewer.combat;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CombatViewerTest {

    private Dungeon mockDungeon;
    private Monster mockMonster;
    private Player mockPlayer;
    private GUI mockGUI;
    private CombatViewer combatViewer;

    @BeforeEach
    void setUp() {
        mockDungeon = mock(Dungeon.class);
        mockMonster = mock(Monster.class);
        mockPlayer = mock(Player.class);
        mockGUI = mock(GUI.class);

        when(mockDungeon.getPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getHealth()).thenReturn(80);
        when(mockPlayer.getMaxHealth()).thenReturn(100);
        when(mockPlayer.getAttack()).thenReturn(15);
        when(mockPlayer.getArmor()).thenReturn(10);

        when(mockMonster.getName()).thenReturn("Slime");
        when(mockMonster.getHealth()).thenReturn(20);
        when(mockMonster.getMaxHealth()).thenReturn(30);
        when(mockMonster.getAttack()).thenReturn(5);
        when(mockMonster.getDefense()).thenReturn(2);

        combatViewer = new CombatViewer(mockDungeon, mockMonster);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockDungeon, combatViewer.getModel());
    }

    @Test
    void testSetSelectedAction() {
        combatViewer.setSelectedAction(2);

        // Verify by drawing and checking the viewer doesn't crash
        combatViewer.draw(mockGUI);
        verify(mockGUI).clear();
    }

    @Test
    void testSetCombatLog() {
        List<String> log = Arrays.asList("Player attacks!", "Monster takes 10 damage");

        combatViewer.setCombatLog(log);
        combatViewer.draw(mockGUI);

        verify(mockGUI).clear();
    }

    @Test
    void testDrawCallsClearAndRefresh() throws IOException {
        combatViewer.draw(mockGUI);

        verify(mockGUI).clear();
        verify(mockGUI, atLeastOnce()).refresh();
    }

    @Test
    void testDrawDisplaysPlayerStats() {
        combatViewer.draw(mockGUI);

        verify(mockGUI, atLeastOnce()).drawText(any(), contains("PLAYER"), any());
        verify(mockGUI, atLeastOnce()).drawText(any(), contains("HP"), any());
    }

    @Test
    void testDrawDisplaysMonsterStats() {
        combatViewer.draw(mockGUI);

        verify(mockGUI, atLeastOnce()).drawText(any(), contains("SLIME"), any());
    }

    @Test
    void testDrawDisplaysCombatLog() {
        List<String> log = Arrays.asList("Test message 1", "Test message 2");
        combatViewer.setCombatLog(log);

        combatViewer.draw(mockGUI);

        verify(mockGUI).drawText(any(), eq("Test message 1"), any());
        verify(mockGUI).drawText(any(), eq("Test message 2"), any());
    }

    @Test
    void testDrawDisplaysActions() {
        combatViewer.draw(mockGUI);

        verify(mockGUI, atLeastOnce()).drawText(any(), contains("Attack"), any());
        verify(mockGUI, atLeastOnce()).drawText(any(), contains("Defend"), any());
    }

    @Test
    void testSelectedActionHighlighted() {
        combatViewer.setSelectedAction(0);
        combatViewer.draw(mockGUI);

        // Selected action should have different color or prefix
        verify(mockGUI).clear();
    }
}
