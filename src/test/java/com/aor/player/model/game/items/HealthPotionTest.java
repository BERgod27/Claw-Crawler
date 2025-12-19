package com.aor.player.model.game.items;

import com.aor.player.model.game.elements.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthPotionTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(0, 0);
    }

    @Test
    void testDefaultConstructor() {
        HealthPotion potion = new HealthPotion();

        assertEquals("Health Potion", potion.getName());
        assertEquals(10, potion.getPrice());
        assertEquals(20, potion.getHealAmount());
        assertEquals("Restores 20 HP", potion.getDescription());
    }

    @Test
    void testCustomConstructor() {
        HealthPotion potion = new HealthPotion("Greater Health Potion", 50);

        assertEquals("Greater Health Potion", potion.getName());
        assertEquals(10, potion.getPrice());
        assertEquals(50, potion.getHealAmount());
        assertEquals("Restores 50 HP", potion.getDescription());
    }

    @Test
    void testGetHealAmount() {
        HealthPotion smallPotion = new HealthPotion("Small Potion", 10);
        HealthPotion largePotion = new HealthPotion("Large Potion", 100);

        assertEquals(10, smallPotion.getHealAmount());
        assertEquals(100, largePotion.getHealAmount());
    }

    @Test
    void testDescriptionIncludesHealAmount() {
        HealthPotion potion = new HealthPotion("Test Potion", 35);

        assertTrue(potion.getDescription().contains("35"));
        assertTrue(potion.getDescription().toUpperCase().contains("HP"));
    }

    @Test
    void testUseHealsPlayer() {
        HealthPotion potion = new HealthPotion("Test Potion", 30);

        // Damage player first
        player.takeDamage(50);
        int healthBeforeUse = player.getHealth();

        potion.use(player);

        int healthAfterUse = player.getHealth();
        assertTrue(healthAfterUse > healthBeforeUse);
    }

    @Test
    void testUseHealsCorrectAmount() {
        HealthPotion potion = new HealthPotion("Test Potion", 25);

        // Damage player
        player.takeDamage(40);
        int healthBefore = player.getHealth();

        potion.use(player);

        assertEquals(healthBefore + 25, player.getHealth());
    }

    @Test
    void testUseDoesNotExceedMaxHealth() {
        HealthPotion potion = new HealthPotion("Large Potion", 200);

        // Damage player slightly
        player.takeDamage(10);

        potion.use(player);

        assertEquals(player.getMaxHealth(), player.getHealth());
    }

    @Test
    void testImplementsItemInterface() {
        HealthPotion potion = new HealthPotion();

        assertTrue(potion instanceof Item);
    }

    @Test
    void testImplementsUsableInterface() {
        HealthPotion potion = new HealthPotion();

        assertTrue(potion instanceof Usable);
    }

    @Test
    void testZeroHealAmount() {
        HealthPotion potion = new HealthPotion("Broken Potion", 0);

        assertEquals(0, potion.getHealAmount());
    }

    @Test
    void testUseOnFullHealthPlayer() {
        HealthPotion potion = new HealthPotion("Test Potion", 20);

        int maxHealth = player.getMaxHealth();
        assertEquals(maxHealth, player.getHealth()); // Player starts at full health

        potion.use(player);

        assertEquals(maxHealth, player.getHealth()); // Should still be at max
    }

    @Test
    void testLargeHealAmount() {
        HealthPotion potion = new HealthPotion("Ultimate Potion", 9999);

        assertEquals(9999, potion.getHealAmount());
    }
}
