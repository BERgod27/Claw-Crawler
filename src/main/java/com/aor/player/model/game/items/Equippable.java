package com.aor.player.model.game.items;

import com.aor.player.model.game.elements.Player;

public interface Equippable {
    void equip(Player player);

    void unequip(Player player);

    int getStatBonus();
}
