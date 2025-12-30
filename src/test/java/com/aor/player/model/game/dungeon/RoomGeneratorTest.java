
package com.aor.player.model.game.dungeon;

import com.aor.player.model.game.elements.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomGeneratorTest {
    private RoomGenerator generator;
    private final int WIDTH = 50;
    private final int HEIGHT = 50;

    @BeforeEach
    void setUp() {
        generator = new RoomGenerator(WIDTH, HEIGHT);
    }

    @Test
    void testGenerateRooms() {
        int numRooms = 5;
        int minSize = 5;
        int maxSize = 10;

        List<RoomArea> rooms = generator.generateRooms(numRooms, minSize, maxSize);

        // Should generate requested number of rooms (unless max attempts reached, which
        // is unlikely for these params)
        assertEquals(numRooms, rooms.size());

        for (RoomArea room : rooms) {
            // Check size constraints
            assertTrue(room.getWidth() >= minSize && room.getWidth() <= maxSize);
            assertTrue(room.getHeight() >= minSize && room.getHeight() <= maxSize);

            // Check bounds constraints (x > 0, y > 0 based on implementation x = rand(...)
            // + 1)
            assertTrue(room.getX() >= 1);
            assertTrue(room.getY() >= 1);
            assertTrue(room.getX() + room.getWidth() < WIDTH);
            assertTrue(room.getY() + room.getHeight() < HEIGHT);
        }

        // Check no overlaps
        for (int i = 0; i < rooms.size(); i++) {
            for (int j = i + 1; j < rooms.size(); j++) {
                assertFalse(rooms.get(i).overlaps(rooms.get(j)), "Rooms should not overlap");
            }
        }
    }

    @Test
    void testCreateWallsFromRooms() {
        // Manually create a simple room layout
        List<RoomArea> rooms = new ArrayList<>();
        rooms.add(new RoomArea(2, 2, 4, 4)); // Room from (2,2) to (5,5)

        List<Wall> walls = generator.createWallsFromRooms(rooms);

        assertFalse(walls.isEmpty());

        // Check that no walls are inside the room
        for (Wall wall : walls) {
            assertFalse(rooms.get(0).contains(wall.getPosition()),
                    "Wall at " + wall.getPosition().getX() + "," + wall.getPosition().getY()
                            + " should not be inside room");
        }

        // Check perimeter walls exist (e.g., 0,0)
        boolean hasPerimeterWall = false;
        for (Wall wall : walls) {
            if (wall.getPosition().getX() == 0 || wall.getPosition().getY() == 0 ||
                    wall.getPosition().getX() == WIDTH - 1 || wall.getPosition().getY() == HEIGHT - 1) {
                hasPerimeterWall = true;
                break;
            }
        }
        assertTrue(hasPerimeterWall, "Should generate perimeter walls");
    }

    @Test
    void testCreateCorridors() {
        // Two rooms that need connecting
        List<RoomArea> rooms = new ArrayList<>();
        RoomArea room1 = new RoomArea(2, 2, 3, 3); // Center ~ (3, 3)
        RoomArea room2 = new RoomArea(10, 2, 3, 3); // Center ~ (11, 3)
        // Same Y level, should be horizontal corridor

        rooms.add(room1);
        rooms.add(room2);

        // Get initial walls (full filled except rooms)
        List<Wall> walls = generator.createWallsFromRooms(rooms);
        int initialWallCount = walls.size();

        generator.createCorridors(rooms, walls);

        assertTrue(walls.size() < initialWallCount, "Corridors should remove walls");

        // Check connectivity roughly - between (5,3) and (10,3) walls should be removed
        // Actually implementation does horizontal then vertical
        // Center1 (3,3), Center2 (11,3)
        // Horizontal: min(3,11) to max(3,11) -> 3 to 11 at y=3

        // Verify a spot in the expected corridor doesn't have a wall
        for (Wall wall : walls) {
            if (wall.getPosition().getX() == 7 && wall.getPosition().getY() == 3) {
                fail("Corridor should have cleared wall at (7,3)");
            }
        }
    }
}
