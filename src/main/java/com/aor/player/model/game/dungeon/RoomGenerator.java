package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Chest;
import com.aor.player.model.game.elements.Wall;

import java.util.ArrayList;
import java.util.List;

public class RoomGenerator {
    private final int dungeonWidth;
    private final int dungeonHeight;

    public RoomGenerator(int dungeonWidth, int dungeonHeight) {
        this.dungeonWidth = dungeonWidth;
        this.dungeonHeight = dungeonHeight;
    }

    public List<RoomArea> generateRooms(int numRooms, int minSize, int maxSize) {
        List<RoomArea> rooms = new ArrayList<>();
        int attempts = 0;
        int maxAttempts = numRooms * 10;

        while (rooms.size() < numRooms && attempts < maxAttempts) {
            attempts++;

            int width = (int) (Math.random() * (maxSize - minSize + 1)) + minSize;
            int height = (int) (Math.random() * (maxSize - minSize + 1)) + minSize;
            int x = (int) (Math.random() * (dungeonWidth - width - 1)) + 1;
            int y = (int) (Math.random() * (dungeonHeight - height - 1)) + 1;

            RoomArea newRoom = new RoomArea(x, y, width, height);

            boolean overlaps = false;
            for (RoomArea existingRoom : rooms) {
                if (newRoom.overlaps(existingRoom)) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {
                rooms.add(newRoom);
            }
        }

        return rooms;
    }

    public List<Wall> createWallsFromRooms(List<RoomArea> rooms) {
        List<Wall> walls = new ArrayList<>();

        // Dungeon perimeter walls
        for (int x = 0; x < dungeonWidth; x++) {
            walls.add(new Wall(x, 0));
            walls.add(new Wall(x, dungeonHeight - 1));
        }
        for (int y = 1; y < dungeonHeight - 1; y++) {
            walls.add(new Wall(0, y));
            walls.add(new Wall(dungeonWidth - 1, y));
        }

        // Fill dungeon with walls
        for (int x = 1; x < dungeonWidth - 1; x++) {
            for (int y = 1; y < dungeonHeight - 1; y++) {
                walls.add(new Wall(x, y));
            }
        }

        // Carve out rooms
        walls.removeIf(wall -> {
            for (RoomArea room : rooms) {
                if (room.contains(wall.getPosition())) return true;
            }
            return false;
        });

        return walls;
    }

    public void createCorridors(List<RoomArea> rooms, List<Wall> walls) {
        for (int i = 0; i < rooms.size() - 1; i++) {
            RoomArea room1 = rooms.get(i);
            RoomArea room2 = rooms.get(i + 1);

            int x1 = room1.getCenter().getX();
            int y1 = room1.getCenter().getY();
            int x2 = room2.getCenter().getX();
            int y2 = room2.getCenter().getY();

            // Horizontal corridor
            int startX = Math.min(x1, x2);
            int endX = Math.max(x1, x2);
            final int corridorY = y1;
            for (int x = startX; x <= endX; x++) {
                final int corridorX = x;
                walls.removeIf(wall -> wall.getPosition().getX() == corridorX && wall.getPosition().getY() == corridorY);
            }

            // Vertical corridor
            int startY = Math.min(y1, y2);
            int endY = Math.max(y1, y2);
            final int corridorX2 = x2;
            for (int y = startY; y <= endY; y++) {
                final int corridorY2 = y;
                walls.removeIf(wall -> wall.getPosition().getX() == corridorX2 && wall.getPosition().getY() == corridorY2);
            }
        }
    }
}
