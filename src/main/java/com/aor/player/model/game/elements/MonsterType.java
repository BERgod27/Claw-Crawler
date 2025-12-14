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
    SLIME("Slime", 20, 5, 2, "sprites/characters/Slime.png"),

    /**
     * First boss - Troll King
     * A massive troll with high health and defense
     */
    BOSS_TROLL_KING("Troll King", 50, 12, 5, "sprites/characters/Troll.png"),

    /**
     * Second boss - Vampire Lord
     * A cunning vampire with balanced stats and life-stealing abilities
     */
    BOSS_VAMPIRE_LORD("Vampire Lord", 80, 18, 8, "sprites/characters/Vampire.png"),

    /**
     * Final boss - Demon King
     * The ultimate evil with devastating power
     */
    BOSS_DEMON_KING("Demon King", 120, 25, 12, "sprites/characters/Devil.png");

    private final String displayName;
    private final int baseHealth;
    private final int baseAttack;
    private final int baseDefense;
    private final String spritePath;

    /**
     * Constructor for MonsterType enum
     * 
     * @param displayName The name displayed to the player
     * @param baseHealth  Starting and maximum health points
     * @param baseAttack  Attack power
     * @param baseDefense Defense rating
     * @param spritePath  Path to sprite resource file
     */
    MonsterType(String displayName, int baseHealth, int baseAttack, int baseDefense, String spritePath) {
        this.displayName = displayName;
        this.baseHealth = baseHealth;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.spritePath = spritePath;
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
}
