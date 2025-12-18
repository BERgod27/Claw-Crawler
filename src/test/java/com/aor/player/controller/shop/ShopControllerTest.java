package com.aor.player.controller.shop;

import com.aor.player.Game;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.dungeon.Room;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.shop.Shop;
import com.aor.player.states.StateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopControllerTest {
    private ShopController controller;
    private Dungeon dungeon;
    private Player player;
    private Game game;
    private StateFactory stateFactory;

    @BeforeEach
    void setUp() {
        dungeon = new Dungeon(100, 100);
        player = new Player(50, 50);
        player.addGold(1000); // Give player gold for purchases
        dungeon.setPlayer(player);

        Room room = new Room("test_room");
        dungeon.addRoom(room);
        dungeon.setCurrentRoomId("test_room");

        stateFactory = Mockito.mock(StateFactory.class);
        controller = new ShopController(dungeon, stateFactory);
        game = Mockito.mock(Game.class);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        assertNotNull(controller.getModel());
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testNavigateUp() throws IOException {
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(1, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.UP, 100);
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testNavigateDown() throws IOException {
        assertEquals(0, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(1, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(2, controller.getSelectedIndex());
    }

    @Test
    void testNavigateDownBoundary() throws IOException {
        Shop shop = controller.getModel();
        int maxIndex = shop.getAvailableOfferings().size() - 1;

        // Navigate to last item
        for (int i = 0; i <= maxIndex + 2; i++) {
            controller.step(game, GUI.ACTION.DOWN, 100);
        }

        // Should not go beyond last item
        assertEquals(maxIndex, controller.getSelectedIndex());
    }

    @Test
    void testNavigateUpBoundary() throws IOException {
        assertEquals(0, controller.getSelectedIndex());

        // Should not go below 0
        controller.step(game, GUI.ACTION.UP, 100);
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testPurchaseItem() throws IOException {
        int initialGold = player.getGold();
        int initialHealth = player.getHealth();

        // Purchase first item (Health Potion)
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Player should have less gold and restored health
        assertTrue(player.getGold() < initialGold);
        assertTrue(player.getHealth() >= initialHealth); // Health should be same or higher
    }

    @Test
    void testCannotPurchaseWithoutGold() throws IOException {
        // Remove all gold
        player.addGold(-player.getGold());
        assertEquals(0, player.getGold());

        int initialHealth = player.getMaxHealth();

        // Try to purchase
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Should fail, stats unchanged
        assertEquals(initialHealth, player.getMaxHealth());
        assertEquals(0, player.getGold());
    }

    @Test
    void testExitShop() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(Mockito.mock(com.aor.player.states.GameState.class));

        controller.step(game, GUI.ACTION.QUIT, 100);

        verify(stateFactory, times(1)).createGameState(dungeon);
        verify(game, times(1)).setState(any());
    }

    @Test
    void testNoActionForLeftRight() throws IOException {
        assertEquals(0, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.LEFT, 100);
        assertEquals(0, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.RIGHT, 100);
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testNoActionForNone() throws IOException {
        assertEquals(0, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.NONE, 100);
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testNoActionForInventory() throws IOException {
        assertEquals(0, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.INVENTORY, 100);
        assertEquals(0, controller.getSelectedIndex());
    }

    @Test
    void testGetModel() {
        assertNotNull(controller.getModel());
        assertTrue(controller.getModel() instanceof Shop);
    }

    @Test
    void testMultipleNavigations() throws IOException {
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(2, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.UP, 100);
        assertEquals(1, controller.getSelectedIndex());

        controller.step(game, GUI.ACTION.UP, 100);
        controller.step(game, GUI.ACTION.UP, 100);
        assertEquals(0, controller.getSelectedIndex());
    }
}
