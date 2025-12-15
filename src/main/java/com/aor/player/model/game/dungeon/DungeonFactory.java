package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Door;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.elements.ShopKeeper;
import com.aor.player.model.game.elements.Wall;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonFactory {
    private static final int DUNGEON_WIDTH = 77;
    private static final int DUNGEON_HEIGHT = 35;

    // Door dimensions - should match or accommodate player sprite size
    private static final int DOOR_WIDTH = 1; // Horizontal span
    private static final int DOOR_HEIGHT = 10; // Vertical span (matches player sprite height)

    public DungeonFactory() {
    }

    public DungeonFactory(Random random) {
        // Constructor kept for compatibility with tests
    }

    public Dungeon createDungeon() {
        Dungeon dungeon = new Dungeon(DUNGEON_WIDTH, DUNGEON_HEIGHT);

        // Create three rooms
        Room mainRoom = createRoom("main");
        Room eastRoom = createRoom("east");
        Room westRoom = createRoom("west");
        Room demonRoom = createRoom("demon");

        // Add shopkeeper to main room (top-right corner)
        mainRoom.setShopKeeper(new ShopKeeper(DUNGEON_WIDTH - 10, 5));

        // Add monsters to rooms
        addMonstersToMainRoom(mainRoom);
        addMonstersToEastRoom(eastRoom);
        addMonstersToWestRoom(westRoom);
        addMonstersToDemonRoom(demonRoom);

        // Add chests to rooms
        addChestsToMainRoom(mainRoom);
        addChestsToEastRoom(eastRoom);
        addChestsToWestRoom(westRoom);
        addChestsToDemonRoom(demonRoom);

        // Calculate door position (centered vertically for side doors, horizontally for
        // top door)
        int doorCenterY = DUNGEON_HEIGHT / 2;
        int doorStartY = doorCenterY - (DOOR_HEIGHT / 2);
        int doorCenterX = DUNGEON_WIDTH / 2;
        int doorStartX = doorCenterX - (DOOR_HEIGHT / 2);

        // Add doorways to connect rooms
        // Main room: right door to east, left door to west, top door to demon
        createDoorway(mainRoom, DUNGEON_WIDTH - 1, doorStartY, "east",
                new Position(2, doorStartY));
        createDoorway(mainRoom, 0, doorStartY, "west",
                new Position(DUNGEON_WIDTH - 9, doorStartY));
        createHorizontalDoorway(mainRoom, doorStartX, 0, "demon",
                new Position(doorStartX, DUNGEON_HEIGHT - 11));

        // East room: left door back to main
        createDoorway(eastRoom, 0, doorStartY, "main",
                new Position(DUNGEON_WIDTH - 9, doorStartY));

        // West room: right door back to main
        createDoorway(westRoom, DUNGEON_WIDTH - 1, doorStartY, "main",
                new Position(2, doorStartY));

        // Demon room: bottom door back to main
        createHorizontalDoorway(demonRoom, doorStartX, DUNGEON_HEIGHT - 1, "main",
                new Position(doorStartX, 11));

        // Add rooms to dungeon
        dungeon.addRoom(mainRoom);
        dungeon.addRoom(eastRoom);
        dungeon.addRoom(westRoom);
        dungeon.addRoom(demonRoom);

        // Set starting room
        dungeon.setCurrentRoomId("main");

        // Place player in the center of the main room
        int centerX = DUNGEON_WIDTH / 2;
        int centerY = DUNGEON_HEIGHT / 2;
        dungeon.setPlayer(new Player(centerX, centerY));

        return dungeon;
    }

    private void addMonstersToMainRoom(Room room) {
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createSlime(20, 10));
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createSlime(30, 15));
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createSlime(50, 20));
    }

    private void addMonstersToEastRoom(Room room) {
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createTrollKing(DUNGEON_WIDTH / 2,
                DUNGEON_HEIGHT / 2));
        // Add slimes around the boss
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createSlime(15, 10));
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createSlime(DUNGEON_WIDTH - 20, 15));
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createSlime(20, DUNGEON_HEIGHT - 10));
    }

    private void addMonstersToWestRoom(Room room) {
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createVampireLord(DUNGEON_WIDTH / 2,
                DUNGEON_HEIGHT / 2));
        // Add slimes around the boss
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createSlime(25, 12));
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createSlime(DUNGEON_WIDTH - 25, 18));
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createSlime(30, DUNGEON_HEIGHT - 12));
    }

    private void addChestsToMainRoom(Room room) {
        room.addChest(new com.aor.player.model.game.elements.Chest(new Position(10, 10)));
        room.addChest(new com.aor.player.model.game.elements.Chest(new Position(DUNGEON_WIDTH - 15, 25)));
    }

    private void addChestsToEastRoom(Room room) {
        room.addChest(new com.aor.player.model.game.elements.Chest(new Position(15, 15)));
        room.addChest(new com.aor.player.model.game.elements.Chest(new Position(DUNGEON_WIDTH - 20, 20)));
    }

    private void addChestsToWestRoom(Room room) {
        room.addChest(new com.aor.player.model.game.elements.Chest(new Position(20, 10)));
    }

    private void addMonstersToDemonRoom(Room room) {
        // Place Demon King at the top of the room (far from entrance at bottom)
        room.addMonster(com.aor.player.model.game.elements.MonsterFactory.createDemonKing(DUNGEON_WIDTH / 2, 8));
    }

    private void addChestsToDemonRoom(Room room) {
        room.addChest(new com.aor.player.model.game.elements.Chest(new Position(DUNGEON_WIDTH / 2 - 10, 10)));
        room.addChest(new com.aor.player.model.game.elements.Chest(new Position(DUNGEON_WIDTH / 2 + 10, 10)));
    }

    /**
     * Creates a doorway (multiple door elements) at the specified position
     * 
     * @param room          The room to add the doorway to
     * @param x             X position of the doorway
     * @param startY        Starting Y position of the doorway
     * @param targetRoomId  ID of the room this doorway leads to
     * @param spawnPosition Where the player spawns in the target room
     */
    private void createDoorway(Room room, int x, int startY, String targetRoomId, Position spawnPosition) {
        for (int i = 0; i < DOOR_HEIGHT; i++) {
            room.addDoor(new Door(x, startY + i, targetRoomId, spawnPosition));
        }
    }

    /**
     * Creates a horizontal doorway (for top/bottom walls)
     */
    private void createHorizontalDoorway(Room room, int startX, int y, String targetRoomId, Position spawnPosition) {
        for (int i = 0; i < DOOR_HEIGHT; i++) {
            room.addDoor(new Door(startX + i, y, targetRoomId, spawnPosition));
        }
    }

    private Room createRoom(String id) {
        Room room = new Room(id);
        List<Wall> walls = new ArrayList<>();

        // Create boundary walls for the room
        // Top and bottom walls
        for (int x = 0; x < DUNGEON_WIDTH; x++) {
            walls.add(new Wall(x, 0)); // Top wall
            walls.add(new Wall(x, DUNGEON_HEIGHT - 1)); // Bottom wall
        }

        // Left and right walls
        for (int y = 1; y < DUNGEON_HEIGHT - 1; y++) {
            walls.add(new Wall(0, y)); // Left wall
            walls.add(new Wall(DUNGEON_WIDTH - 1, y)); // Right wall
        }

        room.setWalls(walls);
        return room;
    }
}
