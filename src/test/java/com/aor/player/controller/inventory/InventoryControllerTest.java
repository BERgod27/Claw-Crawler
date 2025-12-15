package com.aor.player.controller.inventory;

import com.aor.player.Game;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.dungeon.Room;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.items.Armor;
import com.aor.player.model.game.items.HealthPotion;
import com.aor.player.model.game.items.Sword;
import com.aor.player.states.StateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryControllerTest {
    private InventoryController controller;
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
        controller = new InventoryController(dungeon, stateFactory);
        game = Mockito.mock(Game.class);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        assertEquals(dungeon, controller.getModel());
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testNavigateUp() throws IOException {
        player.addItem(new HealthPotion("Potion 1", 10));
        player.addItem(new HealthPotion("Potion 2", 20));

        // Move to second item
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(1, controller.getSelectedIndex());

        // Move back to first item
        controller.step(game, GUI.ACTION.UP, 100);
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testNavigateDown() throws IOException {
        player.addItem(new HealthPotion("Potion 1", 10));
        player.addItem(new HealthPotion("Potion 2", 20));
        player.addItem(new HealthPotion("Potion 3", 30));

        assertEquals(0, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(1, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(2, controller.getSelectedIndex());

        // Should not go beyond last item
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(2, controller.getSelectedIndex());
    }

    @Test
    void testNavigateUpBoundary() throws IOException {
        player.addItem(new HealthPotion("Potion", 10));

        assertEquals(0, controller.getSelectedIndex());

        // Should not go below 0
        controller.step(game, GUI.ACTION.UP, 100);
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testCloseInventoryWithQuit() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(Mockito.mock(com.aor.player.states.GameState.class));

        controller.step(game, GUI.ACTION.QUIT, 100);

        verify(stateFactory, times(1)).createGameState(dungeon);
        verify(game, times(1)).setState(any());
    }

    @Test
    void testCloseInventoryWithInventoryKey() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(Mockito.mock(com.aor.player.states.GameState.class));

        controller.step(game, GUI.ACTION.INVENTORY, 100);

        verify(stateFactory, times(1)).createGameState(dungeon);
        verify(game, times(1)).setState(any());
    }

    @Test
    void testUsePotion() throws IOException {
        // Damage player first
        player.takeDamage(30);
        int damagedHealth = player.getHealth();

        // Add potion to inventory
        HealthPotion potion = new HealthPotion("Health Potion", 20);
        player.addItem(potion);

        // Use the potion
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Player should be healed
        assertTrue(player.getHealth() > damagedHealth);

        // Potion should be removed from inventory
        assertTrue(player.getInventory().isEmpty());
    }

    @Test
    void testEquipSword() throws IOException {
        Sword sword = new Sword("Iron Sword", 10);
        player.addItem(sword);

        assertNull(player.getEquippedSword());

        // Equip the sword
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Sword should be equipped
        assertEquals(sword, player.getEquippedSword());

        // Sword should stay in inventory
        assertFalse(player.getInventory().isEmpty());
    }

    @Test
    void testEquipArmor() throws IOException {
        Armor armor = new Armor("Iron Armor", 5);
        player.addItem(armor);

        assertNull(player.getEquippedArmor());

        // Equip the armor
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Armor should be equipped
        assertEquals(armor, player.getEquippedArmor());

        // Armor should stay in inventory
        assertFalse(player.getInventory().isEmpty());
    }

    @Test
    void testSelectItemAfterUse() throws IOException {
        // Add two potions
        player.addItem(new HealthPotion("Potion 1", 10));
        player.addItem(new HealthPotion("Potion 2", 20));

        // Move to second potion
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(1, controller.getSelectedIndex());

        // Use the second potion
        player.takeDamage(15);
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Selected index should adjust back to 0
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testEmptyInventory() throws IOException {
        assertTrue(player.getInventory().isEmpty());

        // Trying to select with empty inventory should not crash
        controller.step(game, GUI.ACTION.SELECT, 100);

        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testNoActionForLeftRight() throws IOException {
        player.addItem(new HealthPotion("Potion", 10));

        assertEquals(0, controller.getSelectedIndex());

        // LEFT and RIGHT should do nothing
        controller.step(game, GUI.ACTION.LEFT, 100);
        assertEquals(0, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.RIGHT, 100);
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testNoActionForNone() throws IOException {
        player.addItem(new HealthPotion("Potion", 10));

        assertEquals(0, controller.getSelectedIndex());

        // NONE should do nothing
        controller.step(game, GUI.ACTION.NONE, 100);
        assertEquals(0, controller.getSelectedIndex());
    }
}
