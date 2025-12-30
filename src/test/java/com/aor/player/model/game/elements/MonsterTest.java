package com.aor.player.model.game.elements;

import org.junit.jupiter.api.Test;
import com.aor.player.model.Position;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {

    // ========== Constructor Tests ==========

    @Test
    void testConstructorSetsPosition() {
        Monster monster = new Monster(10, 15, MonsterType.SLIME);

        assertEquals(10, monster.getPosition().getX());
        assertEquals(15, monster.getPosition().getY());
    }

    @Test
    void testConstructorInitializesStatsFromType() {
        Monster slime = new Monster(0, 0, MonsterType.SLIME);

        assertEquals(MonsterType.SLIME, slime.getType());
        assertEquals(20, slime.getMaxHealth());
        assertEquals(20, slime.getHealth());
        assertEquals(5, slime.getAttack());
        assertEquals(2, slime.getDefense());
        assertEquals(500, slime.getMoveDelay());
    }

    @Test
    void testConstructorForBossTrollKing() {
        Monster boss = new Monster(5, 5, MonsterType.BOSS_TROLL_KING);

        assertEquals(200, boss.getMaxHealth());
        assertEquals(200, boss.getHealth());
        assertEquals(10, boss.getAttack());
        assertEquals(30, boss.getDefense());
        assertEquals(1000, boss.getMoveDelay());
    }

    @Test
    void testConstructorForBossVampireLord() {
        Monster boss = new Monster(0, 0, MonsterType.BOSS_VAMPIRE_LORD);

        assertEquals(275, boss.getMaxHealth());
        assertEquals(275, boss.getHealth());
        assertEquals(25, boss.getAttack());
        assertEquals(10, boss.getDefense());
        assertEquals(800, boss.getMoveDelay());
    }

    @Test
    void testConstructorForBossDemonKing() {
        Monster boss = new Monster(0, 0, MonsterType.BOSS_DEMON_KING);

        assertEquals(400, boss.getMaxHealth());
        assertEquals(400, boss.getHealth());
        assertEquals(40, boss.getAttack());
        assertEquals(40, boss.getDefense());
        assertEquals(1200, boss.getMoveDelay());
    }

    @Test
    void testConstructorInitializesLastMoveTimeToZero() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        assertEquals(0, monster.getLastMoveTime());
    }

    // ========== Type and Name Tests ==========

    @Test
    void testGetTypeReturnsCorrectType() {
        Monster slime = new Monster(0, 0, MonsterType.SLIME);
        Monster boss = new Monster(0, 0, MonsterType.BOSS_DEMON_KING);

        assertEquals(MonsterType.SLIME, slime.getType());
        assertEquals(MonsterType.BOSS_DEMON_KING, boss.getType());
    }

    @Test
    void testGetNameReturnsDisplayName() {
        Monster slime = new Monster(0, 0, MonsterType.SLIME);
        Monster troll = new Monster(0, 0, MonsterType.BOSS_TROLL_KING);

        assertEquals("Slime", slime.getName());
        assertEquals("Troll King", troll.getName());
    }

    // ========== Health Tests ==========

    @Test
    void testGetHealthReturnsCurrentHealth() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        assertEquals(20, monster.getHealth());
    }

    @Test
    void testGetMaxHealthReturnsMaximumHealth() {
        Monster slime = new Monster(0, 0, MonsterType.SLIME);
        Monster boss = new Monster(0, 0, MonsterType.BOSS_DEMON_KING);

        assertEquals(20, slime.getMaxHealth());
        assertEquals(400, boss.getMaxHealth());
    }

    @Test
    void testTakeDamageReducesHealth() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        monster.takeDamage(10);

        assertEquals(10, monster.getHealth());
    }

    @Test
    void testTakeDamageMultipleTimes() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        monster.takeDamage(5);
        monster.takeDamage(5);
        monster.takeDamage(5);

        assertEquals(5, monster.getHealth());
    }

    @Test
    void testTakeDamageCannotGoBelowZero() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        monster.takeDamage(100);

        assertEquals(0, monster.getHealth());
    }

    @Test
    void testTakeDamageExactlyLethal() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        monster.takeDamage(20);

        assertEquals(0, monster.getHealth());
    }

    // ========== Death Tests ==========

    @Test
    void testIsDeadReturnsFalseWhenHealthAboveZero() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        assertFalse(monster.isDead());
    }

    @Test
    void testIsDeadReturnsTrueWhenHealthIsZero() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        monster.takeDamage(20);

        assertTrue(monster.isDead());
    }

    @Test
    void testIsDeadAfterOverkillDamage() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        monster.takeDamage(1000);

        assertTrue(monster.isDead());
    }

    // ========== Attack Tests ==========

    @Test
    void testGetAttackReturnsAttackValue() {
        Monster slime = new Monster(0, 0, MonsterType.SLIME);
        Monster vampire = new Monster(0, 0, MonsterType.BOSS_VAMPIRE_LORD);

        assertEquals(5, slime.getAttack());
        assertEquals(25, vampire.getAttack());
    }

    // ========== Defense Tests ==========

    @Test
    void testGetDefenseReturnsDefenseValue() {
        Monster slime = new Monster(0, 0, MonsterType.SLIME);
        Monster troll = new Monster(0, 0, MonsterType.BOSS_TROLL_KING);

        assertEquals(2, slime.getDefense());
        assertEquals(30, troll.getDefense());
    }

    // ========== Movement Tests ==========

    @Test
    void testGetMoveDelayReturnsCorrectDelay() {
        Monster slime = new Monster(0, 0, MonsterType.SLIME);
        Monster demon = new Monster(0, 0, MonsterType.BOSS_DEMON_KING);

        assertEquals(500, slime.getMoveDelay());
        assertEquals(1200, demon.getMoveDelay());
    }

    @Test
    void testSetLastMoveTimeUpdatesTime() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        monster.setLastMoveTime(1000);

        assertEquals(1000, monster.getLastMoveTime());
    }

    @Test
    void testSetLastMoveTimeMultipleTimes() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        monster.setLastMoveTime(100);
        monster.setLastMoveTime(500);
        monster.setLastMoveTime(1500);

        assertEquals(1500, monster.getLastMoveTime());
    }

    // ========== Sprite Dimension Tests ==========

    @Test
    void testGetSpriteWidthReturnsDefaultWhenNoSprite() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        // Should return 1 as default when sprite is not available
        int width = monster.getSpriteWidth();
        assertTrue(width >= 1);
    }

    @Test
    void testGetSpriteHeightReturnsDefaultWhenNoSprite() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        // Should return 1 as default when sprite is not available
        int height = monster.getSpriteHeight();
        assertTrue(height >= 1);
    }

    // ========== Element Inheritance Tests ==========

    @Test
    void testMonsterExtendsElement() {
        Monster monster = new Monster(5, 10, MonsterType.SLIME);

        assertTrue(monster instanceof Element);
        assertEquals(5, monster.getPosition().getX());
        assertEquals(10, monster.getPosition().getY());
    }

    @Test
    void testMovementMethodsFromElement() {
        Monster monster = new Monster(5, 10, MonsterType.SLIME);

        monster.setPosition(new Position(15, 20));

        assertEquals(15, monster.getPosition().getX());
        assertEquals(20, monster.getPosition().getY());
    }

    // ========== Combat Scenario Tests ==========

    @Test
    void testWeakMonsterDiesQuickly() {
        Monster slime = new Monster(0, 0, MonsterType.SLIME);

        assertFalse(slime.isDead());

        slime.takeDamage(10);
        assertFalse(slime.isDead());

        slime.takeDamage(10);
        assertTrue(slime.isDead());
    }

    @Test
    void testBossWithstandsMultipleHits() {
        Monster boss = new Monster(0, 0, MonsterType.BOSS_DEMON_KING);

        assertFalse(boss.isDead());

        boss.takeDamage(50);
        assertFalse(boss.isDead());
        assertEquals(350, boss.getHealth());

        boss.takeDamage(100);
        assertFalse(boss.isDead());
        assertEquals(250, boss.getHealth());

        boss.takeDamage(100);
        assertFalse(boss.isDead());
        assertEquals(150, boss.getHealth());

        boss.takeDamage(150);
        assertTrue(boss.isDead());
    }

    @Test
    void testDifferentMonsterTypesHaveDifferentStats() {
        Monster slime = new Monster(0, 0, MonsterType.SLIME);
        Monster troll = new Monster(0, 0, MonsterType.BOSS_TROLL_KING);
        Monster vampire = new Monster(0, 0, MonsterType.BOSS_VAMPIRE_LORD);
        Monster demon = new Monster(0, 0, MonsterType.BOSS_DEMON_KING);

        // Health comparison
        assertTrue(troll.getMaxHealth() > slime.getMaxHealth());
        assertTrue(vampire.getMaxHealth() > troll.getMaxHealth());
        assertTrue(demon.getMaxHealth() > vampire.getMaxHealth());

        // Attack comparison (not all are ordered, but demon should be strongest)
        assertTrue(demon.getAttack() > slime.getAttack());

        // Defense comparison
        assertTrue(demon.getDefense() > slime.getDefense());
    }

    @Test
    void testMonsterMovementDelayVariesByType() {
        Monster slime = new Monster(0, 0, MonsterType.SLIME);
        Monster vampire = new Monster(0, 0, MonsterType.BOSS_VAMPIRE_LORD);
        Monster demon = new Monster(0, 0, MonsterType.BOSS_DEMON_KING);

        // Slime should be fastest (lowest delay)
        assertTrue(slime.getMoveDelay() < demon.getMoveDelay());

        // Vampire should be faster than demon
        assertTrue(vampire.getMoveDelay() < demon.getMoveDelay());
    }

    @Test
    void testZeroDamageStillLeavesMonsterAlive() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        monster.takeDamage(0);

        assertEquals(20, monster.getHealth());
        assertFalse(monster.isDead());
    }

    @Test
    void testHealthNeverGoesNegative() {
        Monster monster = new Monster(0, 0, MonsterType.SLIME);

        monster.takeDamage(1000);

        assertEquals(0, monster.getHealth());
        assertTrue(monster.getHealth() >= 0);
    }
}
