package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.MonsterFactory;
import com.aor.player.model.game.elements.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Dungeon monster movement collision detection
 */
class DungeonMonsterCollisionTest {
    private Dungeon dungeon;
    private Room room;

    @BeforeEach
    void setUp() {
        dungeon = new Dungeon(50, 50);
        room = new Room("test");

        // Create walls around the room perimeter
        List<Wall> walls = new ArrayList<>();
        for (int x = 0; x < 50; x++) {
            walls.add(new Wall(x, 0)); // Top wall
            walls.add(new Wall(x, 49)); // Bottom wall
        }
        for (int y = 1; y < 49; y++) {
            walls.add(new Wall(0, y)); // Left wall
            walls.add(new Wall(49, y)); // Right wall
        }
        room.setWalls(walls);

        dungeon.addRoom(room);
        dungeon.setCurrentRoomId("test");
    }

    @Test
    void testCanMonsterMoveToValidPosition() {
        Monster slime = MonsterFactory.createSlime(10, 10);
        room.addMonster(slime);

        Position newPos = new Position(15, 15);
        assertTrue(dungeon.canMonsterMoveTo(slime, newPos),
                "Monster should be able to move to empty space");
    }

    @Test
    void testCannotMoveOutOfBoundsLeft() {
        Monster slime = MonsterFactory.createSlime(5, 10);
        room.addMonster(slime);

        Position outOfBounds = new Position(-1, 10);
        assertFalse(dungeon.canMonsterMoveTo(slime, outOfBounds),
                "Monster should not move out of bounds (left)");
    }

    @Test
    void testCannotMoveOutOfBoundsTop() {
        Monster slime = MonsterFactory.createSlime(10, 5);
        room.addMonster(slime);

        Position outOfBounds = new Position(10, -1);
        assertFalse(dungeon.canMonsterMoveTo(slime, outOfBounds),
                "Monster should not move out of bounds (top)");
    }

    @Test
    void testCannotMoveOutOfBoundsRight() {
        Monster slime = MonsterFactory.createSlime(10, 10);
        room.addMonster(slime);

        // Position that would put sprite past right edge
        Position outOfBounds = new Position(50, 10);
        assertFalse(dungeon.canMonsterMoveTo(slime, outOfBounds),
                "Monster should not move out of bounds (right)");
    }

    @Test
    void testCannotMoveOutOfBoundsBottom() {
        Monster slime = MonsterFactory.createSlime(10, 10);
        room.addMonster(slime);

        // Position that would put sprite past bottom edge
        Position outOfBounds = new Position(10, 50);
        assertFalse(dungeon.canMonsterMoveTo(slime, outOfBounds),
                "Monster should not move out of bounds (bottom)");
    }

    @Test
    void testCannotMoveThroughWalls() {
        Monster slime = MonsterFactory.createSlime(10, 10);
        room.addMonster(slime);

        // Try to move to a wall position
        Position wallPos = new Position(0, 5);
        assertFalse(dungeon.canMonsterMoveTo(slime, wallPos),
                "Monster should not move through walls");
    }

    @Test
    void testCannotOverlapWithOtherMonster() {
        Monster slime1 = MonsterFactory.createSlime(10, 10);
        Monster slime2 = MonsterFactory.createSlime(20, 20);
        room.addMonster(slime1);
        room.addMonster(slime2);

        // Try to move slime1 to slime2's position
        Position slime2Pos = slime2.getPosition();
        assertFalse(dungeon.canMonsterMoveTo(slime1, slime2Pos),
                "Monster should not overlap with another monster");
    }

    @Test
    void testCanMoveAdjacentToOtherMonster() {
        Monster slime1 = MonsterFactory.createSlime(10, 10);
        Monster slime2 = MonsterFactory.createSlime(20, 20);
        room.addMonster(slime1);
        room.addMonster(slime2);

        // Move slime1 adjacent to slime2 (but not overlapping)
        int slime2Width = slime2.getSpriteWidth();
        Position adjacentPos = new Position(20 + slime2Width, 20);
        assertTrue(dungeon.canMonsterMoveTo(slime1, adjacentPos),
                "Monster should be able to move adjacent to another monster");
    }

    @Test
    void testMonsterCanStayInPlace() {
        Monster slime = MonsterFactory.createSlime(10, 10);
        room.addMonster(slime);

        Position currentPos = slime.getPosition();
        assertTrue(dungeon.canMonsterMoveTo(slime, currentPos),
                "Monster should be able to stay in current position");
    }

    @Test
    void testMultipleMonstersCollision() {
        Monster slime1 = MonsterFactory.createSlime(10, 10);
        Monster slime2 = MonsterFactory.createSlime(15, 15);
        Monster slime3 = MonsterFactory.createSlime(20, 20);
        room.addMonster(slime1);
        room.addMonster(slime2);
        room.addMonster(slime3);

        // slime1 tries to move to slime2's position
        assertFalse(dungeon.canMonsterMoveTo(slime1, slime2.getPosition()),
                "Monster should not overlap with second monster");

        // slime1 tries to move to slime3's position
        assertFalse(dungeon.canMonsterMoveTo(slime1, slime3.getPosition()),
                "Monster should not overlap with third monster");
    }

    @Test
    void testLargeMonsterCollision() {
        // Bosses have larger sprites
        Monster boss = MonsterFactory.createTrollKing(25, 25);
        Monster slime = MonsterFactory.createSlime(10, 10);
        room.addMonster(boss);
        room.addMonster(slime);

        // Try to move slime into boss's larger hitbox
        Position bossEdge = new Position(
                boss.getPosition().getX() + boss.getSpriteWidth() - 1,
                boss.getPosition().getY());
        assertFalse(dungeon.canMonsterMoveTo(slime, bossEdge),
                "Small monster should not overlap with large monster's sprite");
    }

    @Test
    void testEmptyRoomNoCollisions() {
        Monster slime = MonsterFactory.createSlime(25, 25);
        room.addMonster(slime);

        // Should be able to move to any position not against walls
        assertTrue(dungeon.canMonsterMoveTo(slime, new Position(10, 10)));
        assertTrue(dungeon.canMonsterMoveTo(slime, new Position(30, 30)));
        assertTrue(dungeon.canMonsterMoveTo(slime, new Position(20, 15)));
    }
}
