package com.aor.player.model.game.shop;

import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.items.HealthPotion;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private final List<ShopOffering> offerings;

    public Shop() {
        this.offerings = new ArrayList<>();
        initializeOfferings();
    }

    private void initializeOfferings() {
        // Add health potion as a consumable item
        offerings.add(new ShopOffering(new HealthPotion()));

        // Add permanent stat upgrades
        offerings.add(new ShopOffering(new ShopUpgrade(
                "Health Boost",
                "Increase max health",
                ShopUpgrade.UpgradeType.HEALTH,
                50,
                50)));

        offerings.add(new ShopOffering(new ShopUpgrade(
                "Sharpen Blade",
                "Increase attack power",
                ShopUpgrade.UpgradeType.ATTACK,
                10,
                75)));

        offerings.add(new ShopOffering(new ShopUpgrade(
                "Reinforce Armor",
                "Increase armor rating",
                ShopUpgrade.UpgradeType.ARMOR,
                10,
                75)));
    }

    public List<ShopOffering> getAvailableOfferings() {
        return new ArrayList<>(offerings);
    }

    // Legacy method for backward compatibility with upgrades
    public List<ShopUpgrade> getAvailableUpgrades() {
        List<ShopUpgrade> upgrades = new ArrayList<>();
        for (ShopOffering offering : offerings) {
            if (offering.getType() == ShopOffering.OfferingType.UPGRADE) {
                upgrades.add(offering.getUpgrade());
            }
        }
        return upgrades;
    }

    public boolean canAfford(ShopOffering offering, Player player) {
        return player.getGold() >= offering.getPrice();
    }

    public boolean purchase(ShopOffering offering, Player player) {
        return offering.purchase(player);
    }
}
