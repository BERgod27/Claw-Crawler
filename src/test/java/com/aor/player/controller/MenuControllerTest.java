package com.aor.player.controller;

import com.aor.player.Game;
import com.aor.player.controller.menu.MenuController;
import com.aor.player.gui.GUI;
import com.aor.player.model.menu.Menu;
import com.aor.player.states.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuControllerTest {
    private MenuController controller;
    private Menu menu;
    private Game game;

    @BeforeEach
    void setUp() {
        menu = new Menu();
        controller = new MenuController(menu);
        game = Mockito.mock(Game.class);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        assertNotNull(controller.getModel());
    }

    @Test
    void testGetModel() {
        assertEquals(menu, controller.getModel());
    }

    @Test
    void testStepWithUpAction() throws IOException {
        // Initially on "Start" (index 0)
        assertTrue(menu.isSelectedStart());

        controller.step(game, GUI.ACTION.UP, 100);

        // Should wrap to "Exit" (index 1)
        assertTrue(menu.isSelectedExit());
    }

    @Test
    void testStepWithDownAction() throws IOException {
        // Initially on "Start" (index 0)
        assertTrue(menu.isSelectedStart());

        controller.step(game, GUI.ACTION.DOWN, 100);

        // Should move to "Exit" (index 1)
        assertTrue(menu.isSelectedExit());
    }

    @Test
    void testStepWithSelectOnExit() throws IOException {
        // Move to Exit option
        menu.nextEntry();
        assertTrue(menu.isSelectedExit());

        controller.step(game, GUI.ACTION.SELECT, 100);

        // Should set game state to null
        verify(game, times(1)).setState(null);
    }

    @Test
    void testStepWithSelectOnStart() throws IOException {
        // Should be on Start by default
        assertTrue(menu.isSelectedStart());

        controller.step(game, GUI.ACTION.SELECT, 100);

        // Should create a new GameState
        ArgumentCaptor<GameState> stateCaptor = ArgumentCaptor.forClass(GameState.class);
        verify(game, times(1)).setState(stateCaptor.capture());

        GameState capturedState = stateCaptor.getValue();
        assertNotNull(capturedState);
    }

    @Test
    void testStepWithOtherActions() throws IOException {
        boolean initialStart = menu.isSelectedStart();

        controller.step(game, GUI.ACTION.LEFT, 100);
        assertEquals(initialStart, menu.isSelectedStart());

        controller.step(game, GUI.ACTION.RIGHT, 100);
        assertEquals(initialStart, menu.isSelectedStart());

        controller.step(game, GUI.ACTION.QUIT, 100);
        assertEquals(initialStart, menu.isSelectedStart());
    }

    @Test
    void testMenuNavigation() throws IOException {
        // Start at "Start"
        assertTrue(menu.isSelectedStart());

        // Go down to "Exit"
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertTrue(menu.isSelectedExit());

        // Go down again, should wrap to "Start"
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertTrue(menu.isSelectedStart());

        // Go up, should wrap to "Exit"
        controller.step(game, GUI.ACTION.UP, 100);
        assertTrue(menu.isSelectedExit());

        // Go up again, should go to "Start"
        controller.step(game, GUI.ACTION.UP, 100);
        assertTrue(menu.isSelectedStart());
    }

    @Test
    void testCreateDungeonDimensions() throws IOException {
        controller.step(game, GUI.ACTION.SELECT, 100);

        ArgumentCaptor<GameState> stateCaptor = ArgumentCaptor.forClass(GameState.class);
        verify(game).setState(stateCaptor.capture());

        GameState gameState = stateCaptor.getValue();
        assertNotNull(gameState);
        assertEquals(20, gameState.getModel().getWidth());
        assertEquals(20, gameState.getModel().getHeight());
    }

    @Test
    void testCreateDungeonPlayerPosition() throws IOException {
        controller.step(game, GUI.ACTION.SELECT, 100);

        ArgumentCaptor<GameState> stateCaptor = ArgumentCaptor.forClass(GameState.class);
        verify(game).setState(stateCaptor.capture());

        GameState gameState = stateCaptor.getValue();
        assertNotNull(gameState.getModel().getPlayer());
        assertEquals(10, gameState.getModel().getPlayer().getPosition().getX());
        assertEquals(10, gameState.getModel().getPlayer().getPosition().getY());
    }

    @Test
    void testCreateDungeonWalls() throws IOException {
        controller.step(game, GUI.ACTION.SELECT, 100);

        ArgumentCaptor<GameState> stateCaptor = ArgumentCaptor.forClass(GameState.class);
        verify(game).setState(stateCaptor.capture());

        GameState gameState = stateCaptor.getValue();
        assertNotNull(gameState.getModel().getWalls());
        // Should have walls around perimeter: 20 top + 20 bottom + 18 left + 18 right =
        // 76
        assertEquals(76, gameState.getModel().getWalls().size());
    }

    @Test
    void testCreateDungeonMonsters() throws IOException {
        controller.step(game, GUI.ACTION.SELECT, 100);

        ArgumentCaptor<GameState> stateCaptor = ArgumentCaptor.forClass(GameState.class);
        verify(game).setState(stateCaptor.capture());

        GameState gameState = stateCaptor.getValue();
        assertNotNull(gameState.getModel().getMonsters());
        assertEquals(0, gameState.getModel().getMonsters().size());
    }
}
