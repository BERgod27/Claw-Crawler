package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Door;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.elements.Wall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dungeon {
    private final int width;
    private final int height;

    private Player player;
    private Map<String, Room> rooms;
    private String currentRoomId;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.rooms = new HashMap<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addRoom(Room room) {
        this.rooms.put(room.getId(), room);
    }

    public Room getCurrentRoom() {
        return rooms.get(currentRoomId);
    }

    public void setCurrentRoomId(String roomId) {
        this.currentRoomId = roomId;
    }

    public String getCurrentRoomId() {
        return currentRoomId;
    }

    public List<Monster> getMonsters() {
        return getCurrentRoom() != null ? getCurrentRoom().getMonsters() : List.of();
    }

    public List<Wall> getWalls() {
        return getCurrentRoom() != null ? getCurrentRoom().getWalls() : List.of();
    }

    public List<Door> getDoors() {
        return getCurrentRoom() != null ? getCurrentRoom().getDoors() : List.of();
    }

    public void addMonster(Monster monster) {
        if (getCurrentRoom() != null) {
            getCurrentRoom().addMonster(monster);
        }
    }

    public void removeMonster(Monster monster) {
        if (getCurrentRoom() != null) {
            getCurrentRoom().removeMonster(monster);
        }
    }

    public boolean isEmpty(Position position) {
        if (getCurrentRoom() == null)
            return true;
        return getCurrentRoom().isEmpty(position);
    }

    /**
     * Check if the player sprite (7x7) can fit at the given position without
     * colliding with walls
     */
    public boolean canPlayerFit(Position position) {
        // Check if position is within bounds
        if (position.getX() < 0 || position.getY() < 0)
            return false;
        if (position.getX() + player.getSpriteWidth() > width)
            return false;
        if (position.getY() + player.getSpriteHeight() > height)
            return false;

        // Check all cells that the sprite would occupy
        for (int dy = 0; dy < player.getSpriteHeight(); dy++) {
            for (int dx = 0; dx < player.getSpriteWidth(); dx++) {
                Position checkPos = new Position(position.getX() + dx, position.getY() + dy);
                if (!isEmpty(checkPos)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isMonster(Position position) {
        for (Monster monster : getMonsters())
            if (monster.getPosition().equals(position))
                return true;
        return false;
    }

    public Door getDoorAt(Position position) {
        if (getCurrentRoom() == null)
            return null;
        return getCurrentRoom().getDoorAt(position);
    }

    public void changeRoom(String targetRoomId, Position spawnPosition) {
        this.currentRoomId = targetRoomId;
        this.player.setPosition(spawnPosition);
    }

    public void movePlayer(Position newPosition) {
        if (canPlayerFit(newPosition)) {
            player.setPosition(newPosition);
        }
    }

    public boolean isPlayerNearShopKeeper() {
        if (getCurrentRoom() == null || getCurrentRoom().getShopKeeper() == null) {
            return false;
        }

        Position shopKeeperPos = getCurrentRoom().getShopKeeper().getPosition();
        Position playerPos = player.getPosition();

        // Check if any part of the player sprite (7x7) overlaps with shopkeeper
        for (int dy = 0; dy < player.getSpriteHeight(); dy++) {
            for (int dx = 0; dx < player.getSpriteWidth(); dx++) {
                Position checkPos = new Position(playerPos.getX() + dx, playerPos.getY() + dy);
                if (checkPos.equals(shopKeeperPos)) {
                    return true;
                }
            }
        }
        return false;
    }
}
