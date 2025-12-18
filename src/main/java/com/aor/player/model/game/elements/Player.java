package com.aor.player.model.game.elements;

import com.aor.player.model.game.items.Armor;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.items.Sword;

import java.util.ArrayList;
import java.util.List;

public class Player extends Element {
    private static final int DEFAULT_ARMOR = 10;
    private static final int DEFAULT_ATTACK = 10;
    private static final int DEFAULT_HEALTH = 100;
    private static final int INITIAL_MAX_HEALTH = 100;
    private static final int DEFAULT_GOLD = 10000;
    private static final int SPRITE_WIDTH = 7;
    private static final int SPRITE_HEIGHT = 7;

    private int baseArmor;
    private int health;
    private int maxHealth;
    private int gold;
    private int attack;

    private List<Item> inventory;
    private Armor equippedArmor;
    private Sword equippedSword;

    public Player(int x, int y) {
        super(x, y);
        this.baseArmor = DEFAULT_ARMOR;
        this.health = DEFAULT_HEALTH;
        this.maxHealth = INITIAL_MAX_HEALTH;
        this.gold = DEFAULT_GOLD;
        this.attack = DEFAULT_ATTACK;
        this.inventory = new ArrayList<>();
        this.equippedArmor = null;
        this.equippedSword = null;
    }

    public int getArmor() {
        int totalArmor = baseArmor;
        if (equippedArmor != null) {
            totalArmor += equippedArmor.getStatBonus();
        }
        return totalArmor;
    }

    public int getHealth() {
        return health;
    }

    public int getSpriteWidth() {
        return SPRITE_WIDTH;
    }

    public int getSpriteHeight() {
        return SPRITE_HEIGHT;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public boolean removeGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public void heal(int amount) {
        health = Math.min(health + amount, maxHealth);
    }

    public void equipArmor(Armor armor) {
        this.equippedArmor = armor;
    }

    public void unequipArmor() {
        this.equippedArmor = null;
    }

    public void equipSword(Sword sword) {
        this.equippedSword = sword;
    }

    public void unequipSword() {
        this.equippedSword = null;
    }

    public int getAttack() {
        int totalAttack = attack;
        if (equippedSword != null) {
            totalAttack += equippedSword.getStatBonus();
        }
        return totalAttack;
    }

    public Armor getEquippedArmor() {
        return equippedArmor;
    }

    public Sword getEquippedSword() {
        return equippedSword;
    }

    /**
     * Gets the maximum health value
     * 
     * @return Maximum health points
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Increases the player's maximum health
     * 
     * @param amount Amount to increase max health by
     */
    public void increaseMaxHealth(int amount) {
        this.maxHealth += amount;
    }

    /**
     * Increases the player's base attack
     * 
     * @param amount Amount to increase attack by
     */
    public void increaseAttack(int amount) {
        this.attack += amount;
    }

    /**
     * Increases the player's base armor
     * 
     * @param amount Amount to increase armor by
     */
    public void increaseBaseArmor(int amount) {
        this.baseArmor += amount;
    }

    /**
     * Reduces player health by damage amount, accounting for armor.
     * Actual damage is calculated as: max(1, damage - armor)
     * Health cannot go below 0.
     * 
     * @param damage Raw damage before armor reduction
     */
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - getArmor());
        this.health = Math.max(0, this.health - actualDamage);
    }

    /**
     * Directly reduces player health by the specified amount.
     * Use this when damage has already been calculated with armor reduction.
     * Health cannot go below 0.
     * 
     * @param damage Final damage amount (after armor has been applied)
     */
    public void reduceHealth(int damage) {
        this.health = Math.max(0, this.health - damage);
    }

    /**
     * Checks if the player is dead (health <= 0)
     * 
     * @return true if player is dead, false otherwise
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Removes an item from the inventory at the specified index
     * 
     * @param index Index of item to remove
     */
    public void removeItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            inventory.remove(index);
        }
    }
}
