package com.aor.player.model.game.elements;

/**
 * Factory class for creating Monster instances.
 */
public class MonsterFactory {

    /**
     * Creates a new Monster at the specified position with the given type.
     * 
     * @param x    X coordinate position in the dungeon
     * @param y    Y coordinate position in the dungeon
     * @param type The type of monster to create (defines stats)
     * @return A new Monster instance with stats from the specified type
     */
    public static Monster createMonster(int x, int y, MonsterType type) {
        return new Monster(x, y, type);
    }

    /**
     * Creates a common slime enemy at the specified position
     * 
     * @param x X coordinate position
     * @param y Y coordinate position
     * @return A new Slime monster
     */
    public static Monster createSlime(int x, int y) {
        return createMonster(x, y, MonsterType.SLIME);
    }

    /**
     * Creates the first boss (Troll King) at the specified position
     * 
     * @param x X coordinate position
     * @param y Y coordinate position
     * @return A new Troll King monster
     */
    public static Monster createTrollKing(int x, int y) {
        return createMonster(x, y, MonsterType.BOSS_TROLL_KING);
    }

    /**
     * Creates the second boss (Vampire Lord) at the specified position
     * 
     * @param x X coordinate position
     * @param y Y coordinate position
     * @return A new Vampire Lord monster
     */
    public static Monster createVampireLord(int x, int y) {
        return createMonster(x, y, MonsterType.BOSS_VAMPIRE_LORD);
    }

    /**
     * Creates the final boss (Demon King) at the specified position
     * 
     * @param x X coordinate position
     * @param y Y coordinate position
     * @return A new Demon King monster
     */
    public static Monster createDemonKing(int x, int y) {
        return createMonster(x, y, MonsterType.BOSS_DEMON_KING);
    }
}