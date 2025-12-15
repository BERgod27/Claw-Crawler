package com.aor.player.model.game.shop;

import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.items.*;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private final List<Item> inventory;

    public Shop() {
        this.inventory = new ArrayList<>();
        initializeInventory();
    }

    private void initializeInventory() {
        // Add items to shop inventory
        inventory.add(new HealthPotion());
        inventory.add(new Armor("Iron Armor", 15));
        inventory.add(new Sword("Steel Sword", 10));
    }

    public List<Item> getAvailableItems() {
        return new ArrayList<>(inventory);
    }

    public boolean canAfford(Item item, Player player) {
        return player.getGold() >= item.getPrice();
    }

    public boolean purchase(Item item, Player player) {
        if (!canAfford(item, player)) {
            return false;
        }

        if (player.removeGold(item.getPrice())) {
            player.addItem(item);

            // Auto-equip if it's equippable
            if (item instanceof Equippable) {
                ((Equippable) item).equip(player);
            }

            return true;
        }

        return false;
    }
}
