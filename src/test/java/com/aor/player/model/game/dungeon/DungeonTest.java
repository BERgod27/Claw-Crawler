package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DungeonTest {
    private Dungeon dungeon;
    private Room mockRoom;
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        dungeon = new Dungeon(100, 100);
        mockRoom = mock(Room.class);
        mockPlayer = mock(Player.class);

        when(mockRoom.getId()).thenReturn("testRoom");
        dungeon.addRoom(mockRoom);
        dungeon.setPlayer(mockPlayer);
        dungeon.setCurrentRoomId("testRoom");
    }

    @Test
    void testCurrentRoomAccess() {
        assertEquals(mockRoom, dungeon.getCurrentRoom());
        assertEquals("testRoom", dungeon.getCurrentRoomId());
    }

    @Test
    void testChangeRoom() {
        Room newRoom = mock(Room.class);
        when(newRoom.getId()).thenReturn("newRoom");
        dungeon.addRoom(newRoom);
        Position spawnPos = new Position(10, 10);
        dungeon.changeRoom("newRoom", spawnPos);
        assertEquals("newRoom", dungeon.getCurrentRoomId());
        verify(mockPlayer).setPosition(spawnPos);
        verify(newRoom).respawnSlimes();
    }

    @Test
    void testRemoveMonsterTracksKills() {
        Monster slime = mock(Monster.class);
        when(slime.getType()).thenReturn(MonsterType.SLIME);
        when(mockRoom.getMonsters()).thenReturn(List.of(slime));
        dungeon.removeMonster(slime);
        verify(mockRoom).removeMonster(slime);
        assertEquals(1, dungeon.getSlimesKilled());
    }

    @Test
    void testRemoveBossTracksDefeat() {
        Monster boss = mock(Monster.class);
        when(boss.getType()).thenReturn(MonsterType.BOSS_TROLL_KING);
        dungeon.removeMonster(boss);
        assertEquals(1, dungeon.getDefeatedBossCount());
        assertFalse(dungeon.areAllBossesDefeated());
    }

    @Test
    void testAllBossesDefeated() {
        Monster boss1 = mock(Monster.class);
        when(boss1.getType()).thenReturn(MonsterType.BOSS_TROLL_KING);
        dungeon.removeMonster(boss1);

        Monster boss2 = mock(Monster.class);
        when(boss2.getType()).thenReturn(MonsterType.BOSS_VAMPIRE_LORD);
        dungeon.removeMonster(boss2);
        assertFalse(dungeon.areAllBossesDefeated());

        Monster boss3 = mock(Monster.class);
        when(boss3.getType()).thenReturn(MonsterType.BOSS_DEMON_KING);
        dungeon.removeMonster(boss3);
        assertTrue(dungeon.areAllBossesDefeated());
    }

    @Test
    void testIsPlayerNearShopKeeper() {
        ShopKeeper shopKeeper = mock(ShopKeeper.class);
        when(mockRoom.getShopKeeper()).thenReturn(shopKeeper);
        when(mockPlayer.getPosition()).thenReturn(new Position(10, 10));
        when(mockPlayer.getSpriteWidth()).thenReturn(1);
        when(mockPlayer.getSpriteHeight()).thenReturn(1);
        when(shopKeeper.getPosition()).thenReturn(new Position(11, 10));
        when(shopKeeper.getSpriteWidth()).thenReturn(1);
        when(shopKeeper.getSpriteHeight()).thenReturn(1);
        assertTrue(dungeon.isPlayerNearShopKeeper());
    }

    @Test
    void testGetChestNearPlayer() {
        Chest chest = mock(Chest.class);
        when(mockRoom.getChests()).thenReturn(List.of(chest));
        when(chest.isOpened()).thenReturn(false);
        when(mockPlayer.getPosition()).thenReturn(new Position(10, 10));
        when(mockPlayer.getSpriteWidth()).thenReturn(1);
        when(mockPlayer.getSpriteHeight()).thenReturn(1);
        when(chest.getPosition()).thenReturn(new Position(10, 10));
        assertEquals(chest, dungeon.getChestNearPlayer());
    }

    @Test
    void testCanPlayerFit() {
        when(mockPlayer.getSpriteWidth()).thenReturn(2);
        when(mockPlayer.getSpriteHeight()).thenReturn(2);
        when(mockRoom.isEmpty(any(Position.class))).thenReturn(true);
        assertTrue(dungeon.canPlayerFit(new Position(0, 0)));
        assertFalse(dungeon.canPlayerFit(new Position(-1, 0)));
        assertFalse(dungeon.canPlayerFit(new Position(99, 99)));
    }

    @Test
    void testIsMonsterAtPosition() {
        Monster monster = mock(Monster.class);
        when(monster.getPosition()).thenReturn(new Position(10, 10));
        when(monster.getSpriteWidth()).thenReturn(1);
        when(monster.getSpriteHeight()).thenReturn(1);
        when(mockRoom.getMonsters()).thenReturn(List.of(monster));
        assertTrue(dungeon.isMonster(new Position(10, 10)));
        assertFalse(dungeon.isMonster(new Position(20, 20)));
    }

    @Test
    void testGetMonsterAtPosition() {
        Monster monster = mock(Monster.class);
        when(monster.getPosition()).thenReturn(new Position(5, 5));
        when(monster.getSpriteWidth()).thenReturn(1);
        when(monster.getSpriteHeight()).thenReturn(1);
        when(mockRoom.getMonsters()).thenReturn(List.of(monster));
        assertEquals(monster, dungeon.getMonsterAt(new Position(5, 5)));
        assertNull(dungeon.getMonsterAt(new Position(6, 6)));
    }

    @Test
    void testDelegateGetters() {
        List<Door> doors = List.of(mock(Door.class));
        List<Chest> chests = List.of(mock(Chest.class));
        List<Wall> walls = List.of(mock(Wall.class));
        when(mockRoom.getDoors()).thenReturn(doors);
        when(mockRoom.getChests()).thenReturn(chests);
        when(mockRoom.getWalls()).thenReturn(walls);
        assertEquals(doors, dungeon.getDoors());
        assertEquals(chests, dungeon.getChests());
        assertEquals(walls, dungeon.getWalls());
    }

    @Test
    void testDelegateGettersNullRoom() {
        dungeon.setCurrentRoomId("invalid");
        assertTrue(dungeon.getDoors().isEmpty());
        assertTrue(dungeon.getChests().isEmpty());
        assertTrue(dungeon.getWalls().isEmpty());
    }

    @Test
    void testIsDemonRoomUnlocked() {
        assertFalse(dungeon.isDemonRoomUnlocked());

        Monster boss1 = mock(Monster.class);
        when(boss1.getType()).thenReturn(MonsterType.BOSS_TROLL_KING);
        dungeon.removeMonster(boss1);
        assertFalse(dungeon.isDemonRoomUnlocked());

        Monster boss2 = mock(Monster.class);
        when(boss2.getType()).thenReturn(MonsterType.BOSS_VAMPIRE_LORD);
        dungeon.removeMonster(boss2);
        assertTrue(dungeon.isDemonRoomUnlocked());
    }

    @Test
    void testGetTotalGold() {
        when(mockPlayer.getGold()).thenReturn(100);
        assertEquals(100, dungeon.getTotalGold());
        dungeon.setPlayer(null);
        assertEquals(0, dungeon.getTotalGold());
    }

    @Test
    void testGetTimePlayed() {
        assertTrue(dungeon.getTimePlayed() >= 0);
    }

    @Test
    void testCanMonsterMoveTo() {
        Monster monster = mock(Monster.class);
        when(monster.getSpriteWidth()).thenReturn(2);
        when(monster.getSpriteHeight()).thenReturn(2);
        when(mockRoom.isEmpty(any(Position.class))).thenReturn(true);
        when(mockRoom.getMonsters()).thenReturn(List.of(monster));

        assertTrue(dungeon.canMonsterMoveTo(monster, new Position(10, 10)));
        assertFalse(dungeon.canMonsterMoveTo(monster, new Position(-1, 5)));
        assertFalse(dungeon.canMonsterMoveTo(monster, new Position(99, 5)));
        assertTrue(dungeon.canMonsterMoveTo(monster, new Position(98, 5)));
        assertFalse(dungeon.canMonsterMoveTo(monster, new Position(99, 5)));
        assertTrue(dungeon.canMonsterMoveTo(monster, new Position(5, 98)));
        assertFalse(dungeon.canMonsterMoveTo(monster, new Position(5, 99)));

        when(mockRoom.isEmpty(eq(new Position(20, 20)))).thenReturn(false);
        assertFalse(dungeon.canMonsterMoveTo(monster, new Position(20, 20)));

        Monster other = mock(Monster.class);
        when(other.getPosition()).thenReturn(new Position(30, 30));
        when(other.getSpriteWidth()).thenReturn(2);
        when(other.getSpriteHeight()).thenReturn(2);
        when(mockRoom.getMonsters()).thenReturn(List.of(monster, other));
        assertFalse(dungeon.canMonsterMoveTo(monster, new Position(30, 30)));
        assertFalse(dungeon.canMonsterMoveTo(monster, new Position(29, 29)));
    }

    @Test
    void testAddMonster() {
        Monster monster = mock(Monster.class);
        dungeon.addMonster(monster);
        verify(mockRoom).addMonster(monster);
        dungeon.setCurrentRoomId("invalid");
        dungeon.addMonster(monster);
    }

    @Test
    void testIsEmptyDelegation() {
        Position pos = new Position(10, 10);
        when(mockRoom.isEmpty(pos)).thenReturn(true);
        assertTrue(dungeon.isEmpty(pos));
        verify(mockRoom).isEmpty(pos);
        when(mockRoom.isEmpty(pos)).thenReturn(false);
        assertFalse(dungeon.isEmpty(pos));
    }

    @Test
    void testGetDoorAtDelegation() {
        Position pos = new Position(10, 10);
        Door mockDoor = mock(Door.class);
        when(mockRoom.getDoorAt(pos)).thenReturn(mockDoor);
        Door result = dungeon.getDoorAt(pos);
        assertEquals(mockDoor, result);
        verify(mockRoom).getDoorAt(pos);
    }

    @Test
    void testGetTimePlayedCalculation() {
        long timePlayed = dungeon.getTimePlayed();
        assertTrue(timePlayed >= 0);
    }

    @Test
    void testGetChestNearPlayerReturnsNull() {
        when(mockPlayer.getPosition()).thenReturn(new Position(50, 50));
        when(mockPlayer.getSpriteWidth()).thenReturn(2);
        when(mockPlayer.getSpriteHeight()).thenReturn(2);
        when(mockRoom.getChests()).thenReturn(List.of());
        assertNull(dungeon.getChestNearPlayer());
    }

    @Test
    void testIsPlayerNearShopKeeperWithNoShopKeeper() {
        when(mockRoom.getShopKeeper()).thenReturn(null);
        assertFalse(dungeon.isPlayerNearShopKeeper());
    }

    @Test
    void testCanPlayerFitWithWall() {
        when(mockPlayer.getSpriteWidth()).thenReturn(2);
        when(mockPlayer.getSpriteHeight()).thenReturn(2);
        Position pos = new Position(10, 10);
        when(mockRoom.isEmpty(any())).thenReturn(false);
        assertFalse(dungeon.canPlayerFit(pos));
    }

    @Test
    void testCanPlayerFitBoundaryChecks() {
        when(mockPlayer.getSpriteWidth()).thenReturn(2);
        when(mockPlayer.getSpriteHeight()).thenReturn(2);
        when(mockRoom.isEmpty(any())).thenReturn(true);
        assertFalse(dungeon.canPlayerFit(new Position(-1, 10)));
        assertFalse(dungeon.canPlayerFit(new Position(10, -1)));
        assertFalse(dungeon.canPlayerFit(new Position(99, 10)));
        assertFalse(dungeon.canPlayerFit(new Position(10, 99)));
    }
}
