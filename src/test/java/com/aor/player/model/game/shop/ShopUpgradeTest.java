package com.aor.player.model.game.shop;

import com.aor.player.model.game.elements.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ShopUpgradeTest {
    private ShopUpgrade healthUpgrade;
    private ShopUpgrade attackUpgrade;
    private ShopUpgrade armorUpgrade;

    @BeforeEach
    void setUp() {
        healthUpgrade = new ShopUpgrade("HP Boost", "More HP", ShopUpgrade.UpgradeType.HEALTH, 10, 100);
        attackUpgrade = new ShopUpgrade("Attack Boost", "More DMG", ShopUpgrade.UpgradeType.ATTACK, 5, 50);
        armorUpgrade = new ShopUpgrade("Armor Boost", "More Armor", ShopUpgrade.UpgradeType.ARMOR, 2, 75);
    }

    @Test
    void testPriceScaling() {
        // Initial price
        assertEquals(100, healthUpgrade.getCurrentPrice());

        Player player = mock(Player.class);
        healthUpgrade.applyUpgrade(player);

        // Price should increase by 1.15x -> 114 (due to floating point truncation)
        assertEquals(114, healthUpgrade.getCurrentPrice());

        healthUpgrade.applyUpgrade(player);
        // Price should increase again: 100 * 1.15^2 = 132.25 -> 132
        assertEquals(132, healthUpgrade.getCurrentPrice());
    }

    @Test
    void testApplyHealthUpgrade() {
        Player player = mock(Player.class);
        healthUpgrade.applyUpgrade(player);

        verify(player).increaseMaxHealth(10);
        verify(player).heal(10);
        assertEquals(1, healthUpgrade.getPurchaseCount());
    }

    @Test
    void testApplyAttackUpgrade() {
        Player player = mock(Player.class);
        attackUpgrade.applyUpgrade(player);

        verify(player).increaseAttack(5);
        assertEquals(1, attackUpgrade.getPurchaseCount());
    }

    @Test
    void testApplyArmorUpgrade() {
        Player player = mock(Player.class);
        armorUpgrade.applyUpgrade(player);

        verify(player).increaseBaseArmor(2);
        assertEquals(1, armorUpgrade.getPurchaseCount());
    }

    @Test
    void testGettersAndDescription() {
        assertEquals("HP Boost", healthUpgrade.getName());
        assertEquals("More HP (+10 Max Health)", healthUpgrade.getDescription());
        assertEquals(ShopUpgrade.UpgradeType.HEALTH, healthUpgrade.getType());
        assertEquals(10, healthUpgrade.getStatIncrease());
    }
}
