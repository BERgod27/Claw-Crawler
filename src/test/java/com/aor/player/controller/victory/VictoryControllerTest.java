package com.aor.player.controller.victory;

import com.aor.player.Game;
import com.aor.player.gui.GUI;
import com.aor.player.model.victory.Victory;
import com.aor.player.states.MenuState;
import com.aor.player.states.StateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VictoryControllerTest {
    private VictoryController controller;
    private Victory victory;
    private Game game;
    private StateFactory stateFactory;
    private MenuState menuState;

    @BeforeEach
    void setUp() {
        victory = new Victory(60000, 500, 10); // 1 minute played, 500 gold, 10 slimes killed
        stateFactory = Mockito.mock(StateFactory.class);
        menuState = Mockito.mock(MenuState.class);
        controller = new VictoryController(victory, stateFactory);
        game = Mockito.mock(Game.class);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        assertNotNull(controller.getModel());
    }

    @Test
    void testGetModel() {
        assertEquals(victory, controller.getModel());
        assertSame(victory, controller.getModel());
    }

    @Test
    void testGetModelReturnsVictory() {
        Victory model = controller.getModel();
        assertNotNull(model);
        assertEquals(60000, model.getTimePlayed());
        assertEquals(500, model.getTotalGold());
        assertEquals(10, model.getSlimesKilled());
    }

    @Test
    void testStepWithSelectAction() throws IOException {
        when(stateFactory.createMenuState()).thenReturn(menuState);

        controller.step(game, GUI.ACTION.SELECT, 100);

        verify(stateFactory, times(1)).createMenuState();
        verify(game, times(1)).setState(menuState);
    }

    @Test
    void testStepWithQuitAction() throws IOException {
        when(stateFactory.createMenuState()).thenReturn(menuState);

        controller.step(game, GUI.ACTION.QUIT, 100);

        verify(stateFactory, times(1)).createMenuState();
        verify(game, times(1)).setState(menuState);
    }

    @Test
    void testBothSelectAndQuitGoToMenu() throws IOException {
        when(stateFactory.createMenuState()).thenReturn(menuState);

        // Test SELECT
        controller.step(game, GUI.ACTION.SELECT, 100);
        verify(game, times(1)).setState(menuState);

        // Reset mock
        Mockito.reset(game);
        when(stateFactory.createMenuState()).thenReturn(menuState);

        // Test QUIT - should have same behavior
        controller.step(game, GUI.ACTION.QUIT, 200);
        verify(game, times(1)).setState(menuState);
    }

    @Test
    void testStepWithSelectActionMultipleTimes() throws IOException {
        when(stateFactory.createMenuState()).thenReturn(menuState);

        controller.step(game, GUI.ACTION.SELECT, 100);
        controller.step(game, GUI.ACTION.SELECT, 200);

        verify(stateFactory, times(2)).createMenuState();
        verify(game, times(2)).setState(menuState);
    }

    @Test
    void testStepWithQuitActionMultipleTimes() throws IOException {
        when(stateFactory.createMenuState()).thenReturn(menuState);

        controller.step(game, GUI.ACTION.QUIT, 100);
        controller.step(game, GUI.ACTION.QUIT, 200);

        verify(stateFactory, times(2)).createMenuState();
        verify(game, times(2)).setState(menuState);
    }

    @Test
    void testStepWithUpAction() throws IOException {
        controller.step(game, GUI.ACTION.UP, 100);

        // Should not change state for UP action
        verify(game, never()).setState(any());
        verify(stateFactory, never()).createMenuState();
    }

    @Test
    void testStepWithDownAction() throws IOException {
        controller.step(game, GUI.ACTION.DOWN, 100);

        verify(game, never()).setState(any());
        verify(stateFactory, never()).createMenuState();
    }

    @Test
    void testStepWithLeftAction() throws IOException {
        controller.step(game, GUI.ACTION.LEFT, 100);

        verify(game, never()).setState(any());
        verify(stateFactory, never()).createMenuState();
    }

    @Test
    void testStepWithRightAction() throws IOException {
        controller.step(game, GUI.ACTION.RIGHT, 100);

        verify(game, never()).setState(any());
        verify(stateFactory, never()).createMenuState();
    }

    @Test
    void testStepWithNoneAction() throws IOException {
        controller.step(game, GUI.ACTION.NONE, 100);

        verify(game, never()).setState(any());
        verify(stateFactory, never()).createMenuState();
    }

    @Test
    void testStepWithInventoryAction() throws IOException {
        controller.step(game, GUI.ACTION.INVENTORY, 100);

        verify(game, never()).setState(any());
        verify(stateFactory, never()).createMenuState();
    }

    @Test
    void testStepDoesNotThrowExceptionWithVariousActions() {
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.UP, 100));
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.DOWN, 100));
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.LEFT, 100));
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.RIGHT, 100));
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.NONE, 100));
    }

    @Test
    void testOnlySelectAndQuitChangeState() throws IOException {
        when(stateFactory.createMenuState()).thenReturn(menuState);

        // Try all actions except SELECT and QUIT
        controller.step(game, GUI.ACTION.UP, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.LEFT, 100);
        controller.step(game, GUI.ACTION.RIGHT, 100);
        controller.step(game, GUI.ACTION.NONE, 100);

        // No state change should have occurred
        verify(game, never()).setState(any());

        // Now press SELECT
        controller.step(game, GUI.ACTION.SELECT, 100);

        // State should change exactly once
        verify(game, times(1)).setState(menuState);
    }

    @Test
    void testModelRetainsSameInstance() {
        Victory firstCall = controller.getModel();
        Victory secondCall = controller.getModel();

        assertSame(firstCall, secondCall);
    }

    @Test
    void testVictoryModelHasCorrectData() {
        Victory model = controller.getModel();

        assertEquals(60000, model.getTimePlayed());
        assertEquals(500, model.getTotalGold());
        assertEquals(10, model.getSlimesKilled());
    }

    @Test
    void testVictoryFormattedTime() {
        Victory model = controller.getModel();

        // 60000 ms = 60 seconds = 1 minute = 01:00
        assertEquals("01:00", model.getFormattedTime());
    }

    @Test
    void testVictoryWithDifferentStats() {
        Victory customVictory = new Victory(123456, 9999, 42);
        VictoryController customController = new VictoryController(customVictory, stateFactory);

        assertEquals(customVictory, customController.getModel());
        assertEquals(123456, customController.getModel().getTimePlayed());
        assertEquals(9999, customController.getModel().getTotalGold());
        assertEquals(42, customController.getModel().getSlimesKilled());
    }

    @Test
    void testSwitchCaseHandlesBothActions() throws IOException {
        when(stateFactory.createMenuState()).thenReturn(menuState);

        // Test that both SELECT and QUIT are handled by the same switch case
        controller.step(game, GUI.ACTION.SELECT, 100);
        verify(stateFactory, times(1)).createMenuState();

        Mockito.reset(stateFactory);
        when(stateFactory.createMenuState()).thenReturn(menuState);

        controller.step(game, GUI.ACTION.QUIT, 200);
        verify(stateFactory, times(1)).createMenuState();
    }

    @Test
    void testDefaultCaseDoesNothing() throws IOException {
        // All these should hit the default case and do nothing
        GUI.ACTION[] defaultActions = {
                GUI.ACTION.UP,
                GUI.ACTION.DOWN,
                GUI.ACTION.LEFT,
                GUI.ACTION.RIGHT,
                GUI.ACTION.NONE,
                GUI.ACTION.INVENTORY
        };

        for (GUI.ACTION action : defaultActions) {
            controller.step(game, action, 100);
        }

        // No state changes should occur
        verify(game, never()).setState(any());
        verify(stateFactory, never()).createMenuState();
    }
}
