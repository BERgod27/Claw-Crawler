package com.aor.player.viewer.shop;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.shop.Shop;
import com.aor.player.viewer.Viewer;

import java.io.IOException;

public class ShopMenuViewer extends Viewer<Shop> {
    private int selectedIndex = 0;
    private int playerGold = 100;

    public ShopMenuViewer(Shop shop) {
        super(shop);
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
    }

    public void setPlayerGold(int gold) {
        this.playerGold = gold;
    }

    @Override
    public void drawElements(GUI gui) {
        gui.clear();

        // Draw shop header
        gui.drawText(2, 2, "=== SHOP ===", "#FFD700");
        gui.drawText(2, 3, "Gold: " + playerGold, "#FFFF00");

        // Draw items
        int y = 5;
        int index = 0;
        for (Item item : getModel().getAvailableItems()) {
            String prefix = (index == selectedIndex) ? "> " : "  ";
            String itemText = prefix + item.getName() + " - " + item.getPrice() + "g";
            String color = (index == selectedIndex) ? "#00FF00" : "#FFFFFF";

            gui.drawText(2, y, itemText, color);
            gui.drawText(4, y + 1, item.getDescription(), "#AAAAAA");

            y += 3;
            index++;
        }

        // Draw controls
        gui.drawText(2, y + 2, "UP/DOWN: Navigate | ENTER: Buy | Q: Exit", "#888888");

        try {
            gui.refresh();
        } catch (IOException e) { // Changed Exception to IOException as gui.refresh() typically throws
                                  // IOException
            // Ignore refresh errors
        }
    }
}
