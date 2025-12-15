package com.aor.player.model.game.items;

import com.aor.player.model.game.elements.Player;

public class HealthPotion implements Item, Usable {
    private static final String DEFAULT_NAME = "Health Potion";
    private static final int DEFAULT_PRICE = 10;
    private static final int DEFAULT_HEAL_AMOUNT = 20;

    private final String name;
    private final int price;
    private final String description;
    private final int healAmount;

    // Constructor with custom name and heal amount
    public HealthPotion(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
        this.price = DEFAULT_PRICE;
        this.description = "Restores " + healAmount + " HP";
    }

    // Default constructor for backward compatibility
    public HealthPotion() {
        this(DEFAULT_NAME, DEFAULT_HEAL_AMOUNT);
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
    public void use(Player player) {
        player.heal(healAmount);
    }

    public int getHealAmount() {
        return healAmount;
    }
}
