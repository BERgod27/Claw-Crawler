package com.aor.player.model.game.dungeon;

import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.elements.Wall;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonFactory {
    private static final int DUNGEON_WIDTH = 20;
    private static final int DUNGEON_HEIGHT = 20;
    private static final int MIN_ROOMS = 5;
    private static final int ROOM_VARIANCE = 4;
    private static final int MIN_ROOM_SIZE = 3;
    private static final int MAX_ROOM_SIZE = 6;
    private static final int DEFAULT_PLAYER_X = 10;
    private static final int DEFAULT_PLAYER_Y = 10;

    private final Random random;

    public DungeonFactory() {
        this.random = new Random();
    }

    public DungeonFactory(Random random) {
        this.random = random;
    }

    public Dungeon createDungeon() {
        Dungeon dungeon = new Dungeon(DUNGEON_WIDTH, DUNGEON_HEIGHT);

        // Generate rooms
        RoomGenerator generator = new RoomGenerator(DUNGEON_WIDTH, DUNGEON_HEIGHT);
        int numRooms = MIN_ROOMS + random.nextInt(ROOM_VARIANCE);
        List<Room> rooms = generator.generateRooms(numRooms, MIN_ROOM_SIZE, MAX_ROOM_SIZE);

        // Create walls and carve out rooms
        List<Wall> walls = generator.createWallsFromRooms(rooms);

        // Connect rooms with corridors
        generator.createCorridors(rooms, walls);

        dungeon.setWalls(walls);

        // Place player in the center of the first room
        if (!rooms.isEmpty()) {
            dungeon.setPlayer(new Player(rooms.get(0).getCenter().getX(), rooms.get(0).getCenter().getY()));
        } else {
            dungeon.setPlayer(new Player(DEFAULT_PLAYER_X, DEFAULT_PLAYER_Y));
        }

        // Initialize empty list for monsters
        dungeon.setMonsters(new ArrayList<>());

        return dungeon;
    }
}
