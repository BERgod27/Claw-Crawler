package com.aor.player.model.game.items;

import org.junit.jupiter.api.Test;
import com.aor.player.model.game.elements.Player;

import static org.junit.jupiter.api.Assertions.*;

class ArmorTest {

    @Test
    void testDefaultConstructor() {
        Armor armor = new Armor();

        assertEquals("Iron Armor", armor.getName());
        assertEquals(50, armor.getPrice());
        assertEquals(15, armor.getStatBonus());
        assertEquals("Increases armor by 15", armor.getDescription());
    }

    @Test
    void testCustomConstructor() {
        Armor armor = new Armor("Dragon Scale Armor", 40);

        assertEquals("Dragon Scale Armor", armor.getName());
        assertEquals(50, armor.getPrice());
        assertEquals(40, armor.getStatBonus());
        assertEquals("Increases armor by 40", armor.getDescription());
    }

    @Test
    void testGetStatBonusReturnsArmorBonus() {
        Armor lightArmor = new Armor("Leather Armor", 5);
        Armor heavyArmor = new Armor("Plate Armor", 30);

        assertEquals(5, lightArmor.getStatBonus());
        assertEquals(30, heavyArmor.getStatBonus());
    }

    @Test
    void testDescriptionIncludesArmorBonus() {
        Armor armor = new Armor("Test Armor", 20);

        assertTrue(armor.getDescription().contains("20"));
        assertTrue(armor.getDescription().toLowerCase().contains("armor"));
    }

    @Test
    void testEquipCallsPlayerEquipArmor() {
        Armor armor = new Armor();
        Player player = new Player(0, 0);

        armor.equip(player);

        assertEquals(armor, player.getEquippedArmor());
    }

    @Test
    void testUnequipCallsPlayerUnequipArmor() {
        Armor armor = new Armor();
        Player player = new Player(0, 0);

        player.equipArmor(armor);
        armor.unequip(player);

        assertNull(player.getEquippedArmor());
    }

    @Test
    void testImplementsItemInterface() {
        Armor armor = new Armor();

        assertTrue(armor instanceof Item);
    }

    @Test
    void testImplementsEquippableInterface() {
        Armor armor = new Armor();

        assertTrue(armor instanceof Equippable);
    }

    @Test
    void testZeroArmorBonus() {
        Armor armor = new Armor("Cloth Armor", 0);

        assertEquals(0, armor.getStatBonus());
    }

    @Test
    void testLargeArmorBonus() {
        Armor armor = new Armor("Mythical Armor", 999);

        assertEquals(999, armor.getStatBonus());
    }
}
