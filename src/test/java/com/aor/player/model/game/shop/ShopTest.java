package com.aor.player.model.game.shop;

import com.aor.player.model.game.elements.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopTest {
    private Shop shop;

    @BeforeEach
    void setUp() {
        shop = new Shop();
    }

    @Test
    void testInitialization() {
        // Shop should start with default offerings: 1 potion + 3 upgrades
        List<ShopOffering> offerings = shop.getAvailableOfferings();
        assertEquals(4, offerings.size());

        // Verify types
        long itemOffers = offerings.stream()
                .filter(o -> o.getType() == ShopOffering.OfferingType.ITEM).count();
        long upgradeOffers = offerings.stream()
                .filter(o -> o.getType() == ShopOffering.OfferingType.UPGRADE).count();

        assertEquals(1, itemOffers);
        assertEquals(3, upgradeOffers);
    }

    @Test
    void testGetAvailableUpgrades() {
        List<ShopUpgrade> upgrades = shop.getAvailableUpgrades();
        assertEquals(3, upgrades.size());

        // Check for specific upgrades
        boolean hasHealth = upgrades.stream().anyMatch(u -> u.getType() == ShopUpgrade.UpgradeType.HEALTH);
        boolean hasAttack = upgrades.stream().anyMatch(u -> u.getType() == ShopUpgrade.UpgradeType.ATTACK);
        boolean hasArmor = upgrades.stream().anyMatch(u -> u.getType() == ShopUpgrade.UpgradeType.ARMOR);

        assertTrue(hasHealth);
        assertTrue(hasAttack);
        assertTrue(hasArmor);
    }

    @Test
    void testCanAfford() {
        Player player = mock(Player.class);
        when(player.getGold()).thenReturn(100);

        ShopOffering affordable = mock(ShopOffering.class);
        when(affordable.getPrice()).thenReturn(50);

        ShopOffering expensive = mock(ShopOffering.class);
        when(expensive.getPrice()).thenReturn(150);

        assertTrue(shop.canAfford(affordable, player));
        assertFalse(shop.canAfford(expensive, player));
    }

    @Test
    void testPurchaseDelegation() {
        Player player = mock(Player.class);
        ShopOffering offering = mock(ShopOffering.class);

        when(offering.purchase(player)).thenReturn(true);
        assertTrue(shop.purchase(offering, player));
        verify(offering).purchase(player);

        when(offering.purchase(player)).thenReturn(false);
        assertFalse(shop.purchase(offering, player));
    }
}
