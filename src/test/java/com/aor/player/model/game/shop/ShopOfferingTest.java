package com.aor.player.model.game.shop;

import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.items.Usable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopOfferingTest {
    private Item mockItem;
    private ShopUpgrade mockUpgrade;
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        mockItem = mock(Item.class);
        mockUpgrade = mock(ShopUpgrade.class);
        mockPlayer = mock(Player.class);
    }

    @Test
    void testItemOfferingInitialization() {
        ShopOffering offering = new ShopOffering(mockItem);

        assertEquals(ShopOffering.OfferingType.ITEM, offering.getType());
        assertEquals(mockItem, offering.getItem());
        assertNull(offering.getUpgrade());

        when(mockItem.getName()).thenReturn("Potion");
        when(mockItem.getDescription()).thenReturn("Heals");
        when(mockItem.getPrice()).thenReturn(50);

        assertEquals("Potion", offering.getName());
        assertEquals("Heals", offering.getDescription());
        assertEquals(50, offering.getPrice());
        assertEquals(0, offering.getPurchaseCount());
    }

    @Test
    void testUpgradeOfferingInitialization() {
        ShopOffering offering = new ShopOffering(mockUpgrade);

        assertEquals(ShopOffering.OfferingType.UPGRADE, offering.getType());
        assertEquals(mockUpgrade, offering.getUpgrade());
        assertNull(offering.getItem());

        when(mockUpgrade.getName()).thenReturn("Boost");
        when(mockUpgrade.getDescription()).thenReturn("Boosts");
        when(mockUpgrade.getCurrentPrice()).thenReturn(100);
        when(mockUpgrade.getPurchaseCount()).thenReturn(2);

        assertEquals("Boost", offering.getName());
        assertEquals("Boosts", offering.getDescription());
        assertEquals(100, offering.getPrice());
        assertEquals(2, offering.getPurchaseCount());
    }

    @Test
    void testPurchaseInsufficientGold() {
        ShopOffering offering = new ShopOffering(mockItem);
        when(mockItem.getPrice()).thenReturn(50);
        when(mockPlayer.getGold()).thenReturn(40);

        assertFalse(offering.purchase(mockPlayer));
        verify(mockPlayer, never()).addItem(any());
    }

    @Test
    void testPurchaseItemSuccess() {
        ShopOffering offering = new ShopOffering(mockItem);
        when(mockItem.getPrice()).thenReturn(50);
        when(mockPlayer.getGold()).thenReturn(60);
        when(mockPlayer.removeGold(50)).thenReturn(true);

        assertTrue(offering.purchase(mockPlayer));
        verify(mockPlayer).removeGold(50);
        verify(mockPlayer).addItem(mockItem);
    }

    @Test
    void testPurchaseUsableItemAutoUse() {

        Item usableItem = mock(Item.class, withSettings().extraInterfaces(Usable.class));
        ShopOffering offering = new ShopOffering(usableItem);

        when(usableItem.getPrice()).thenReturn(50);
        when(mockPlayer.getGold()).thenReturn(100);
        when(mockPlayer.removeGold(50)).thenReturn(true);

        assertTrue(offering.purchase(mockPlayer));

        verify(mockPlayer).addItem(usableItem);
        verify((Usable) usableItem).use(mockPlayer);
    }

    @Test
    void testPurchaseUpgradeSuccess() {
        ShopOffering offering = new ShopOffering(mockUpgrade);
        when(mockUpgrade.getCurrentPrice()).thenReturn(100);
        when(mockPlayer.getGold()).thenReturn(150);
        when(mockPlayer.removeGold(100)).thenReturn(true);

        assertTrue(offering.purchase(mockPlayer));

        verify(mockPlayer).removeGold(100);
        verify(mockUpgrade).applyUpgrade(mockPlayer);
    }
}
