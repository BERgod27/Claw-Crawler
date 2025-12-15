package com.aor.player.controller;

import com.aor.player.Game;
import com.aor.player.controller.game.DungeonController;
import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.dungeon.Room;
import com.aor.player.model.game.elements.Player;
import com.aor.player.states.StateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DungeonControllerTest {
    private DungeonController controller;
    private Dungeon dungeon;
    private Player player;
    private Game game;
    private StateFactory stateFactory;

    @BeforeEach
    void setUp() {
        dungeon = new Dungeon(100, 100);
        player = new Player(50, 50);
        dungeon.setPlayer(player);

        // Create and add a room to prevent NullPointerException in
        // checkChestInteraction
        Room room = new Room("test_room");
        dungeon.addRoom(room);
        dungeon.setCurrentRoomId("test_room");

        stateFactory = Mockito.mock(StateFactory.class);
        controller = new DungeonController(dungeon, stateFactory);
        game = Mockito.mock(Game.class);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        assertNotNull(controller.getModel());
    }

    @Test
    void testGetModel() {
        assertEquals(dungeon, controller.getModel());
    }

    @Test
    void testStepWithUpAction() throws IOException {
        Position initialPosition = player.getPosition();
        controller.step(game, GUI.ACTION.UP, 100);
        assertEquals(initialPosition.getUp(), player.getPosition());
    }

    @Test
    void testStepWithDownAction() throws IOException {
        Position initialPosition = player.getPosition();
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(initialPosition.getDown(), player.getPosition());
    }

    @Test
    void testStepWithLeftAction() throws IOException {
        Position initialPosition = player.getPosition();
        controller.step(game, GUI.ACTION.LEFT, 100);
        assertEquals(initialPosition.getLeft(), player.getPosition());
    }

    @Test
    void testStepWithRightAction() throws IOException {
        Position initialPosition = player.getPosition();
        controller.step(game, GUI.ACTION.RIGHT, 100);
        assertEquals(initialPosition.getRight(), player.getPosition());
    }

    @Test
    void testStepWithQuitAction() throws IOException {
        controller.step(game, GUI.ACTION.QUIT, 100);
        verify(game, times(1)).setState(null);
    }

    @Test
    void testStepWithSelectAction() throws IOException {
        Position initialPosition = player.getPosition();
        controller.step(game, GUI.ACTION.SELECT, 100);
        // SELECT action should not move player
        assertEquals(initialPosition, player.getPosition());
    }

    @Test
    void testMovePlayerLeft() {
        Position initialPosition = player.getPosition();
        controller.movePlayerLeft();
        assertEquals(initialPosition.getLeft(), player.getPosition());
    }

    @Test
    void testMovePlayerRight() {
        Position initialPosition = player.getPosition();
        controller.movePlayerRight();
        assertEquals(initialPosition.getRight(), player.getPosition());
    }

    @Test
    void testMovePlayerUp() {
        Position initialPosition = player.getPosition();
        controller.movePlayerUp();
        assertEquals(initialPosition.getUp(), player.getPosition());
    }

    @Test
    void testMovePlayerDown() {
        Position initialPosition = player.getPosition();
        controller.movePlayerDown();
        assertEquals(initialPosition.getDown(), player.getPosition());
    }

    @Test
    void testMultipleMovements() throws IOException {
        Position start = new Position(5, 5);
        player.setPosition(start);

        controller.step(game, GUI.ACTION.UP, 100);
        assertEquals(new Position(5, 4), player.getPosition());

        controller.step(game, GUI.ACTION.RIGHT, 100);
        assertEquals(new Position(6, 4), player.getPosition());

        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(new Position(6, 5), player.getPosition());

        controller.step(game, GUI.ACTION.LEFT, 100);
        assertEquals(new Position(5, 5), player.getPosition());
    }
}
