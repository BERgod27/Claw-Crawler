package com.aor.player.model.game.items;

import com.aor.player.model.game.elements.Player;

public class Sword implements Item, Equippable {
    private static final String DEFAULT_NAME = "Steel Sword";
    private static final int DEFAULT_PRICE = 75;
    private static final int DEFAULT_ATTACK_BONUS = 10;

    private final String name;
    private final int price;
    private final String description;
    private final int attackBonus;

    // Constructor with custom name and attack bonus
    public Sword(String name, int attackBonus) {
        this.name = name;
        this.attackBonus = attackBonus;
        this.price = DEFAULT_PRICE;
        this.description = "Increases attack by " + attackBonus;
    }

    // Default constructor for backward compatibility
    public Sword() {
        this(DEFAULT_NAME, DEFAULT_ATTACK_BONUS);
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
        player.equipSword(this);
    }

    @Override
    public void unequip(Player player) {
        player.unequipSword();
    }

    @Override
    public int getStatBonus() {
        return attackBonus;
    }
}
