
package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomAreaTest {

    @Test
    void testRoomInitialization() {
        RoomArea room = new RoomArea(1, 2, 10, 5);
        assertEquals(1, room.getX());
        assertEquals(2, room.getY());
        assertEquals(10, room.getWidth());
        assertEquals(5, room.getHeight());
    }

    @Test
    void testGetCenter() {
        RoomArea room = new RoomArea(0, 0, 10, 10);
        Position center = room.getCenter();
        assertEquals(5, center.getX());
        assertEquals(5, center.getY());

        RoomArea room2 = new RoomArea(1, 1, 5, 5);
        Position center2 = room2.getCenter();
        assertEquals(3, center2.getX()); // 1 + 5/2 = 3
        assertEquals(3, center2.getY());
    }

    @Test
    void testOverlaps() {
        RoomArea room1 = new RoomArea(0, 0, 10, 10);

        // Completely inside
        RoomArea room2 = new RoomArea(2, 2, 5, 5);
        assertTrue(room1.overlaps(room2));
        assertTrue(room2.overlaps(room1));

        // Partial overlap
        RoomArea room3 = new RoomArea(8, 8, 5, 5);
        assertTrue(room1.overlaps(room3));
        assertTrue(room3.overlaps(room1));

        // No overlap - touching edges
        RoomArea room4 = new RoomArea(10, 0, 5, 5);
        assertFalse(room1.overlaps(room4));

        // No overlap - completely separate
        RoomArea room5 = new RoomArea(20, 20, 5, 5);
        assertFalse(room1.overlaps(room5));
    }

    @Test
    void testContains() {
        RoomArea room = new RoomArea(0, 0, 10, 10);

        // Inside
        assertTrue(room.contains(new Position(5, 5)));
        assertTrue(room.contains(new Position(0, 0))); // Top-left corner included

        // Edge cases
        assertTrue(room.contains(new Position(9, 9)));

        // Outside
        assertFalse(room.contains(new Position(10, 10))); // Bottom-right bound is exclusive
        assertFalse(room.contains(new Position(-1, 5)));
        assertFalse(room.contains(new Position(5, -1)));
    }
}
