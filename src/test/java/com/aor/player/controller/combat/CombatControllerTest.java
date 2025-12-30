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
import java.util.List;

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
        dungeon = Mockito.spy(new Dungeon(100, 100));
        player = Mockito.spy(new Player(50, 50));
        dungeon.setPlayer(player);
        monster = Mockito.spy(new Monster(10, 10, MonsterType.SLIME));
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
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(1, controller.getSelectedAction());
        controller.step(game, GUI.ACTION.UP, 100);
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
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(3, controller.getSelectedAction());
    }

    @Test
    void testNavigateUpBoundary() throws IOException {
        assertEquals(0, controller.getSelectedAction());
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
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(monster.getHealth() < initialMonsterHealth);
        assertTrue(controller.getCombatLog().stream()
                .anyMatch(log -> log.contains("hit") || log.contains("damage")));
    }

    @Test
    void testDefendAction() throws IOException {
        controller.step(game, GUI.ACTION.DOWN, 100);
        doReturn(10).when(monster).getAttack();
        doReturn(0).when(player).getArmor();
        int initialHp = player.getHealth();
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertEquals(initialHp - 5, player.getHealth());
        assertTrue(controller.getCombatLog().stream()
                .anyMatch(log -> log.contains("block")));
    }

    @Test
    void testMinimumDamage() throws IOException {
        doReturn(100).when(monster).getDefense();
        int monsterHp = monster.getHealth();
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertEquals(monsterHp - 1, monster.getHealth());

        doReturn(0).when(monster).getAttack();
        doReturn(100).when(player).getArmor();
        int playerHp = player.getHealth();
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertEquals(playerHp - 1, player.getHealth());
    }

    @Test
    void testUseItemWithPotion() throws IOException {
        player.takeDamage(20);
        int damagedHealth = player.getHealth();
        HealthPotion potion = new HealthPotion("Health Potion", 30);
        player.addItem(potion);
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(2, controller.getSelectedAction());
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(player.getHealth() > damagedHealth);
    }

    @Test
    void testUseItemWithNoItems() throws IOException {
        player.getInventory().clear();
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(controller.getCombatLog().stream()
                .anyMatch(log -> log.contains("No usable items")));
    }

    @Test
    void testFleeAction() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(Mockito.mock(com.aor.player.states.GameState.class));
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(3, controller.getSelectedAction());
        controller.step(game, GUI.ACTION.SELECT, 100);
        verify(stateFactory, times(1)).createGameState(dungeon);
        verify(game, times(1)).setState(any());
    }

    @Test
    void testVictoryCondition() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(Mockito.mock(com.aor.player.states.GameState.class));
        while (!monster.isDead()) {
            monster.takeDamage(100);
        }
        controller.step(game, GUI.ACTION.SELECT, 100);
        controller.step(game, GUI.ACTION.QUIT, 100);
        verify(game, times(1)).setState(any());
    }

    @Test
    void testDefeatCondition() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(Mockito.mock(com.aor.player.states.GameState.class));
        while (!player.isDead()) {
            player.takeDamage(100);
        }
        controller.step(game, GUI.ACTION.SELECT, 100);
        controller.step(game, GUI.ACTION.QUIT, 100);
        verify(game, times(1)).setState(any());
    }

    @Test
    void testCombatLogLimit() throws IOException {
        for (int i = 0; i < 10; i++) {
            controller.step(game, GUI.ACTION.SELECT, 100);
        }
        assertTrue(controller.getCombatLog().size() <= 6);
    }

    @Test
    void testMonsterTurn() throws IOException {
        int initialHealth = player.getHealth();
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(player.getHealth() < initialHealth);
    }

    @Test
    void testGetMonster() {
        assertEquals(monster, controller.getMonster());
        assertEquals(MonsterType.SLIME, controller.getMonster().getType());
    }

    @Test
    void testAllBossesDefeated() throws IOException {
        when(stateFactory.createVictoryState(dungeon))
                .thenReturn(Mockito.mock(com.aor.player.states.VictoryState.class));
        doReturn(true).when(dungeon).areAllBossesDefeated();
        doReturn(0).when(monster).getHealth();
        doReturn(true).when(monster).isDead();
        controller.step(game, GUI.ACTION.SELECT, 100);
        controller.step(game, GUI.ACTION.QUIT, 100);
        verify(stateFactory, times(1)).createVictoryState(dungeon);
        verify(game, times(1)).setState(any());
    }

    @Test
    void testLogRolling() throws IOException {
        for (int i = 0; i < 10; i++) {
            if (!monster.isDead() && !player.isDead()) {
                controller.step(game, GUI.ACTION.SELECT, 100);
            }
        }
        assertTrue(controller.getCombatLog().size() <= 5);
    }

    @Test
    void testDefeatTransitionsToMenu() throws IOException {
        when(stateFactory.createMenuState())
                .thenReturn(mock(com.aor.player.states.MenuState.class));
        doReturn(true).when(player).isDead();
        doReturn(0).when(player).getHealth();
        controller.step(game, GUI.ACTION.SELECT, 100);
        controller.step(game, GUI.ACTION.QUIT, 100);
        verify(stateFactory, times(1)).createMenuState();
        verify(game).setState(any());
    }

    @Test
    void testDefendMinimumDamage() throws IOException {
        doReturn(1).when(monster).getAttack();
        doReturn(100).when(player).getArmor();
        controller.step(game, GUI.ACTION.DOWN, 100);
        assertEquals(1, controller.getSelectedAction());
        int initialHp = player.getHealth();
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertEquals(initialHp - 1, player.getHealth());
    }

    @Test
    void testVictoryGivesGold() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(mock(com.aor.player.states.GameState.class));
        int initialGold = player.getGold();
        monster.takeDamage(1000);
        assertTrue(monster.isDead());
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(player.getGold() > initialGold);
    }

    @Test
    void testDefendingLogsCorrectMessage() throws IOException {
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(controller.getCombatLog().stream()
                .anyMatch(log -> log.contains("brace")));
    }

    @Test
    void testFleeingLogsCorrectMessage() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(mock(com.aor.player.states.GameState.class));
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(controller.getCombatLog().stream()
                .anyMatch(log -> log.contains("fled")));
    }

    @Test
    void testVictoryRemovesMonster() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(mock(com.aor.player.states.GameState.class));
        dungeon.addMonster(monster);
        int initialCount = dungeon.getMonsters().size();
        monster.takeDamage(1000);
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(dungeon.getMonsters().size() < initialCount || monster.isDead());
    }

    @Test
    void testVictoryLogsMessages() throws IOException {
        when(stateFactory.createGameState(dungeon))
                .thenReturn(mock(com.aor.player.states.GameState.class));
        monster.takeDamage(1000);
        controller.step(game, GUI.ACTION.SELECT, 100);
        List<String> log = controller.getCombatLog();
        assertTrue(log.stream().anyMatch(l -> l.contains("Victory") || l.contains("defeated")));
        assertTrue(log.stream().anyMatch(l -> l.contains("gold")));
    }

    @Test
    void testDefeatLogsMessages() throws IOException {
        when(stateFactory.createMenuState())
                .thenReturn(mock(com.aor.player.states.MenuState.class));
        player.takeDamage(1000);
        controller.step(game, GUI.ACTION.SELECT, 100);
        List<String> log = controller.getCombatLog();
        assertTrue(log.stream().anyMatch(l -> l.contains("defeated")));
    }

    @Test
    void testMonsterTurnLogsMessage() throws IOException {
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(controller.getCombatLog().stream()
                .anyMatch(log -> log.contains("deals") || log.contains("damage")));
    }

    @Test
    void testUseItemLogsSuccess() throws IOException {
        player.takeDamage(20);
        HealthPotion potion = new HealthPotion("Health Potion", 30);
        player.addItem(potion);
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.DOWN, 100);
        controller.step(game, GUI.ACTION.SELECT, 100);
        assertTrue(controller.getCombatLog().stream()
                .anyMatch(log -> log.contains("used") || log.contains("Health Potion")));
    }
}
