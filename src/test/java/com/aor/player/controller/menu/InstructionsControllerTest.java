package com.aor.player.controller.menu;

import com.aor.player.Game;
import com.aor.player.gui.GUI;
import com.aor.player.model.menu.Instructions;
import com.aor.player.states.MenuState;
import com.aor.player.states.StateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructionsControllerTest {
    private InstructionsController controller;
    private Instructions instructions;
    private Game game;
    private StateFactory stateFactory;
    private MenuState menuState;

    @BeforeEach
    void setUp() {
        instructions = new Instructions();
        stateFactory = Mockito.mock(StateFactory.class);
        menuState = Mockito.mock(MenuState.class);
        controller = new InstructionsController(instructions, stateFactory);
        game = Mockito.mock(Game.class);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        assertNotNull(controller.getModel());
    }

    @Test
    void testGetModel() {
        assertEquals(instructions, controller.getModel());
        assertSame(instructions, controller.getModel());
    }

    @Test
    void testGetModelReturnsInstructions() {
        Instructions model = controller.getModel();
        assertNotNull(model);
        assertEquals("Instructions", model.getTitle());
        assertTrue(model.getNumberOfLines() > 0);
    }

    @Test
    void testStepWithQuitAction() throws IOException {
        when(stateFactory.createMenuState()).thenReturn(menuState);

        controller.step(game, GUI.ACTION.QUIT, 100);

        verify(stateFactory, times(1)).createMenuState();
        verify(game, times(1)).setState(menuState);
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

        // Should not call setState when action is not QUIT
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
    void testStepWithSelectAction() throws IOException {
        controller.step(game, GUI.ACTION.SELECT, 100);

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
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.SELECT, 100));
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.NONE, 100));
    }

    @Test
    void testOnlyQuitActionChangesState() throws IOException {
        when(stateFactory.createMenuState()).thenReturn(menuState);

        // Try all actions except QUIT
        controller.step(game, GUI.ACTION.UP, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.LEFT, 100);
        controller.step(game, GUI.ACTION.RIGHT, 100);
        controller.step(game, GUI.ACTION.SELECT, 100);
        controller.step(game, GUI.ACTION.NONE, 100);

        // No state change should have occurred
        verify(game, never()).setState(any());

        // Now press QUIT
        controller.step(game, GUI.ACTION.QUIT, 100);

        // State should change exactly once
        verify(game, times(1)).setState(menuState);
    }

    @Test
    void testModelRetainsSameInstance() {
        Instructions firstCall = controller.getModel();
        Instructions secondCall = controller.getModel();

        assertSame(firstCall, secondCall);
    }

    @Test
    void testInstructionsHasCorrectContent() {
        Instructions model = controller.getModel();

        assertEquals("Instructions", model.getTitle());
        assertNotNull(model.getInstructionLines());
        assertTrue(model.getInstructionLines().length > 0);

        // Verify instruction lines contain expected content
        String[] lines = model.getInstructionLines();
        boolean hasMovementInfo = false;
        boolean hasQuitInfo = false;

        for (String line : lines) {
            if (line.contains("Move") || line.contains("Arrow")) {
                hasMovementInfo = true;
            }
            if (line.contains("Q") || line.contains("Quit")) {
                hasQuitInfo = true;
            }
        }

        assertTrue(hasMovementInfo, "Instructions should contain movement information");
        assertTrue(hasQuitInfo, "Instructions should contain quit information");
    }
}
