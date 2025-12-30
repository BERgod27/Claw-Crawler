package com.aor.player.viewer.inventory;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.items.Armor;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.items.Sword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryViewerTest {

    private Dungeon mockDungeon;
    private Player mockPlayer;
    private GUI mockGUI;
    private InventoryViewer inventoryViewer;

    @BeforeEach
    void setUp() {
        mockDungeon = mock(Dungeon.class);
        mockPlayer = mock(Player.class);
        mockGUI = mock(GUI.class);

        when(mockDungeon.getPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getGold()).thenReturn(150);
        when(mockPlayer.getEquippedArmor()).thenReturn(null);
        when(mockPlayer.getEquippedSword()).thenReturn(null);
        when(mockPlayer.getInventory()).thenReturn(Collections.emptyList());

        inventoryViewer = new InventoryViewer(mockDungeon);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockDungeon, inventoryViewer.getModel());
    }

    @Test
    void testSetSelectedIndex() {
        inventoryViewer.setSelectedIndex(2);

        // Should not throw, selection updated internally
        inventoryViewer.drawElements(mockGUI);
        verify(mockGUI, atLeastOnce()).clear();
    }

    @Test
    void testDrawElementsCallsClear() {
        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).clear();
    }

    @Test
    void testDrawElementsCallsRefresh() throws java.io.IOException {
        inventoryViewer.drawElements(mockGUI);

        // Refresh is called inside try-catch
        verify(mockGUI, atLeastOnce()).refresh();
    }

    @Test
    void testDrawDisplaysInventoryHeader() {
        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(2), eq("=== INVENTORY ==="), eq("#FFD700"));
    }

    @Test
    void testDrawDisplaysGoldAmount() {
        when(mockPlayer.getGold()).thenReturn(250);

        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(3), eq("Gold: 250"), eq("#FFFF00"));
    }

    @Test
    void testDrawDisplaysEquippedSectionHeader() {
        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(5), eq("EQUIPPED:"), eq("#00FF00"));
    }

    @Test
    void testDrawDisplaysNoArmorWhenNotEquipped() {
        when(mockPlayer.getEquippedArmor()).thenReturn(null);

        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(6), eq("  Armor: None"), eq("#666666"));
    }

    @Test
    void testDrawDisplaysEquippedArmor() {
        Armor mockArmor = mock(Armor.class);
        when(mockArmor.getName()).thenReturn("Iron Armor");
        when(mockArmor.getStatBonus()).thenReturn(15);
        when(mockPlayer.getEquippedArmor()).thenReturn(mockArmor);

        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(6), eq("  Armor: Iron Armor (+15 armor)"), eq("#AAAAAA"));
    }

    @Test
    void testDrawDisplaysNoSwordWhenNotEquipped() {
        when(mockPlayer.getEquippedSword()).thenReturn(null);

        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(7), eq("  Sword: None"), eq("#666666"));
    }

    @Test
    void testDrawDisplaysEquippedSword() {
        Sword mockSword = mock(Sword.class);
        when(mockSword.getName()).thenReturn("Steel Sword");
        when(mockSword.getStatBonus()).thenReturn(10);
        when(mockPlayer.getEquippedSword()).thenReturn(mockSword);

        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(7), eq("  Sword: Steel Sword (+10 attack)"), eq("#AAAAAA"));
    }

    @Test
    void testDrawDisplaysItemsSectionHeader() {
        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(10), eq("ITEMS:"), eq("#00FF00"));
    }

    @Test
    void testDrawDisplaysEmptyInventoryMessage() {
        when(mockPlayer.getInventory()).thenReturn(Collections.emptyList());

        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(11), eq("  (Empty)"), eq("#666666"));
    }

    @Test
    void testDrawDisplaysSingleItem() {
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Health Potion");
        when(mockItem.getDescription()).thenReturn("Restores 20 HP");
        when(mockPlayer.getInventory()).thenReturn(Collections.singletonList(mockItem));

        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(11), eq("> Health Potion - Restores 20 HP"), eq("#00FF00"));
    }

    @Test
    void testDrawDisplaysMultipleItems() {
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        when(item1.getName()).thenReturn("Health Potion");
        when(item1.getDescription()).thenReturn("Restores 20 HP");
        when(item2.getName()).thenReturn("Mana Potion");
        when(item2.getDescription()).thenReturn("Restores 15 MP");
        when(mockPlayer.getInventory()).thenReturn(Arrays.asList(item1, item2));

        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(11), eq("> Health Potion - Restores 20 HP"), eq("#00FF00"));
        verify(mockGUI).drawText(eq(2), eq(12), eq("  Mana Potion - Restores 15 MP"), eq("#FFFFFF"));
    }

    @Test
    void testDrawHighlightsSelectedItem() {
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        when(item1.getName()).thenReturn("Item 1");
        when(item1.getDescription()).thenReturn("Desc 1");
        when(item2.getName()).thenReturn("Item 2");
        when(item2.getDescription()).thenReturn("Desc 2");
        when(mockPlayer.getInventory()).thenReturn(Arrays.asList(item1, item2));

        inventoryViewer.setSelectedIndex(1);
        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(11), eq("  Item 1 - Desc 1"), eq("#FFFFFF"));
        verify(mockGUI).drawText(eq(2), eq(12), eq("> Item 2 - Desc 2"), eq("#00FF00"));
    }

    @Test
    void testDrawDisplaysControls() {
        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), anyInt(), eq("UP/DOWN: Navigate | ENTER: Use/Equip | I/Q: Close"),
                eq("#888888"));
    }

    @Test
    void testDrawWithBothEquippedItems() {
        Armor mockArmor = mock(Armor.class);
        Sword mockSword = mock(Sword.class);
        when(mockArmor.getName()).thenReturn("Plate Armor");
        when(mockArmor.getStatBonus()).thenReturn(20);
        when(mockSword.getName()).thenReturn("Excalibur");
        when(mockSword.getStatBonus()).thenReturn(25);
        when(mockPlayer.getEquippedArmor()).thenReturn(mockArmor);
        when(mockPlayer.getEquippedSword()).thenReturn(mockSword);

        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(6), contains("Plate Armor"), eq("#AAAAAA"));
        verify(mockGUI).drawText(eq(2), eq(7), contains("Excalibur"), eq("#AAAAAA"));
    }

    @Test
    void testDrawMultipleItemsWithSelection() {
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Item item3 = mock(Item.class);
        when(item1.getName()).thenReturn("A");
        when(item1.getDescription()).thenReturn("DA");
        when(item2.getName()).thenReturn("B");
        when(item2.getDescription()).thenReturn("DB");
        when(item3.getName()).thenReturn("C");
        when(item3.getDescription()).thenReturn("DC");
        when(mockPlayer.getInventory()).thenReturn(Arrays.asList(item1, item2, item3));

        inventoryViewer.setSelectedIndex(2);
        inventoryViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(eq(2), eq(13), eq("> C - DC"), eq("#00FF00"));
    }
}
