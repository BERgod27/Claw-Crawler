package com.aor.player.model.game.items;

import com.aor.player.model.game.elements.Player;

public class Armor implements Item, Equippable {
    private static final String DEFAULT_NAME = "Iron Armor";
    private static final int DEFAULT_PRICE = 50;
    private static final int DEFAULT_ARMOR_BONUS = 15;

    private final String name;
    private final int price;
    private final String description;
    private final int armorBonus;

    // Constructor with custom name and armor bonus
    public Armor(String name, int armorBonus) {
        this.name = name;
        this.armorBonus = armorBonus;
        this.price = DEFAULT_PRICE;
        this.description = "Increases armor by " + armorBonus;
    }

    // Default constructor for backward compatibility
    public Armor() {
        this(DEFAULT_NAME, DEFAULT_ARMOR_BONUS);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
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
        return armorBonus;
    }
}
