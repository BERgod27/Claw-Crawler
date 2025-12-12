package com.aor.player.model.game.items;

import com.aor.player.model.game.elements.Player;

public class Armor implements Item, Equippable {
    private static final String NAME = "Iron Armor";
    private static final int PRICE = 50;
    private static final String DESCRIPTION = "Increases armor by 15";
    private static final int ARMOR_BONUS = 15;

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
    public void equip(Player player) {
        player.equipArmor(this);
    }

    @Override
    public void unequip(Player player) {
        player.unequipArmor();
    }

    @Override
    public int getStatBonus() {
        return ARMOR_BONUS;
    }
}
