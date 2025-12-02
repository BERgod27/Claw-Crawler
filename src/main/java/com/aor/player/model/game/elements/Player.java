package com.aor.player.model.game.elements;

public class Player extends Element {
    private static final int DEFAULT_SPEED = 10;
    private static final int DEFAULT_ARMOR = 10;
    private static final int DEFAULT_HEALTH = 10;

    private int speed;
    private int armor;
    private int health;

    public Player(int x, int y) {
        super(x, y);
        this.speed = DEFAULT_SPEED;
        this.armor = DEFAULT_ARMOR;
        this.health = DEFAULT_HEALTH;
    }

    public int getArmor() {
        return armor;
    }

    public int getHealth() {
        return health;
    }

    public int getSpeed() {
        return speed;
    }

}
