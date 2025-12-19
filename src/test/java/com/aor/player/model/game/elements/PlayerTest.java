package com.aor.player.model.game.elements;

import com.aor.player.model.game.items.Armor;
import com.aor.player.model.game.items.HealthPotion;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.items.Sword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(5, 10);
    }

    // ========== Constructor Tests ==========

    @Test
    void testConstructorSetsPosition() {
        Player p = new Player(15, 20);

        assertEquals(15, p.getPosition().getX());
        assertEquals(20, p.getPosition().getY());
    }

    @Test
    void testConstructorInitializesDefaultStats() {
        assertEquals(100, player.getHealth());
        assertEquals(100, player.getMaxHealth());
        assertEquals(10, player.getArmor()); // base armor
        assertEquals(10, player.getAttack()); // base attack
        assertEquals(100, player.getGold());
    }

    @Test
    void testConstructorInitializesEmptyInventory() {
        List<Item> inventory = player.getInventory();

        assertNotNull(inventory);
        assertTrue(inventory.isEmpty());
    }

    @Test
    void testConstructorInitializesNoEquipment() {
        assertNull(player.getEquippedSword());
        assertNull(player.getEquippedArmor());
    }

    // ========== Health Tests ==========

    @Test
    void testGetHealth() {
        assertEquals(100, player.getHealth());
    }

    @Test
    void testHealIncreasesHealth() {
        player.takeDamage(50);
        int healthBefore = player.getHealth();

        player.heal(20);

        assertEquals(healthBefore + 20, player.getHealth());
    }

    @Test
    void testHealDoesNotExceedMaxHealth() {
        player.takeDamage(10);

        player.heal(200);

        assertEquals(player.getMaxHealth(), player.getHealth());
    }

    @Test
    void testTakeDamageReducesHealth() {
        int initialHealth = player.getHealth();

        player.takeDamage(30);

        assertTrue(player.getHealth() < initialHealth);
    }

    @Test
    void testTakeDamageAccountsForArmor() {
        // Base armor is 10, so 30 damage - 10 armor = 20 actual damage
        // But minimum damage is 1
        player.takeDamage(30);

        int expectedHealth = 100 - 20; // 30 - 10 = 20
        assertEquals(expectedHealth, player.getHealth());
    }

    @Test
    void testTakeDamageMinimumOne() {
        // Even if armor is higher than damage, at least 1 damage is dealt
        player.takeDamage(5); // 5 - 10 armor = -5, but minimum is 1

        assertEquals(99, player.getHealth());
    }

    @Test
    void testReduceHealthDirectlyReducesHealth() {
        player.reduceHealth(15);

        assertEquals(85, player.getHealth());
    }

    @Test
    void testReduceHealthCannotGoBelowZero() {
        player.reduceHealth(150);

        assertEquals(0, player.getHealth());
    }

    @Test
    void testTakeDamageCannotGoBelowZero() {
        player.takeDamage(500);

        assertEquals(0, player.getHealth());
    }

    @Test
    void testIsDeadReturnsTrueWhenHealthIsZero() {
        player.takeDamage(1000);

        assertTrue(player.isDead());
    }

    @Test
    void testIsDeadReturnsFalseWhenHealthAboveZero() {
        assertFalse(player.isDead());
    }

    @Test
    void testMaxHealthGetterAndSetter() {
        assertEquals(100, player.getMaxHealth());

        player.increaseMaxHealth(50);

        assertEquals(150, player.getMaxHealth());
    }

    @Test
    void testIncreaseMaxHealth() {
        player.increaseMaxHealth(25);

        assertEquals(125, player.getMaxHealth());
    }

    // ========== Gold Tests ==========

    @Test
    void testGetGold() {
        assertEquals(100, player.getGold());
    }

    @Test
    void testAddGold() {
        player.addGold(50);

        assertEquals(150, player.getGold());
    }

    @Test
    void testAddMultipleGold() {
        player.addGold(25);
        player.addGold(75);

        assertEquals(200, player.getGold());
    }

    @Test
    void testRemoveGoldSucceedsWhenEnough() {
        boolean result = player.removeGold(50);

        assertTrue(result);
        assertEquals(50, player.getGold());
    }

    @Test
    void testRemoveGoldFailsWhenNotEnough() {
        boolean result = player.removeGold(150);

        assertFalse(result);
        assertEquals(100, player.getGold()); // Gold should not change
    }

    @Test
    void testRemoveExactGoldAmount() {
        boolean result = player.removeGold(100);

        assertTrue(result);
        assertEquals(0, player.getGold());
    }

    // ========== Inventory Tests ==========

    @Test
    void testAddItemToInventory() {
        Item sword = new Sword();

        player.addItem(sword);

        assertEquals(1, player.getInventory().size());
        assertTrue(player.getInventory().contains(sword));
    }

    @Test
    void testAddMultipleItems() {
        Item sword = new Sword();
        Item armor = new Armor();
        Item potion = new HealthPotion();

        player.addItem(sword);
        player.addItem(armor);
        player.addItem(potion);

        assertEquals(3, player.getInventory().size());
    }

    @Test
    void testGetInventoryReturnsDefensiveCopy() {
        Item sword = new Sword();
        player.addItem(sword);

        List<Item> inventory1 = player.getInventory();
        List<Item> inventory2 = player.getInventory();

        assertNotSame(inventory1, inventory2);
    }

    @Test
    void testRemoveItemFromInventory() {
        Item sword = new Sword();
        Item armor = new Armor();
        player.addItem(sword);
        player.addItem(armor);

        player.removeItem(0); // Remove first item

        assertEquals(1, player.getInventory().size());
        assertFalse(player.getInventory().contains(sword));
    }

    @Test
    void testRemoveItemWithInvalidIndexDoesNothing() {
        Item sword = new Sword();
        player.addItem(sword);

        player.removeItem(5); // Invalid index

        assertEquals(1, player.getInventory().size());
    }

    @Test
    void testRemoveItemWithNegativeIndexDoesNothing() {
        Item sword = new Sword();
        player.addItem(sword);

        player.removeItem(-1);

        assertEquals(1, player.getInventory().size());
    }

    // ========== Equipment Tests ==========

    @Test
    void testEquipSword() {
        Sword sword = new Sword("Test Sword", 15);

        player.equipSword(sword);

        assertEquals(sword, player.getEquippedSword());
    }

    @Test
    void testUnequipSword() {
        Sword sword = new Sword();
        player.equipSword(sword);

        player.unequipSword();

        assertNull(player.getEquippedSword());
    }

    @Test
    void testEquipArmor() {
        Armor armor = new Armor("Test Armor", 20);

        player.equipArmor(armor);

        assertEquals(armor, player.getEquippedArmor());
    }

    @Test
    void testUnequipArmor() {
        Armor armor = new Armor();
        player.equipArmor(armor);

        player.unequipArmor();

        assertNull(player.getEquippedArmor());
    }

    @Test
    void testEquipSwordReplacesOldSword() {
        Sword sword1 = new Sword("Sword 1", 10);
        Sword sword2 = new Sword("Sword 2", 20);

        player.equipSword(sword1);
        player.equipSword(sword2);

        assertEquals(sword2, player.getEquippedSword());
    }

    @Test
    void testEquipArmorReplacesOldArmor() {
        Armor armor1 = new Armor("Armor 1", 10);
        Armor armor2 = new Armor("Armor 2", 20);

        player.equipArmor(armor1);
        player.equipArmor(armor2);

        assertEquals(armor2, player.getEquippedArmor());
    }

    // ========== Attack Tests ==========

    @Test
    void testGetAttackWithNoSword() {
        assertEquals(10, player.getAttack()); // Base attack
    }

    @Test
    void testGetAttackWithSword() {
        Sword sword = new Sword("Power Sword", 25);
        player.equipSword(sword);

        assertEquals(35, player.getAttack()); // 10 base + 25 sword
    }

    @Test
    void testIncreaseAttack() {
        player.increaseAttack(15);

        assertEquals(25, player.getAttack());
    }

    @Test
    void testIncreaseAttackWithSwordEquipped() {
        Sword sword = new Sword("Sword", 10);
        player.equipSword(sword);
        player.increaseAttack(5);

        assertEquals(25, player.getAttack()); // 15 base + 10 sword
    }

    // ========== Armor Tests ==========

    @Test
    void testGetArmorWithNoArmor() {
        assertEquals(10, player.getArmor()); // Base armor
    }

    @Test
    void testGetArmorWithArmor() {
        Armor armor = new Armor("Heavy Armor", 30);
        player.equipArmor(armor);

        assertEquals(40, player.getArmor()); // 10 base + 30 armor
    }

    @Test
    void testIncreaseBaseArmor() {
        player.increaseBaseArmor(20);

        assertEquals(30, player.getArmor());
    }

    @Test
    void testIncreaseBaseArmorWithArmorEquipped() {
        Armor armor = new Armor("Armor", 15);
        player.equipArmor(armor);
        player.increaseBaseArmor(10);

        assertEquals(35, player.getArmor()); // 20 base + 15 armor
    }

    // ========== Sprite Dimension Tests ==========

    @Test
    void testGetSpriteWidth() {
        assertEquals(7, player.getSpriteWidth());
    }

    @Test
    void testGetSpriteHeight() {
        assertEquals(7, player.getSpriteHeight());
    }

    // ========== Integration Tests ==========

    @Test
    void testCombatScenario() {
        // Equip good gear
        player.equipSword(new Sword("Strong Sword", 20));
        player.equipArmor(new Armor("Strong Armor", 25));

        // Take damage
        player.takeDamage(50);

        // Attack should be 30 (10 base + 20 sword)
        assertEquals(30, player.getAttack());

        // Armor should be 35 (10 base + 25 armor)
        assertEquals(35, player.getArmor());

        // Damage should be reduced by armor: max(1, 50 - 35) = 15
        assertEquals(85, player.getHealth());
    }

    @Test
    void testInventoryAndEquipmentFlow() {
        Sword sword = new Sword("Epic Sword", 30);
        Armor armor = new Armor("Epic Armor", 40);

        // Add to inventory
        player.addItem(sword);
        player.addItem(armor);

        // Equip items
        player.equipSword(sword);
        player.equipArmor(armor);

        // Verify equipment
        assertEquals(sword, player.getEquippedSword());
        assertEquals(armor, player.getEquippedArmor());

        // Stats should reflect equipment
        assertEquals(40, player.getAttack()); // 10 + 30
        assertEquals(50, player.getArmor()); // 10 + 40
    }

    @Test
    void testHealingAndMaxHealthIncrease() {
        // Take damage (60 damage - 10 armor = 50 actual damage)
        player.takeDamage(60);
        assertEquals(50, player.getHealth());

        // Increase max health
        player.increaseMaxHealth(50);
        assertEquals(150, player.getMaxHealth());
        assertEquals(50, player.getHealth()); // Current health unchanged

        // Heal
        player.heal(70);
        assertEquals(120, player.getHealth());

        // Heal beyond max
        player.heal(100);
        assertEquals(150, player.getHealth());
    }
}
