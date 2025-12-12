package com.aor.player.model.game.items;

import com.aor.player.model.game.elements.Player;

public class HealthPotion implements Item, Usable {
    private static final String NAME = "Health Potion";
    private static final int PRICE = 10;
    private static final String DESCRIPTION = "Restores 20 HP";
    private static final int HEAL_AMOUNT = 20;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void use(Player player) {
        player.heal(HEAL_AMOUNT);
    }

    public int getHealAmount() {
        return HEAL_AMOUNT;
    }
}
