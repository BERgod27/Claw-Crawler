package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomTest {
    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room("room1");
    }

    @Test
    void testAddAndGetMonsters() {
        Monster monster = mock(Monster.class);
        room.addMonster(monster);

        List<Monster> monsters = room.getMonsters();
        assertEquals(1, monsters.size());
        assertEquals(monster, monsters.get(0));
    }

    @Test
    void testRemoveMonster() {
        Monster monster = mock(Monster.class);
        room.addMonster(monster);
        room.removeMonster(monster);

        assertTrue(room.getMonsters().isEmpty());
    }

    @Test
    void testAddAndGetWalls() {
        Wall wall = new Wall(1, 1);
        room.addWall(wall);

        List<Wall> walls = room.getWalls();
        assertEquals(1, walls.size());
        assertEquals(wall, walls.get(0));
    }

    @Test
    void testAddAndGetDoors() {
        Door door = new Door(2, 2, "room2", new Position(0, 0));
        room.addDoor(door);

        List<Door> doors = room.getDoors();
        assertEquals(1, doors.size());
        assertEquals(door, doors.get(0));
    }

    @Test
    void testIsEmptyChecksWalls() {
        Wall wall = new Wall(5, 5);
        room.addWall(wall);

        assertFalse(room.isEmpty(new Position(5, 5)));
        assertTrue(room.isEmpty(new Position(6, 6)));
    }

    @Test
    void testGetDoorAt() {
        Position pos = new Position(3, 3);
        Door door = new Door(3, 3, "room2", new Position(0, 0));
        room.addDoor(door);

        assertEquals(door, room.getDoorAt(pos));
        assertNull(room.getDoorAt(new Position(4, 4)));
    }

    @Test
    void testGetChestAt() {
        Position pos = new Position(4, 4);
        Chest chest = new Chest(pos);
        room.addChest(chest);

        assertEquals(chest, room.getChestAt(pos));
        assertNull(room.getChestAt(new Position(5, 5)));
    }

    @Test
    void testSetChestAtUpdatesExisting() {
        Position pos = new Position(1, 1);
        Chest chest1 = new Chest(pos);
        Chest chest2 = new Chest(pos);

        room.addChest(chest1);
        room.setChestAt(pos, chest2);

        assertEquals(chest2, room.getChestAt(pos));
        assertEquals(1, room.getChests().size());
    }

    @Test
    void testRespawnMultipleSlimes() {
        // Setup: 2 slimes
        Monster slime1 = mock(Monster.class);
        when(slime1.getType()).thenReturn(MonsterType.SLIME);
        when(slime1.getPosition()).thenReturn(new Position(10, 10));

        Monster slime2 = mock(Monster.class);
        when(slime2.getType()).thenReturn(MonsterType.SLIME);
        when(slime2.getPosition()).thenReturn(new Position(20, 20));

        room.addMonster(slime1);
        room.addMonster(slime2);

        // All die
        when(slime1.isDead()).thenReturn(true);
        when(slime2.isDead()).thenReturn(true);

        room.respawnSlimes();

        // Should have 2 new monsters
        assertEquals(2, room.getMonsters().size());
        // Verify they are not the mocks
        assertFalse(room.getMonsters().contains(slime1));
        assertFalse(room.getMonsters().contains(slime2));
    }

    @Test
    void testRespawnPartial() {
        // Setup: 2 slimes
        Monster slime1 = mock(Monster.class); // Will die
        when(slime1.getType()).thenReturn(MonsterType.SLIME);
        when(slime1.getPosition()).thenReturn(new Position(10, 10));

        Monster slime2 = mock(Monster.class); // Will live
        when(slime2.getType()).thenReturn(MonsterType.SLIME);
        when(slime2.getPosition()).thenReturn(new Position(50, 50));

        room.addMonster(slime1);
        room.addMonster(slime2);

        // Slime1 dies, Slime2 lives
        when(slime1.isDead()).thenReturn(true);
        when(slime2.isDead()).thenReturn(false);

        room.respawnSlimes();

        // Expect 2 total: Slime2 + 1 new
        List<Monster> monsters = room.getMonsters();
        assertEquals(2, monsters.size());
        assertTrue(monsters.contains(slime2));
        assertFalse(monsters.contains(slime1));

        // The new one should be at 10,10 because that's where slime1 was
        // Find the one that is not slime2
        Monster newSlime = monsters.stream().filter(m -> m != slime2).findFirst().get();
        assertEquals(10, newSlime.getPosition().getX());
        assertEquals(10, newSlime.getPosition().getY());
    }

    @Test
    void testRespawnBlockedByProximity() {
        // Initial setup: Slime A at 10,10. Slime B at 50,50.
        Monster slimeA = mock(Monster.class);
        when(slimeA.getType()).thenReturn(MonsterType.SLIME);
        when(slimeA.getPosition()).thenReturn(new Position(10, 10));

        Monster slimeB = mock(Monster.class);
        when(slimeB.getType()).thenReturn(MonsterType.SLIME);
        when(slimeB.getPosition()).thenReturn(new Position(50, 50));

        room.addMonster(slimeA);
        room.addMonster(slimeB);

        // Slime A dies.
        when(slimeA.isDead()).thenReturn(true);

        // Slime B moves close to A's spawn point (10,10). Say, to 12,12.
        when(slimeB.getPosition()).thenReturn(new Position(12, 12));

        room.respawnSlimes();

        List<Monster> monsters = room.getMonsters();
        assertEquals(2, monsters.size());
        assertTrue(monsters.contains(slimeB));

        Monster newSlime = monsters.stream().filter(m -> m != slimeB).findFirst().get();
        assertEquals(50, newSlime.getPosition().getX());
        assertEquals(50, newSlime.getPosition().getY());
    }

    @Test
    void testSlimeTrackingOnAdd() {
        // We verify that adding a slime registers its position for respawn
        Monster slime = mock(Monster.class);
        when(slime.getType()).thenReturn(MonsterType.SLIME);
        when(slime.getPosition()).thenReturn(new Position(10, 10));

        room.addMonster(slime);

        // Remove it (simulate death)
        when(slime.isDead()).thenReturn(true);
        room.respawnSlimes();

        List<Monster> monsters = room.getMonsters();
        assertEquals(1, monsters.size());
        assertNotEquals(slime, monsters.get(0));
    }
}
