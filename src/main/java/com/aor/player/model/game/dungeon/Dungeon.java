package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Chest;
import com.aor.player.model.game.elements.Door;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.MonsterType;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.elements.Wall;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dungeon {
    private final int width;
    private final int height;

    private Player player;
    private Map<String, Room> rooms;
    private String currentRoomId;
    private Set<MonsterType> defeatedBosses;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.rooms = new HashMap<>();
        this.defeatedBosses = new HashSet<>();
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

    public List<Chest> getChests() {
        return getCurrentRoom() != null ? getCurrentRoom().getChests() : List.of();
    }

    public void addMonster(Monster monster) {
        if (getCurrentRoom() != null) {
            getCurrentRoom().addMonster(monster);
        }
    }

    public void removeMonster(Monster monster) {
        if (getCurrentRoom() != null) {
            getCurrentRoom().removeMonster(monster);
            // Track boss defeats for unlocking demon room
            if (monster.getType().isBoss()) {
                defeatedBosses.add(monster.getType());
            }
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

    /**
     * Checks if there's a monster at the given position using hitbox collision
     * 
     * @param position Position to check
     * @return true if a monster's hitbox overlaps this position
     */
    public boolean isMonster(Position position) {
        for (Monster monster : getMonsters()) {
            int monsterX = monster.getPosition().getX();
            int monsterY = monster.getPosition().getY();
            int monsterWidth = monster.getSpriteWidth();
            int monsterHeight = monster.getSpriteHeight();

            // Check if position is within monster's bounding box
            if (position.getX() >= monsterX && position.getX() < monsterX + monsterWidth &&
                    position.getY() >= monsterY && position.getY() < monsterY + monsterHeight) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the monster at the specified position using hitbox collision
     * 
     * @param position Position to check
     * @return Monster whose hitbox contains this position, or null if none exists
     */
    public Monster getMonsterAt(Position position) {
        for (Monster monster : getMonsters()) {
            int monsterX = monster.getPosition().getX();
            int monsterY = monster.getPosition().getY();
            int monsterWidth = monster.getSpriteWidth();
            int monsterHeight = monster.getSpriteHeight();

            // Check if position is within monster's bounding box
            if (position.getX() >= monsterX && position.getX() < monsterX + monsterWidth &&
                    position.getY() >= monsterY && position.getY() < monsterY + monsterHeight) {
                return monster;
            }
        }
        return null;
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

    /**
     * Gets the number of bosses that have been defeated
     * 
     * @return Count of defeated bosses
     */
    public int getDefeatedBossCount() {
        return defeatedBosses.size();
    }

    /**
     * Checks if demon room should be unlocked (2+ bosses defeated)
     * 
     * @return true if at least 2 bosses have been defeated
     */
    public boolean isDemonRoomUnlocked() {
        return defeatedBosses.size() >= 2;
    }

    /**
     * Checks if a monster can move to the specified position
     * Validates against bounds, walls, and other monsters
     * 
     * @param monster     The monster attempting to move
     * @param newPosition The target position
     * @return true if the move is valid
     */
    public boolean canMonsterMoveTo(Monster monster, Position newPosition) {
        // Check bounds
        if (newPosition.getX() < 0 || newPosition.getY() < 0)
            return false;
        if (newPosition.getX() + monster.getSpriteWidth() > width)
            return false;
        if (newPosition.getY() + monster.getSpriteHeight() > height)
            return false;

        // Check all cells the monster sprite would occupy
        for (int dy = 0; dy < monster.getSpriteHeight(); dy++) {
            for (int dx = 0; dx < monster.getSpriteWidth(); dx++) {
                Position checkPos = new Position(newPosition.getX() + dx, newPosition.getY() + dy);

                // Check walls
                if (!isEmpty(checkPos))
                    return false;

                // Check collision with other monsters
                for (Monster other : getMonsters()) {
                    if (other == monster)
                        continue; // Skip self

                    int otherX = other.getPosition().getX();
                    int otherY = other.getPosition().getY();
                    int otherWidth = other.getSpriteWidth();
                    int otherHeight = other.getSpriteHeight();

                    // Check if checkPos overlaps with other monster
                    if (checkPos.getX() >= otherX && checkPos.getX() < otherX + otherWidth &&
                            checkPos.getY() >= otherY && checkPos.getY() < otherY + otherHeight) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
