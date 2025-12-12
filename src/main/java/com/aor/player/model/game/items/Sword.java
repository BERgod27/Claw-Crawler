package com.aor.player.model.game.items;

import com.aor.player.model.game.elements.Player;

public class Sword implements Item, Equippable {
    private static final String NAME = "Steel Sword";
    private static final int PRICE = 75;
    private static final String DESCRIPTION = "Increases attack by 10";
    private static final int ATTACK_BONUS = 10;

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
        player.equipSword(this);
    }

    @Override
    public void unequip(Player player) {
        player.unequipSword();
    }

    @Override
    public int getStatBonus() {
        return ATTACK_BONUS;
    }
}
