package com.aor.player.model.game.shop;

import com.aor.player.model.game.elements.Player;

/**
 * Represents a stat upgrade available in the shop.
 * Each purchase increases the stat and the price for the next purchase.
 */
public class ShopUpgrade {
    private final String name;
    private final String description;
    private final UpgradeType type;
    private final int statIncrease;
    private final int basePrice;
    private int purchaseCount;
    private static final double PRICE_MULTIPLIER = 1.15;

    public enum UpgradeType {
        HEALTH("Max Health"),
        ATTACK("Attack"),
        ARMOR("Armor");

        private final String displayName;

        UpgradeType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public ShopUpgrade(String name, String description, UpgradeType type, int statIncrease, int basePrice) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.statIncrease = statIncrease;
        this.basePrice = basePrice;
        this.purchaseCount = 0;
    }

    /**
     * Gets the current price based on purchase count
     * Price = basePrice * (1.15 ^ purchaseCount)
     */
    public int getCurrentPrice() {
        return (int) (basePrice * Math.pow(PRICE_MULTIPLIER, purchaseCount));
    }

    /**
     * Applies the upgrade to the player
     */
    public void applyUpgrade(Player player) {
        switch (type) {
            case HEALTH:
                player.increaseMaxHealth(statIncrease);
                player.heal(statIncrease); // Also heal by the increased amount
                break;
            case ATTACK:
                player.increaseAttack(statIncrease);
                break;
            case ARMOR:
                player.increaseBaseArmor(statIncrease);
                break;
        }
        purchaseCount++;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description + " (+" + statIncrease + " " + type.getDisplayName() + ")";
    }

    public UpgradeType getType() {
        return type;
    }

    public int getStatIncrease() {
        return statIncrease;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }
}
