package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Door;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.ShopKeeper;
import com.aor.player.model.game.elements.Wall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
    private final String id;
    private List<Wall> walls;
    private List<Monster> monsters;
    private List<Door> doors;
    private ShopKeeper shopKeeper;

    public Room(String id) {
        this.id = id;
        this.walls = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.doors = new ArrayList<>();
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
    }

    public void removeMonster(Monster monster) {
        this.monsters.remove(monster);
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

    public boolean isEmpty(Position position) {
        for (Wall wall : walls) {
            if (wall.getPosition().equals(position))
                return false;
        }
        // Doors are passable - don't block movement
        return true;
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
}
