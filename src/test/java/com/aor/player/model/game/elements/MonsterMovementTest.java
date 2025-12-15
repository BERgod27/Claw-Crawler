package com.aor.player.model.game.elements;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Monster movement functionality
 */
class MonsterMovementTest {

    @Test
    void testMonsterInitialMoveTime() {
        Monster slime = MonsterFactory.createSlime(10, 10);

        assertEquals(0, slime.getLastMoveTime(), "Initial move time should be 0");
    }

    @Test
    void testMonsterMoveDelay() {
        Monster slime = MonsterFactory.createSlime(10, 10);
        Monster trollKing = MonsterFactory.createTrollKing(20, 20);
        Monster vampireLord = MonsterFactory.createVampireLord(30, 30);
        Monster demonKing = MonsterFactory.createDemonKing(40, 40);

        assertEquals(500, slime.getMoveDelay(), "Slime should have 500ms move delay");
        assertEquals(1000, trollKing.getMoveDelay(), "Troll King should have 1000ms move delay");
        assertEquals(800, vampireLord.getMoveDelay(), "Vampire Lord should have 800ms move delay");
        assertEquals(1200, demonKing.getMoveDelay(), "Demon King should have 1200ms move delay");
    }

    @Test
    void testSetLastMoveTime() {
        Monster slime = MonsterFactory.createSlime(10, 10);
        long timestamp = System.currentTimeMillis();

        slime.setLastMoveTime(timestamp);

        assertEquals(timestamp, slime.getLastMoveTime(), "Last move time should be updated");
    }

    @Test
    void testMoveDelayFromType() {
        MonsterType slimeType = MonsterType.SLIME;
        MonsterType bossType = MonsterType.BOSS_TROLL_KING;

        assertEquals(500, slimeType.getMoveDelay(), "SLIME type should return 500ms");
        assertEquals(1000, bossType.getMoveDelay(), "BOSS_TROLL_KING type should return 1000ms");
    }

    @Test
    void testMonsterMoveDelayIsPositive() {
        for (MonsterType type : MonsterType.values()) {
            Monster monster = new Monster(0, 0, type);
            assertTrue(monster.getMoveDelay() > 0,
                    type.getDisplayName() + " should have positive move delay");
        }
    }

    @Test
    void testSlimeFasterThanBosses() {
        Monster slime = MonsterFactory.createSlime(10, 10);
        Monster trollKing = MonsterFactory.createTrollKing(20, 20);
        Monster vampireLord = MonsterFactory.createVampireLord(30, 30);
        Monster demonKing = MonsterFactory.createDemonKing(40, 40);

        assertTrue(slime.getMoveDelay() < trollKing.getMoveDelay(),
                "Slime should be faster than Troll King");
        assertTrue(slime.getMoveDelay() < vampireLord.getMoveDelay(),
                "Slime should be faster than Vampire Lord");
        assertTrue(slime.getMoveDelay() < demonKing.getMoveDelay(),
                "Slime should be faster than Demon King");
    }

    @Test
    void testDemonKingSlowest() {
        Monster trollKing = MonsterFactory.createTrollKing(20, 20);
        Monster vampireLord = MonsterFactory.createVampireLord(30, 30);
        Monster demonKing = MonsterFactory.createDemonKing(40, 40);

        assertTrue(demonKing.getMoveDelay() > trollKing.getMoveDelay(),
                "Demon King should be slower than Troll King");
        assertTrue(demonKing.getMoveDelay() > vampireLord.getMoveDelay(),
                "Demon King should be slower than Vampire Lord");
    }
}
