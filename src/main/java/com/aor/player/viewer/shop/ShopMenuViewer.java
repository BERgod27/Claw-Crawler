package com.aor.player.viewer.shop;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.shop.Shop;
import com.aor.player.model.game.shop.ShopOffering;
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

        // Draw offerings
        int y = 5;
        int index = 0;
        for (ShopOffering offering : getModel().getAvailableOfferings()) {
            String prefix = (index == selectedIndex) ? "> " : "  ";
            String offeringText = prefix + offering.getName() + " - " + offering.getPrice() + "g";
            String color = (index == selectedIndex) ? "#00FF00" : "#FFFFFF";

            // Show if player can afford
            boolean canAfford = playerGold >= offering.getPrice();
            if (!canAfford && index == selectedIndex) {
                color = "#FF0000"; // Red if selected but can't afford
            }

            gui.drawText(2, y, offeringText, color);
            gui.drawText(4, y + 1, offering.getDescription(), "#AAAAAA");

            // Show purchase count for upgrades
            if (offering.getType() == ShopOffering.OfferingType.UPGRADE && offering.getPurchaseCount() > 0) {
                gui.drawText(4, y + 2, "Purchased: " + offering.getPurchaseCount() + " times", "#888888");
                y += 4;
            } else {
                y += 3;
            }

            index++;
        }

        // Draw controls
        gui.drawText(2, y + 2, "UP/DOWN: Navigate | ENTER: Buy | Q: Exit", "#888888");
        gui.drawText(2, y + 3, "Upgrade prices increase by 1.15x with each purchase!", "#00FFFF");

        try {
            gui.refresh();
        } catch (IOException e) {
            // Ignore refresh errors
        }
    }
}
