package com.aor.player.viewer.shop;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.shop.Shop;
import com.aor.player.model.game.shop.ShopOffering;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopMenuViewerTest {

    private Shop mockShop;
    private GUI mockGUI;
    private ShopMenuViewer shopMenuViewer;

    @BeforeEach
    void setUp() {
        mockShop = mock(Shop.class);
        mockGUI = mock(GUI.class);

        when(mockShop.getAvailableOfferings()).thenReturn(Collections.emptyList());

        shopMenuViewer = new ShopMenuViewer(mockShop);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockShop, shopMenuViewer.getModel());
    }

    @Test
    void testSetSelectedIndex() {
        shopMenuViewer.setSelectedIndex(2);

        shopMenuViewer.drawElements(mockGUI);
        verify(mockGUI).clear();
    }

    @Test
    void testSetPlayerGold() {
        shopMenuViewer.setPlayerGold(500);

        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(3), eq("Gold: 500"), eq("#FFFF00"));
    }

    @Test
    void testDrawElementsCallsClear() {
        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).clear();
    }

    @Test
    void testDrawElementsCallsRefresh() throws java.io.IOException {
        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI, atLeastOnce()).refresh();
    }

    @Test
    void testDrawDisplaysShopHeader() {
        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(2), eq("=== SHOP ==="), eq("#FFD700"));
    }

    @Test
    void testDrawDisplaysDefaultGold() {
        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(3), eq("Gold: 100"), eq("#FFFF00"));
    }

    @Test
    void testDrawDisplaysSingleOffering() {
        ShopOffering mockOffering = mock(ShopOffering.class);
        when(mockOffering.getName()).thenReturn("Health Potion");
        when(mockOffering.getPrice()).thenReturn(50);
        when(mockOffering.getDescription()).thenReturn("Restores 20 HP");
        when(mockOffering.getType()).thenReturn(ShopOffering.OfferingType.ITEM);
        when(mockOffering.getPurchaseCount()).thenReturn(0);
        when(mockShop.getAvailableOfferings()).thenReturn(Collections.singletonList(mockOffering));

        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(5), eq("> Health Potion - 50g"), eq("#00FF00"));
        verify(mockGUI).drawText(eq(4), eq(6), eq("Restores 20 HP"), eq("#AAAAAA"));
    }

    @Test
    void testDrawDisplaysMultipleOfferings() {
        ShopOffering offering1 = mock(ShopOffering.class);
        ShopOffering offering2 = mock(ShopOffering.class);
        when(offering1.getName()).thenReturn("Potion");
        when(offering1.getPrice()).thenReturn(50);
        when(offering1.getDescription()).thenReturn("Heals");
        when(offering1.getType()).thenReturn(ShopOffering.OfferingType.ITEM);
        when(offering1.getPurchaseCount()).thenReturn(0);
        when(offering2.getName()).thenReturn("Sword");
        when(offering2.getPrice()).thenReturn(100);
        when(offering2.getDescription()).thenReturn("Weapon");
        when(offering2.getType()).thenReturn(ShopOffering.OfferingType.ITEM);
        when(offering2.getPurchaseCount()).thenReturn(0);
        when(mockShop.getAvailableOfferings()).thenReturn(Arrays.asList(offering1, offering2));

        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(5), eq("> Potion - 50g"), eq("#00FF00"));
        verify(mockGUI).drawText(eq(2), eq(8), eq("  Sword - 100g"), eq("#FFFFFF"));
    }

    @Test
    void testDrawHighlightsSelectedOffering() {
        ShopOffering offering1 = mock(ShopOffering.class);
        ShopOffering offering2 = mock(ShopOffering.class);
        when(offering1.getName()).thenReturn("Item 1");
        when(offering1.getPrice()).thenReturn(50);
        when(offering1.getDescription()).thenReturn("Desc 1");
        when(offering1.getType()).thenReturn(ShopOffering.OfferingType.ITEM);
        when(offering1.getPurchaseCount()).thenReturn(0);
        when(offering2.getName()).thenReturn("Item 2");
        when(offering2.getPrice()).thenReturn(75);
        when(offering2.getDescription()).thenReturn("Desc 2");
        when(offering2.getType()).thenReturn(ShopOffering.OfferingType.ITEM);
        when(offering2.getPurchaseCount()).thenReturn(0);
        when(mockShop.getAvailableOfferings()).thenReturn(Arrays.asList(offering1, offering2));

        shopMenuViewer.setSelectedIndex(1);
        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(5), eq("  Item 1 - 50g"), eq("#FFFFFF"));
        verify(mockGUI).drawText(eq(2), eq(8), eq("> Item 2 - 75g"), eq("#00FF00"));
    }

    @Test
    void testDrawShowsRedForUnaffordableSelectedItem() {
        ShopOffering mockOffering = mock(ShopOffering.class);
        when(mockOffering.getName()).thenReturn("Expensive Item");
        when(mockOffering.getPrice()).thenReturn(200);
        when(mockOffering.getDescription()).thenReturn("Too costly");
        when(mockOffering.getType()).thenReturn(ShopOffering.OfferingType.ITEM);
        when(mockOffering.getPurchaseCount()).thenReturn(0);
        when(mockShop.getAvailableOfferings()).thenReturn(Collections.singletonList(mockOffering));

        shopMenuViewer.setPlayerGold(50);
        shopMenuViewer.setSelectedIndex(0);
        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(5), eq("> Expensive Item - 200g"), eq("#FF0000"));
    }

    @Test
    void testDrawShowsGreenForAffordableSelectedItem() {
        ShopOffering mockOffering = mock(ShopOffering.class);
        when(mockOffering.getName()).thenReturn("Cheap Item");
        when(mockOffering.getPrice()).thenReturn(30);
        when(mockOffering.getDescription()).thenReturn("Affordable");
        when(mockOffering.getType()).thenReturn(ShopOffering.OfferingType.ITEM);
        when(mockOffering.getPurchaseCount()).thenReturn(0);
        when(mockShop.getAvailableOfferings()).thenReturn(Collections.singletonList(mockOffering));

        shopMenuViewer.setPlayerGold(100);
        shopMenuViewer.setSelectedIndex(0);
        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(5), eq("> Cheap Item - 30g"), eq("#00FF00"));
    }

    @Test
    void testDrawShowsPurchaseCountForUpgrades() {
        ShopOffering mockUpgrade = mock(ShopOffering.class);
        when(mockUpgrade.getName()).thenReturn("Attack Upgrade");
        when(mockUpgrade.getPrice()).thenReturn(100);
        when(mockUpgrade.getDescription()).thenReturn("+5 Attack");
        when(mockUpgrade.getType()).thenReturn(ShopOffering.OfferingType.UPGRADE);
        when(mockUpgrade.getPurchaseCount()).thenReturn(3);
        when(mockShop.getAvailableOfferings()).thenReturn(Collections.singletonList(mockUpgrade));

        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(4), eq(7), eq("Purchased: 3 times"), eq("#888888"));
    }

    @Test
    void testDrawDoesNotShowPurchaseCountForZeroUpgrades() {
        ShopOffering mockUpgrade = mock(ShopOffering.class);
        when(mockUpgrade.getName()).thenReturn("New Upgrade");
        when(mockUpgrade.getPrice()).thenReturn(100);
        when(mockUpgrade.getDescription()).thenReturn("+5 Defense");
        when(mockUpgrade.getType()).thenReturn(ShopOffering.OfferingType.UPGRADE);
        when(mockUpgrade.getPurchaseCount()).thenReturn(0);
        when(mockShop.getAvailableOfferings()).thenReturn(Collections.singletonList(mockUpgrade));

        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI, never()).drawText(anyInt(), anyInt(), contains("Purchased:"), anyString());
    }

    @Test
    void testDrawDisplaysControls() {
        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(anyInt(), anyInt(), eq("UP/DOWN: Navigate | ENTER: Buy | Q: Exit"), eq("#888888"));
    }

    @Test
    void testDrawDisplaysUpgradeWarning() {
        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(anyInt(), anyInt(), eq("Upgrade prices increase by 1.15x with each purchase!"),
                eq("#00FFFF"));
    }

    @Test
    void testDrawWithMixedOfferingTypes() {
        ShopOffering item = mock(ShopOffering.class);
        ShopOffering upgrade = mock(ShopOffering.class);
        when(item.getName()).thenReturn("Potion");
        when(item.getPrice()).thenReturn(50);
        when(item.getDescription()).thenReturn("Item");
        when(item.getType()).thenReturn(ShopOffering.OfferingType.ITEM);
        when(item.getPurchaseCount()).thenReturn(0);
        when(upgrade.getName()).thenReturn("Upgrade");
        when(upgrade.getPrice()).thenReturn(100);
        when(upgrade.getDescription()).thenReturn("Boost");
        when(upgrade.getType()).thenReturn(ShopOffering.OfferingType.UPGRADE);
        when(upgrade.getPurchaseCount()).thenReturn(2);
        when(mockShop.getAvailableOfferings()).thenReturn(Arrays.asList(item, upgrade));

        shopMenuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(5), contains("Potion"), anyString());
        verify(mockGUI).drawText(anyInt(), anyInt(), contains("Upgrade -"), anyString());
        verify(mockGUI).drawText(eq(4), anyInt(), eq("Purchased: 2 times"), eq("#888888"));
    }
}
