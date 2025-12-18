package com.aor.player.controller.combat;

import com.aor.player.Game;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.MonsterType;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.items.HealthPotion;
import com.aor.player.states.StateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CombatControllerTest {
    private CombatController controller;
    private Dungeon dungeon;
    private Monster monster;
    private Player player;
    private Game game;
    private StateFactory stateFactory;

    @BeforeEach
    void setUp() {
        dungeon = new Dungeon(100, 100);
        player = new Player(50, 50);
        dungeon.setPlayer(player);

        monster = new Monster(10, 10, MonsterType.SLIME);
        stateFactory = Mockito.mock(StateFactory.class);
        controller = new CombatController(dungeon, monster, stateFactory);
        game = Mockito.mock(Game.class);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        assertEquals(monster, controller.getMonster());
        assertFalse(controller.getCombatLog().isEmpty());
        assertTrue(controller.getCombatLog().get(0).contains("Combat started"));
    }

    @Test
    void testNavigateUp() throws IOException {
        controller.step(game, GUI.ACTION.DOWN, 100); // Move to action 1
        assertEquals(1, controller.getSelectedAction());

        controller.step(game, GUI.ACTION.UP, 100); // Move back to action 0
        assertEquals(0, controller.getSelectedAction());
    }

    @Test
    void testNavigateDown() throws IOException {
        assertEquals(0, controller.getSelectedAction());

        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(1, controller.getSelectedAction());

        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(2, controller.getSelectedAction());

        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(3, controller.getSelectedAction());

        // Should not go beyond action 3
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(3, controller.getSelectedAction());
    }

    @Test
    void testNavigateUpBoundary() throws IOException {
        assertEquals(0, controller.getSelectedAction());

        // Should not go below 0
        controller.step(game, GUI.ACTION.UP, 100);
        assertEquals(0, controller.getSelectedAction());
    }

    @Test
    void testQuitAction() throws IOException {
        controller.step(game, GUI.ACTION.QUIT, 100);
        verify(game, times(1)).setState(null);
    }

    @Test
    void testAttackAction() throws IOException {
        int initialMonsterHealth = monster.getHealth();

        // Select attack (action 0) and execute
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Monster should take damage
        assertTrue(monster.getHealth() < initialMonsterHealth);

        // Combat log should record the attack
        assertTrue(controller.getCombatLog().stream()
                .anyMatch(log -> log.contains("hit") || log.contains("damage")));
    }

    @Test
    void testDefendAction() throws IOException {
        int initialHealth = player.getHealth();

        // Select defend (action 1)
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(1, controller.getSelectedAction());

        // Execute defend
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Combat log should show defense
        assertTrue(controller.getCombatLog().stream()
                .anyMatch(log -> log.contains("brace") || log.contains("block")));
    }

    @Test
    void testUseItemWithPotion() throws IOException {
        // Damage player first
        player.takeDamage(20);
        int damagedHealth = player.getHealth();

        // Add a potion to inventory
        HealthPotion potion = new HealthPotion("Health Potion", 30);
        player.addItem(potion);

        // Select use item (action 2)
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(2, controller.getSelectedAction());

        // Execute use item
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Player should be healed
        assertTrue(player.getHealth() > damagedHealth);
    }

    @Test
    void testUseItemWithNoItems() throws IOException {
        // Make sure inventory is empty
        player.getInventory().clear();

        // Select use item (action 2)
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);

        // Execute use item with empty inventory
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Should show message about no items
        assertTrue(controller.getCombatLog().stream()
                .anyMatch(log -> log.contains("No usable items")));
    }

    @Test
    void testFleeAction() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(Mockito.mock(com.aor.player.states.GameState.class));

        // Select flee (action 3)
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(3, controller.getSelectedAction());

        // Execute flee
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Should transition back to game state
        verify(stateFactory, times(1)).createGameState(dungeon);
        verify(game, times(1)).setState(any());
    }

    @Test
    void testVictoryCondition() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(Mockito.mock(com.aor.player.states.GameState.class));

        // Damage monster until it dies
        while (!monster.isDead()) {
            monster.takeDamage(100);
        }

        // Execute any action after monster is dead
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Press Q to close combat screen after victory
        controller.step(game, GUI.ACTION.QUIT, 100);

        // Should transition to game state
        verify(game, times(1)).setState(any());
    }

    @Test
    void testDefeatCondition() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(Mockito.mock(com.aor.player.states.GameState.class));

        // Damage player until death
        while (!player.isDead()) {
            player.takeDamage(100);
        }

        // Execute any action after player is dead (triggers defeat)
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Now need to press Q to close combat screen
        controller.step(game, GUI.ACTION.QUIT, 100);

        // Should transition to game state after pressing Q
        verify(game, times(1)).setState(any());
    }

    @Test
    void testCombatLogLimit() throws IOException {
        // Generate many combat actions
        for (int i = 0; i < 10; i++) {
            controller.step(game, GUI.ACTION.SELECT, 100);
        }

        // Log should be limited to MAX_LOG_ENTRIES (5)
        assertTrue(controller.getCombatLog().size() <= 6); // 5 + initial message
    }

    @Test
    void testMonsterTurn() throws IOException {
        int initialHealth = player.getHealth();

        // Attack the monster (which triggers monster turn)
        controller.step(game, GUI.ACTION.SELECT, 100);

        // Player should take damage from monster counter-attack
        assertTrue(player.getHealth() < initialHealth);
    }

    @Test
    void testGetMonster() {
        assertEquals(monster, controller.getMonster());
        assertEquals(MonsterType.SLIME, controller.getMonster().getType());
    }
}
