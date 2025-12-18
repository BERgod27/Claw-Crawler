package com.aor.player.model.game.elements;

/**
 * Enum defining different monster types with their base statistics.
 * Each monster type has predefined health, attack, and defense values.
 * This follows the Factory Pattern to ensure consistent monster creation.
 */
public enum MonsterType {
    /**
     * Common slime - weakest enemy
     * A basic slime creature found throughout the dungeon
     */
    SLIME("Slime", 20, 5, 2, "sprites/characters/Slime.png", 500, 20),

    /**
     * First boss - Troll King
     * A massive troll with high health and defense
     */
    BOSS_TROLL_KING("Troll King", 200, 10, 30, "sprites/characters/Troll.png", 1000, 50),

    /**
     * Second boss - Vampire Lord
     * A cunning vampire with balanced stats and life-stealing abilities
     */
    BOSS_VAMPIRE_LORD("Vampire Lord", 275, 25, 10, "sprites/characters/Vampire.png", 800, 75),

    /**
     * Final boss - Demon King
     * The ultimate evil with devastating power
     */
    BOSS_DEMON_KING("Demon King", 400, 40, 40, "sprites/characters/Devil.png", 1200, 100);

    private final String displayName;
    private final int baseHealth;
    private final int baseAttack;
    private final int baseDefense;
    private final String spritePath;
    private final int moveDelay;
    private final int goldReward;

    /**
     * Constructor for MonsterType enum
     * 
     * @param displayName The name displayed to the player
     * @param baseHealth  Starting and maximum health points
     * @param baseAttack  Attack power
     * @param baseDefense Defense rating
     * @param spritePath  Path to sprite resource file
     * @param moveDelay   Milliseconds between movements
     * @param goldReward  Gold coins dropped when defeated
     */
    MonsterType(String displayName, int baseHealth, int baseAttack, int baseDefense, String spritePath, int moveDelay,
            int goldReward) {
        this.displayName = displayName;
        this.baseHealth = baseHealth;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.spritePath = spritePath;
        this.moveDelay = moveDelay;
        this.goldReward = goldReward;
    }

    /**
     * Gets the display name of this monster type
     * 
     * @return Display name shown to player
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the base health for this monster type
     * 
     * @return Base health points
     */
    public int getBaseHealth() {
        return baseHealth;
    }

    /**
     * Gets the base attack for this monster type
     * 
     * @return Base attack power
     */
    public int getBaseAttack() {
        return baseAttack;
    }

    /**
     * Gets the base defense for this monster type
     * 
     * @return Base defense rating
     */
    public int getBaseDefense() {
        return baseDefense;
    }

    /**
     * Gets the sprite resource path for this monster type
     * 
     * @return Path to sprite resource file
     */
    public String getSpritePath() {
        return spritePath;
    }

    /**
     * Checks if this monster type is a boss
     * Boss types have names starting with "BOSS_"
     * 
     * @return true if this is a boss monster type
     */
    public boolean isBoss() {
        return this.name().startsWith("BOSS_");
    }

    /**
     * Gets the movement delay for this monster type
     * 
     * @return Milliseconds between movements
     */
    public int getMoveDelay() {
        return moveDelay;
    }

    /**
     * Gets the gold reward for defeating this monster type
     * 
     * @return Gold coins dropped when defeated
     */
    public int getGoldReward() {
        return goldReward;
    }
}
