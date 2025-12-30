package com.aor.player.states;

import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.MonsterType;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.shop.Shop;
import com.aor.player.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StateFactoryTest {
    private StateFactory stateFactory;
    private Dungeon mockDungeon;
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        stateFactory = new StateFactory();
        mockDungeon = mock(Dungeon.class);
        mockPlayer = mock(Player.class);
        when(mockDungeon.getPlayer()).thenReturn(mockPlayer);
        when(mockDungeon.getShop()).thenReturn(new Shop());
        when(mockDungeon.getTimePlayed()).thenReturn(60000L);
        when(mockDungeon.getTotalGold()).thenReturn(100);
        when(mockDungeon.getSlimesKilled()).thenReturn(5);
    }

    @Test
    void testCreateShopState() {
        ShopState shopState = stateFactory.createShopState(mockDungeon);
        assertNotNull(shopState);
        assertNotNull(shopState.getModel());
        assertEquals(Shop.class, shopState.getModel().getClass());
    }

    @Test
    void testCreateInventoryState() {
        InventoryState inventoryState = stateFactory.createInventoryState(mockDungeon);
        assertNotNull(inventoryState);
        assertEquals(mockDungeon, inventoryState.getModel());
    }

    @Test
    void testCreateGameState() {
        GameState gameState = stateFactory.createGameState(mockDungeon);
        assertNotNull(gameState);
        assertEquals(mockDungeon, gameState.getModel());
    }

    @Test
    void testCreateCombatState() {
        Monster monster = mock(Monster.class);
        when(monster.getType()).thenReturn(MonsterType.SLIME);
        when(monster.getPosition()).thenReturn(new Position(0, 0));
        CombatState combatState = stateFactory.createCombatState(mockDungeon, monster);
        assertNotNull(combatState);
        assertEquals(mockDungeon, combatState.getModel());
    }

    @Test
    void testCreateMenuState() {
        MenuState menuState = stateFactory.createMenuState();
        assertNotNull(menuState);
        assertNotNull(menuState.getModel());
    }

    @Test
    void testCreateInstructionsState() {
        InstructionsState instructionsState = stateFactory.createInstructionsState();
        assertNotNull(instructionsState);
        assertNotNull(instructionsState.getModel());
    }

    @Test
    void testCreateVictoryState() {
        VictoryState victoryState = stateFactory.createVictoryState(mockDungeon);
        assertNotNull(victoryState);
        assertNotNull(victoryState.getModel());
        verify(mockDungeon).getTimePlayed();
        verify(mockDungeon).getTotalGold();
        verify(mockDungeon).getSlimesKilled();
    }
}
