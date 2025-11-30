package com.aor.player.controller;

import com.aor.player.Game;
import com.aor.player.controller.game.PlayerController;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.elements.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {
    private PlayerController controller;
    private Player player;
    private Game game;

    @BeforeEach
    void setUp() {
        player = new Player(5, 5);
        controller = new PlayerController(player);
        game = Mockito.mock(Game.class);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        assertNotNull(controller.getModel());
    }

    @Test
    void testGetModel() {
        assertEquals(player, controller.getModel());
    }

    @Test
    void testStepDoesNotThrowException() {
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.UP, 100));
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.DOWN, 100));
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.LEFT, 100));
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.RIGHT, 100));
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.QUIT, 100));
        assertDoesNotThrow(() -> controller.step(game, GUI.ACTION.SELECT, 100));
    }

    @Test
    void testStepWithNullAction() {
        assertDoesNotThrow(() -> controller.step(game, null, 100));
    }
}
