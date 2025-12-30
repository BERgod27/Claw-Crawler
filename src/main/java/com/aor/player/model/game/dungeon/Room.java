package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
    private final String id;
    private List<Wall> walls;
    private List<Monster> monsters;
    private List<Door> doors;
    private ShopKeeper shopKeeper;
    private List<Chest> chests;
    private final List<Position> initialSlimePositions; // Track where slimes should spawn

    public Room(String id) {
        this.id = id;
        this.walls = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.doors = new ArrayList<>();
        this.chests = new ArrayList<>();
        this.initialSlimePositions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<Wall> getWalls() {
        return Collections.unmodifiableList(walls);
    }

    public void setWalls(List<Wall> walls) {
        this.walls = new ArrayList<>(walls);
    }

    public void addWall(Wall wall) {
        this.walls.add(wall);
    }

    public List<Monster> getMonsters() {
        return Collections.unmodifiableList(monsters);
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = new ArrayList<>(monsters);
    }

    public void addMonster(Monster monster) {
        this.monsters.add(monster);
        // Track slimes for respawning
        if (monster.getType() == MonsterType.SLIME) {
            initialSlimePositions.add(monster.getPosition());
        }
    }

    public void removeMonster(Monster monster) {
        this.monsters.remove(monster);
    }

    /**
     * Respawns all slimes in their initial positions
     * Called when player re-enters a room
     */
    public void respawnSlimes() {
        // Remove existing dead slimes
        monsters.removeIf(m -> m.getType() == MonsterType.SLIME && m.isDead());

        // Count how many slimes should exist vs how many are alive
        int expectedSlimes = initialSlimePositions.size();
        long aliveSlimes = monsters.stream()
                .filter(m -> m.getType() == MonsterType.SLIME)
                .count();

        // Only respawn the missing ones
        int slimesToSpawn = expectedSlimes - (int) aliveSlimes;

        if (slimesToSpawn > 0) {
            // Find which positions need slimes spawned
            int spawned = 0;
            for (Position slimePos : initialSlimePositions) {
                if (spawned >= slimesToSpawn)
                    break;

                // Check if any slime (alive) is already at or near this position
                boolean occupied = false;
                for (Monster monster : monsters) {
                    if (monster.getType() == MonsterType.SLIME) {
                        // Check if this spawn position is already occupied
                        int dx = Math.abs(monster.getPosition().getX() - slimePos.getX());
                        int dy = Math.abs(monster.getPosition().getY() - slimePos.getY());
                        if (dx < 5 && dy < 5) { // Within 5 units
                            occupied = true;
                            break;
                        }
                    }
                }

                if (!occupied) {
                    monsters.add(MonsterFactory.createSlime(
                            slimePos.getX(), slimePos.getY()));
                    spawned++;
                }
            }
        }
    }

    public List<Door> getDoors() {
        return Collections.unmodifiableList(doors);
    }

    public void setDoors(List<Door> doors) {
        this.doors = new ArrayList<>(doors);
    }

    public void addDoor(Door door) {
        this.doors.add(door);
    }

    public void addChest(Chest chest) {
        this.chests.add(chest);
    }

    public List<Chest> getChests() {
        return Collections.unmodifiableList(chests);
    }

    public boolean isEmpty(Position position) {
        for (Wall wall : walls) {
            if (wall.getPosition().equals(position))
                return false;
        }
        return true; // Doors and chests are passable by default
    }

    public Door getDoorAt(Position position) {
        for (Door door : doors) {
            if (door.getPosition().equals(position))
                return door;
        }
        return null;
    }

    public ShopKeeper getShopKeeper() {
        return shopKeeper;
    }

    public void setShopKeeper(ShopKeeper shopKeeper) {
        this.shopKeeper = shopKeeper;
    }

    public Chest getChestAt(Position position) {
        for (Chest chest : chests) {
            if (chest.getPosition().equals(position))
                return chest;
        }
        return null;
    }

    public void setChestAt(Position position, Chest chest) {
        for (int i = 0; i < chests.size(); i++) {
            if (chests.get(i).getPosition().equals(position)) {
                chests.set(i, chest);
                return;
            }
        }
        this.chests.add(chest); // Add if none exists
    }
}
