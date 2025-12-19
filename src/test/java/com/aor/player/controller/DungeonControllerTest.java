package com.aor.player.controller;

import com.aor.player.Game;
import com.aor.player.controller.game.DungeonController;
import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.dungeon.Room;
import com.aor.player.model.game.elements.*;
import com.aor.player.states.CombatState;
import com.aor.player.states.InventoryState;
import com.aor.player.states.ShopState;
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
        // First Q press sets confirmation flag, doesn't quit yet
        controller.step(game, GUI.ACTION.QUIT, 100);
        verify(game, never()).setState(null);

        // Second Q press actually quits
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

    @Test
    void testMonsterCollisionInitiatesCombat() throws IOException {
        // Add a monster to the dungeon
        Monster monster = new Monster(52, 50, MonsterType.SLIME);
        dungeon.addMonster(monster);

        CombatState combatState = Mockito.mock(CombatState.class);
        when(stateFactory.createCombatState(dungeon, monster)).thenReturn(combatState);

        // Try to move right into the monster
        controller.step(game, GUI.ACTION.RIGHT, 100);

        // Should transition to combat state
        verify(stateFactory, times(1)).createCombatState(dungeon, monster);
        verify(game, times(1)).setState(combatState);
    }

    @Test
    void testChestInteractionWithSelectKey() throws IOException {
        Position playerPos = new Position(10, 10);
        player.setPosition(playerPos);

        // Add a chest near the player (Chest constructor only takes Position)
        Chest chest = new Chest(new Position(11, 10));
        Room room = dungeon.getCurrentRoom();
        room.addChest(chest);

        int initialGold = player.getGold();

        // Press SELECT to open chest
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Player should gain gold from chest
        assertTrue(player.getGold() > initialGold);
        assertTrue(chest.isOpened());
    }

    @Test
    void testChestAlreadyOpenedDoesNotGiveGold() throws IOException {
        Position playerPos = new Position(10, 10);
        player.setPosition(playerPos);

        // Add a chest and open it (Chest constructor only takes Position)
        Chest chest = new Chest(new Position(11, 10));
        chest.open(); // Open chest first
        Room room = dungeon.getCurrentRoom();
        room.addChest(chest);

        int initialGold = player.getGold();

        // Try to open again with SELECT
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Gold should not increase
        assertEquals(initialGold, player.getGold());
    }

    @Test
    void testDoorTransitionsBetweenRooms() throws IOException {
        // Create a second room
        Room targetRoom = new Room("target_room");
        dungeon.addRoom(targetRoom);

        // Add a door to current room (Door constructor takes x, y, targetRoomId,
        // spawnPosition)
        Position doorPos = player.getPosition().getRight();
        Door door = new Door(doorPos.getX(), doorPos.getY(), "target_room", new Position(5, 5));
        dungeon.getCurrentRoom().addDoor(door);

        String initialRoomId = dungeon.getCurrentRoomId();

        // Move right to go through door
        controller.step(game, GUI.ACTION.RIGHT, 100);

        // Should be in new room
        assertEquals("target_room", dungeon.getCurrentRoomId());
        assertNotEquals(initialRoomId, dungeon.getCurrentRoomId());
    }

    @Test
    void testShopInteractionWhenNearShopKeeper() throws IOException {
        // Add shopkeeper to room (ShopKeeper constructor takes x, y integers)
        ShopKeeper shopKeeper = new ShopKeeper(player.getPosition().getX() + 1, player.getPosition().getY());
        dungeon.getCurrentRoom().setShopKeeper(shopKeeper);

        ShopState shopState = Mockito.mock(ShopState.class);
        when(stateFactory.createShopState(dungeon)).thenReturn(shopState);

        // Press SELECT near shopkeeper
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Should transition to shop state
        verify(stateFactory, times(1)).createShopState(dungeon);
        verify(game, times(1)).setState(shopState);
    }

    @Test
    void testInventoryKeyOpensInventory() throws IOException {
        InventoryState inventoryState = Mockito.mock(InventoryState.class);
        when(stateFactory.createInventoryState(dungeon)).thenReturn(inventoryState);

        controller.step(game, GUI.ACTION.INVENTORY, 100);

        verify(stateFactory, times(1)).createInventoryState(dungeon);
        verify(game, times(1)).setState(inventoryState);
    }

    @Test
    void testMonsterMovementDoesNotCrash() throws IOException {
        // Add multiple monsters (use BOSS_DEMON_KING instead of DEMON)
        Monster slime = new Monster(20, 20, MonsterType.SLIME);
        Monster boss = new Monster(30, 30, MonsterType.BOSS_DEMON_KING);
        dungeon.addMonster(slime);
        dungeon.addMonster(boss);

        // Execute multiple steps with time progression
        controller.step(game, GUI.ACTION.NONE, 1000);
        controller.step(game, GUI.ACTION.NONE, 2000);
        controller.step(game, GUI.ACTION.NONE, 3000);

        // Should not crash - monsters may or may not have moved
        assertNotNull(slime.getPosition());
        assertNotNull(boss.getPosition());
    }

    @Test
    void testQuitConfirmationFlag() {
        assertFalse(controller.isQuitConfirmation());
    }

    @Test
    void testMovementCancelsQuitConfirmation() throws IOException {
        // Press Q to set quit confirmation
        controller.step(game, GUI.ACTION.QUIT, 100);
        assertTrue(controller.isQuitConfirmation());

        // Move player
        controller.step(game, GUI.ACTION.UP, 100);

        // Quit confirmation should be cancelled
        assertFalse(controller.isQuitConfirmation());
    }

    @Test
    void testSelectCancelsQuitConfirmation() throws IOException {
        // Press Q to set quit confirmation
        controller.step(game, GUI.ACTION.QUIT, 100);
        assertTrue(controller.isQuitConfirmation());

        // Press SELECT
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Quit confirmation should be cancelled
        assertFalse(controller.isQuitConfirmation());
    }

    @Test
    void testPlayerCannotMoveIntoMonster() throws IOException {
        Position initialPos = new Position(10, 10);
        player.setPosition(initialPos);

        // Place monster directly to the right
        Monster monster = new Monster(initialPos.getX() + 1, initialPos.getY(), MonsterType.SLIME);
        dungeon.addMonster(monster);

        when(stateFactory.createCombatState(any(), any())).thenReturn(Mockito.mock(CombatState.class));

        // Try to move right
        controller.step(game, GUI.ACTION.RIGHT, 100);

        // Combat should be initiated instead of moving
        verify(stateFactory, times(1)).createCombatState(eq(dungeon), eq(monster));
    }

    @Test
    void testIsPlayerNearShopKeeper() {
        // No shopkeeper initially
        assertFalse(controller.isPlayerNearShopKeeper());

        // Add shopkeeper near player (ShopKeeper constructor takes x, y integers)
        ShopKeeper shopKeeper = new ShopKeeper(player.getPosition().getX() + 1, player.getPosition().getY());
        dungeon.getCurrentRoom().setShopKeeper(shopKeeper);

        // Should detect shopkeeper proximity
        assertTrue(controller.isPlayerNearShopKeeper());
    }

    @Test
    void testPlayerCannotMoveIntoChest() throws IOException {
        Position initialPos = new Position(10, 10);
        player.setPosition(initialPos);

        // Place chest directly to the right (Chest constructor only takes Position)
        Chest chest = new Chest(initialPos.getRight());
        dungeon.getCurrentRoom().addChest(chest);

        // Try to move right into chest
        controller.step(game, GUI.ACTION.RIGHT, 100);

        // Player should not have moved
        assertEquals(initialPos, player.getPosition());
    }
}
