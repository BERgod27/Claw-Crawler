package com.aor.player.viewer.inventory;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.items.Item;
import com.aor.player.viewer.Viewer;

import java.io.IOException;

public class InventoryViewer extends Viewer<Dungeon> {
    private int selectedIndex = 0;

    public InventoryViewer(Dungeon dungeon) {
        super(dungeon);
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
    }

    @Override
    public void drawElements(GUI gui) {
        gui.clear();

        // Draw inventory header
        gui.drawText(2, 2, "=== INVENTORY ===", "#FFD700");
        gui.drawText(2, 3, "Gold: " + getModel().getPlayer().getGold(), "#FFFF00");

        // Draw equipped items section
        int y = 5;
        gui.drawText(2, y++, "EQUIPPED:", "#00FF00");

        if (getModel().getPlayer().getEquippedArmor() != null) {
            String armorText = "  Armor: " + getModel().getPlayer().getEquippedArmor().getName() +
                    " (+" + getModel().getPlayer().getEquippedArmor().getStatBonus() + " armor)";
            gui.drawText(2, y++, armorText, "#AAAAAA");
        } else {
            gui.drawText(2, y++, "  Armor: None", "#666666");
        }

        if (getModel().getPlayer().getEquippedSword() != null) {
            String swordText = "  Sword: " + getModel().getPlayer().getEquippedSword().getName() +
                    " (+" + getModel().getPlayer().getEquippedSword().getStatBonus() + " attack)";
            gui.drawText(2, y++, swordText, "#AAAAAA");
        } else {
            gui.drawText(2, y++, "  Sword: None", "#666666");
        }

        y += 2;
        gui.drawText(2, y++, "ITEMS:", "#00FF00");

        // Draw inventory items
        if (getModel().getPlayer().getInventory().isEmpty()) {
            gui.drawText(2, y, "  (Empty)", "#666666");
        } else {
            int index = 0;
            for (Item item : getModel().getPlayer().getInventory()) {
                String prefix = (index == selectedIndex) ? "> " : "  ";
                String itemText = prefix + item.getName() + " - " + item.getDescription();
                String color = (index == selectedIndex) ? "#00FF00" : "#FFFFFF";

                gui.drawText(2, y++, itemText, color);
                index++;
            }
        }

        // Draw controls
        y += 2;
        gui.drawText(2, y, "UP/DOWN: Navigate | ENTER: Use/Equip | I/Q: Close", "#888888");

        try {
            gui.refresh();
        } catch (IOException e) {
            // Ignore refresh errors
        }
    }
}
