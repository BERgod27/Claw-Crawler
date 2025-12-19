package com.aor.player.model.game.items;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwordTest {

    @Test
    void testDefaultConstructor() {
        Sword sword = new Sword();

        assertEquals("Steel Sword", sword.getName());
        assertEquals(75, sword.getPrice());
        assertEquals(10, sword.getStatBonus());
        assertEquals("Increases attack by 10", sword.getDescription());
    }

    @Test
    void testCustomConstructor() {
        Sword sword = new Sword("Excalibur", 25);

        assertEquals("Excalibur", sword.getName());
        assertEquals(75, sword.getPrice());
        assertEquals(25, sword.getStatBonus());
        assertEquals("Increases attack by 25", sword.getDescription());
    }

    @Test
    void testGetStatBonusReturnsAttackBonus() {
        Sword weakSword = new Sword("Rusty Sword", 5);
        Sword strongSword = new Sword("Legendary Sword", 50);

        assertEquals(5, weakSword.getStatBonus());
        assertEquals(50, strongSword.getStatBonus());
    }

    @Test
    void testDescriptionIncludesAttackBonus() {
        Sword sword = new Sword("Test Sword", 15);

        assertTrue(sword.getDescription().contains("15"));
        assertTrue(sword.getDescription().toLowerCase().contains("attack"));
    }

    @Test
    void testEquipCallsPlayerEquipSword() {
        Sword sword = new Sword();
        com.aor.player.model.game.elements.Player player = new com.aor.player.model.game.elements.Player(0, 0);

        sword.equip(player);

        assertEquals(sword, player.getEquippedSword());
    }

    @Test
    void testUnequipCallsPlayerUnequipSword() {
        Sword sword = new Sword();
        com.aor.player.model.game.elements.Player player = new com.aor.player.model.game.elements.Player(0, 0);

        player.equipSword(sword);
        sword.unequip(player);

        assertNull(player.getEquippedSword());
    }

    @Test
    void testImplementsItemInterface() {
        Sword sword = new Sword();

        assertTrue(sword instanceof Item);
    }

    @Test
    void testImplementsEquippableInterface() {
        Sword sword = new Sword();

        assertTrue(sword instanceof Equippable);
    }

    @Test
    void testZeroAttackBonus() {
        Sword sword = new Sword("Broken Sword", 0);

        assertEquals(0, sword.getStatBonus());
    }

    @Test
    void testLargeAttackBonus() {
        Sword sword = new Sword("Godly Sword", 999);

        assertEquals(999, sword.getStatBonus());
    }
}
