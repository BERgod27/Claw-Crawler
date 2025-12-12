package com.aor.player.model.game.elements;

import com.aor.player.model.game.items.Armor;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.items.Sword;

import java.util.ArrayList;
import java.util.List;

public class Player extends Element {
    private static final int DEFAULT_SPEED = 10;
    private static final int DEFAULT_ARMOR = 10;
    private static final int DEFAULT_HEALTH = 100;
    private static final int MAX_HEALTH = 100;
    private static final int DEFAULT_GOLD = 100;
    private static final int SPRITE_WIDTH = 7;
    private static final int SPRITE_HEIGHT = 7;

    private int speed;
    private int baseArmor;
    private int health;
    private int gold;
    private int attack;

    private List<Item> inventory;
    private Armor equippedArmor;
    private Sword equippedSword;

    public Player(int x, int y) {
        super(x, y);
        this.speed = DEFAULT_SPEED;
        this.baseArmor = DEFAULT_ARMOR;
        this.health = DEFAULT_HEALTH;
        this.gold = DEFAULT_GOLD;
        this.attack = 0;
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

    public int getSpeed() {
        return speed;
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
        health = Math.min(health + amount, MAX_HEALTH);
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
}
