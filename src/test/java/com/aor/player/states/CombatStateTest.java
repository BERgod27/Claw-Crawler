package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.combat.CombatController;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.combat.CombatViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CombatStateTest {

    private Dungeon mockDungeon;
    private Monster mockMonster;
    private StateFactory mockStateFactory;
    private CombatState combatState;

    @BeforeEach
    void setUp() {
        mockDungeon = mock(Dungeon.class);
        mockMonster = mock(Monster.class);
        mockStateFactory = mock(StateFactory.class);
        combatState = new CombatState(mockDungeon, mockMonster, mockStateFactory);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockDungeon, combatState.getModel());
    }

    @Test
    void testGetViewerReturnsCombatViewer() {
        Viewer<Dungeon> viewer = combatState.getViewer();

        assertNotNull(viewer);
        assertTrue(viewer instanceof CombatViewer);
    }

    @Test
    void testGetControllerReturnsCombatController() {
        Controller<Dungeon> controller = combatState.getController();

        assertNotNull(controller);
        assertTrue(controller instanceof CombatController);
    }

    @Test
    void testGetControllerReturnsSameInstance() {
        Controller<Dungeon> controller1 = combatState.getController();
        Controller<Dungeon> controller2 = combatState.getController();

        assertSame(controller1, controller2);
    }

    @Test
    void testGetViewerReturnsSameInstance() {
        Viewer<Dungeon> viewer1 = combatState.getViewer();
        Viewer<Dungeon> viewer2 = combatState.getViewer();

        // Should return same viewer instance
        assertSame(viewer1, viewer2);
    }

    @Test
    void testConstructorInitializesController() {
        Controller<Dungeon> controller = combatState.getController();
        assertNotNull(controller);
    }

    @Test
    void testConstructorInitializesViewer() {
        Viewer<Dungeon> viewer = combatState.getViewer();
        assertNotNull(viewer);
    }
}
