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
        verify(game, never()).setState(null);
        controller.step(game, GUI.ACTION.QUIT, 100);
        verify(game, times(1)).setState(null);
    }

    @Test
    void testStepWithSelectAction() throws IOException {
        Position initialPosition = player.getPosition();
        controller.step(game, GUI.ACTION.SELECT, 100);
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
        Monster monster = new Monster(52, 50, MonsterType.SLIME);
        dungeon.addMonster(monster);
        CombatState combatState = Mockito.mock(CombatState.class);
        when(stateFactory.createCombatState(dungeon, monster)).thenReturn(combatState);
        controller.step(game, GUI.ACTION.RIGHT, 100);
        verify(stateFactory, times(1)).createCombatState(dungeon, monster);
        verify(game, times(1)).setState(combatState);
    }

    @Test
    void testChestInteractionWithSelectKey() throws IOException {
        Position playerPos = new Position(10, 10);
        player.setPosition(playerPos);
        Chest chest = new Chest(new Position(11, 10));
        Room room = dungeon.getCurrentRoom();
        room.addChest(chest);
        int initialGold = player.getGold();
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(player.getGold() > initialGold);
        assertTrue(chest.isOpened());
    }

    @Test
    void testChestAlreadyOpenedDoesNotGiveGold() throws IOException {
        Position playerPos = new Position(10, 10);
        player.setPosition(playerPos);
        Chest chest = new Chest(new Position(11, 10));
        chest.open();
        Room room = dungeon.getCurrentRoom();
        room.addChest(chest);
        int initialGold = player.getGold();
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertEquals(initialGold, player.getGold());
    }

    @Test
    void testDoorTransitionsBetweenRooms() throws IOException {
        Room targetRoom = new Room("target_room");
        dungeon.addRoom(targetRoom);
        Position doorPos = player.getPosition().getRight();
        Door door = new Door(doorPos.getX(), doorPos.getY(), "target_room", new Position(5, 5));
        dungeon.getCurrentRoom().addDoor(door);
        String initialRoomId = dungeon.getCurrentRoomId();
        controller.step(game, GUI.ACTION.RIGHT, 100);
        assertEquals("target_room", dungeon.getCurrentRoomId());
        assertNotEquals(initialRoomId, dungeon.getCurrentRoomId());
    }

    @Test
    void testShopInteractionWhenNearShopKeeper() throws IOException {
        ShopKeeper shopKeeper = new ShopKeeper(player.getPosition().getX() + 1, player.getPosition().getY());
        dungeon.getCurrentRoom().setShopKeeper(shopKeeper);
        ShopState shopState = Mockito.mock(ShopState.class);
        when(stateFactory.createShopState(dungeon)).thenReturn(shopState);
        controller.step(game, GUI.ACTION.SELECT, 100);
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
        Monster slime = new Monster(20, 20, MonsterType.SLIME);
        Monster boss = new Monster(30, 30, MonsterType.BOSS_DEMON_KING);
        dungeon.addMonster(slime);
        dungeon.addMonster(boss);
        controller.step(game, GUI.ACTION.NONE, 1000);
        controller.step(game, GUI.ACTION.NONE, 2000);
        controller.step(game, GUI.ACTION.NONE, 3000);
        assertNotNull(slime.getPosition());
        assertNotNull(boss.getPosition());
    }

    @Test
    void testQuitConfirmationFlag() {
        assertFalse(controller.isQuitConfirmation());
    }

    @Test
    void testMovementCancelsQuitConfirmation() throws IOException {
        controller.step(game, GUI.ACTION.QUIT, 100);
        assertTrue(controller.isQuitConfirmation());
        controller.step(game, GUI.ACTION.UP, 100);
        assertFalse(controller.isQuitConfirmation());
    }

    @Test
    void testSelectCancelsQuitConfirmation() throws IOException {
        controller.step(game, GUI.ACTION.QUIT, 100);
        assertTrue(controller.isQuitConfirmation());
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertFalse(controller.isQuitConfirmation());
    }

    @Test
    void testPlayerCannotMoveIntoMonster() throws IOException {
        Position initialPos = new Position(10, 10);
        player.setPosition(initialPos);
        Monster monster = new Monster(initialPos.getX() + 1, initialPos.getY(), MonsterType.SLIME);
        dungeon.addMonster(monster);
        when(stateFactory.createCombatState(any(), any())).thenReturn(Mockito.mock(CombatState.class));
        controller.step(game, GUI.ACTION.RIGHT, 100);
        verify(stateFactory, times(1)).createCombatState(eq(dungeon), eq(monster));
    }

    @Test
    void testIsPlayerNearShopKeeper() {
        assertFalse(controller.isPlayerNearShopKeeper());
        ShopKeeper shopKeeper = new ShopKeeper(player.getPosition().getX() + 1, player.getPosition().getY());
        dungeon.getCurrentRoom().setShopKeeper(shopKeeper);
        assertTrue(controller.isPlayerNearShopKeeper());
    }

    @Test
    void testPlayerCannotMoveIntoChest() throws IOException {
        Position initialPos = new Position(10, 10);
        player.setPosition(initialPos);
        Chest chest = new Chest(initialPos.getRight());
        dungeon.getCurrentRoom().addChest(chest);
        controller.step(game, GUI.ACTION.RIGHT, 100);
        assertEquals(initialPos, player.getPosition());
    }

    @Test
    void testLockedDemonDoorDoesNotTransition() throws IOException {
        Room demonRoom = new Room("demon");
        dungeon.addRoom(demonRoom);
        Position doorPos = player.getPosition().getRight();
        Door demonDoor = new Door(doorPos.getX(), doorPos.getY(), "demon", new Position(5, 5));
        dungeon.getCurrentRoom().addDoor(demonDoor);
        Position initialPos = player.getPosition();
        controller.step(game, GUI.ACTION.RIGHT, 100);
        assertEquals("test_room", dungeon.getCurrentRoomId());
        assertEquals(initialPos, player.getPosition());
    }

    @Test
    void testNoneActionDoesNothing() throws IOException {
        Position initialPos = player.getPosition();
        controller.step(game, GUI.ACTION.QUIT, 100);
        assertTrue(controller.isQuitConfirmation());
        controller.step(game, GUI.ACTION.NONE, 100);
        assertEquals(initialPos, player.getPosition());
    }

    @Test
    void testPlayerMovementToSamePositionDoesNothing() throws IOException {
        Position pos = new Position(0, 0);
        player.setPosition(pos);
        controller.step(game, GUI.ACTION.UP, 100);
        assertTrue(player.getPosition().getY() >= 0);
    }

    @Test
    void testMonsterTimeUpdateOccurs() throws IOException {
        Monster slime = new Monster(20, 20, MonsterType.SLIME);
        slime.setLastMoveTime(0);
        dungeon.addMonster(slime);
        controller.step(game, GUI.ACTION.NONE, 5000);
        assertTrue(slime.getLastMoveTime() > 0);
    }

    @Test
    void testDoorOverlapDetection() throws IOException {
        Room targetRoom = new Room("target_room");
        dungeon.addRoom(targetRoom);
        Position doorPos = new Position(51, 50);
        Door door = new Door(doorPos.getX(), doorPos.getY(), "target_room", new Position(5, 5));
        dungeon.getCurrentRoom().addDoor(door);
        controller.step(game, GUI.ACTION.RIGHT, 100);
        assertEquals("target_room", dungeon.getCurrentRoomId());
    }
}
