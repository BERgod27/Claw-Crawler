package com.aor.player.model.game.dungeon;

import com.aor.player.model.game.elements.Chest;
import com.aor.player.model.game.elements.Door;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DungeonFactoryTest {
    private DungeonFactory dungeonFactory;

    @BeforeEach
    void setUp() {
        dungeonFactory = new DungeonFactory();
    }

    @Test
    void testCreateDungeon() {
        Dungeon dungeon = dungeonFactory.createDungeon();

        assertNotNull(dungeon);
        assertEquals(77, dungeon.getWidth());
        assertEquals(35, dungeon.getHeight());
        assertNotNull(dungeon.getPlayer());

        // Check rooms exist by trying to set them as current
        dungeon.setCurrentRoomId("main");
        assertNotNull(dungeon.getCurrentRoom());

        dungeon.setCurrentRoomId("east");
        assertNotNull(dungeon.getCurrentRoom());

        dungeon.setCurrentRoomId("west");
        assertNotNull(dungeon.getCurrentRoom());

        dungeon.setCurrentRoomId("demon");
        assertNotNull(dungeon.getCurrentRoom());

        // Check current room is main (factory default)
        dungeon = dungeonFactory.createDungeon(); // Re-create or reset if needed, but factory sets it initially
        assertEquals("main", dungeon.getCurrentRoomId());
    }

    @Test
    void testMainRoomContent() {
        Dungeon dungeon = dungeonFactory.createDungeon();
        dungeon.setCurrentRoomId("main");
        Room mainRoom = dungeon.getCurrentRoom();

        assertNotNull(mainRoom);
        assertFalse(mainRoom.getMonsters().isEmpty());
        assertFalse(mainRoom.getChests().isEmpty());
        assertFalse(mainRoom.getDoors().isEmpty());
        assertFalse(mainRoom.getWalls().isEmpty());
        assertNotNull(mainRoom.getShopKeeper());
    }

    @Test
    void testRoomConnectivity() {
        Dungeon dungeon = dungeonFactory.createDungeon();
        dungeon.setCurrentRoomId("main");
        Room mainRoom = dungeon.getCurrentRoom();

        // Verify doors in main room lead to expected rooms
        boolean leadsToEast = false;
        boolean leadsToWest = false;
        boolean leadsToDemon = false;

        for (Door door : mainRoom.getDoors()) {
            if ("east".equals(door.getTargetRoomId()))
                leadsToEast = true;
            if ("west".equals(door.getTargetRoomId()))
                leadsToWest = true;
            if ("demon".equals(door.getTargetRoomId()))
                leadsToDemon = true;
        }

        assertTrue(leadsToEast, "Main room should have door to east room");
        assertTrue(leadsToWest, "Main room should have door to west room");
        assertTrue(leadsToDemon, "Main room should have door to demon room");
    }
}
