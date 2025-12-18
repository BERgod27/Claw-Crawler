package com.aor.player.model.game.shop;

import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.items.Usable;

/**
 * Represents a shop offering that can be either an item or an upgrade
 */
public class ShopOffering {
    private final Item item;
    private final ShopUpgrade upgrade;
    private final OfferingType type;

    public enum OfferingType {
        ITEM,
        UPGRADE
    }

    // Constructor for items
    public ShopOffering(Item item) {
        this.item = item;
        this.upgrade = null;
        this.type = OfferingType.ITEM;
    }

    // Constructor for upgrades
    public ShopOffering(ShopUpgrade upgrade) {
        this.item = null;
        this.upgrade = upgrade;
        this.type = OfferingType.UPGRADE;
    }

    public OfferingType getType() {
        return type;
    }

    public Item getItem() {
        return item;
    }

    public ShopUpgrade getUpgrade() {
        return upgrade;
    }

    public String getName() {
        return type == OfferingType.ITEM ? item.getName() : upgrade.getName();
    }

    public String getDescription() {
        return type == OfferingType.ITEM ? item.getDescription() : upgrade.getDescription();
    }

    public int getPrice() {
        return type == OfferingType.ITEM ? item.getPrice() : upgrade.getCurrentPrice();
    }

    public int getPurchaseCount() {
        return type == OfferingType.UPGRADE ? upgrade.getPurchaseCount() : 0;
    }

    public boolean purchase(Player player) {
        if (player.getGold() < getPrice()) {
            return false;
        }

        if (!player.removeGold(getPrice())) {
            return false;
        }

        if (type == OfferingType.ITEM) {
            player.addItem(item);
            // Auto-use if it's usable (like health potion)
            if (item instanceof Usable) {
                ((Usable) item).use(player);
            }
        } else {
            upgrade.applyUpgrade(player);
        }

        return true;
    }
}
