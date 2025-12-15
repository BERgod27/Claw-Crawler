package com.aor.player.model.game.elements;

/**
 * Represents a monster enemy in the dungeon.
 * Monsters have health, attack, and defense stats based on their type.
 * They can take damage and be defeated in combat.
 */
public class Monster extends Element {
    private final MonsterType type;
    private final int maxHealth;
    private int health;
    private final int attack;
    private final int defense;
    private long lastMoveTime;
    private final int moveDelay;

    /**
     * Creates a monster with stats based on the specified type
     * 
     * @param x    X position in dungeon
     * @param y    Y position in dungeon
     * @param type Monster type (defines stats)
     */
    public Monster(int x, int y, MonsterType type) {
        super(x, y);
        this.type = type;
        this.maxHealth = type.getBaseHealth();
        this.health = this.maxHealth;
        this.attack = type.getBaseAttack();
        this.defense = type.getBaseDefense();
        this.moveDelay = type.getMoveDelay();
        this.lastMoveTime = 0;
    }

    /**
     * Gets the monster's type
     * 
     * @return Monster type
     */
    public MonsterType getType() {
        return type;
    }

    /**
     * Gets the monster's display name
     * 
     * @return Display name from type
     */
    public String getName() {
        return type.getDisplayName();
    }

    /**
     * Gets current health points
     * 
     * @return Current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gets maximum health points
     * 
     * @return Maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Gets attack power
     * 
     * @return Attack value
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Gets defense rating
     * 
     * @return Defense value
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Reduces monster health by the specified damage amount.
     * Health cannot go below 0.
     * 
     * @param damage Amount of damage to take
     */
    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
    }

    /**
     * Checks if the monster is dead (health <= 0)
     * 
     * @return true if monster is dead, false otherwise
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Gets the width of the monster's sprite
     * 
     * @return Sprite width in cells, defaults to 1 if sprite not available
     */
    public int getSpriteWidth() {
        com.aor.player.model.Sprite sprite = com.aor.player.viewer.game.MonsterViewer.getSpriteForType(type);
        return sprite != null ? sprite.getWidth() : 1;
    }

    /**
     * Gets the height of the monster's sprite
     * 
     * @return Sprite height in cells, defaults to 1 if sprite not available
     */
    public int getSpriteHeight() {
        com.aor.player.model.Sprite sprite = com.aor.player.viewer.game.MonsterViewer.getSpriteForType(type);
        return sprite != null ? sprite.getHeight() : 1;
    }

    /**
     * Gets the last time this monster moved
     * 
     * @return Timestamp of last movement
     */
    public long getLastMoveTime() {
        return lastMoveTime;
    }

    /**
     * Sets the last move time for this monster
     * 
     * @param lastMoveTime Timestamp to set
     */
    public void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    /**
     * Gets the delay between movements in milliseconds
     * 
     * @return Movement delay
     */
    public int getMoveDelay() {
        return moveDelay;
    }
}
