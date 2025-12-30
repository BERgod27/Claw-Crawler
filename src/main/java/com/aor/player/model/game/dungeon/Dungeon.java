package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Chest;
import com.aor.player.model.game.elements.Door;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.MonsterType;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.elements.ShopKeeper;
import com.aor.player.model.game.elements.Wall;
import com.aor.player.model.game.shop.Shop;

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
    private final Shop shop; // Persistent shop instance
    private final long gameStartTime; // Track when game started
    private int slimesKilled; // Track slimes killed

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.rooms = new HashMap<>();
        this.defeatedBosses = new HashSet<>();
        this.shop = new Shop(); // Initialize shop once
        this.gameStartTime = System.currentTimeMillis();
        this.slimesKilled = 0;
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

    public Shop getShop() {
        return shop;
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
            // Track slime kills
            if (monster.getType() == MonsterType.SLIME) {
                slimesKilled++;
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
            // Assume the passed position is a single point (1x1)
            if (com.aor.player.model.utils.CollisionUtils.checkCollision(
                    position, 1, 1,
                    monster.getPosition(), monster.getSpriteWidth(), monster.getSpriteHeight())) {
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
            // Assume the passed position is a single point (1x1)
            if (com.aor.player.model.utils.CollisionUtils.checkCollision(
                    position, 1, 1,
                    monster.getPosition(), monster.getSpriteWidth(), monster.getSpriteHeight())) {
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

        // Respawn slimes in the new room
        Room targetRoom = rooms.get(targetRoomId);
        if (targetRoom != null) {
            targetRoom.respawnSlimes();
        }
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

        ShopKeeper shopKeeper = getCurrentRoom().getShopKeeper();

        return com.aor.player.model.utils.CollisionUtils.checkAdjacency(
                player.getPosition(), player.getSpriteWidth(), player.getSpriteHeight(),
                shopKeeper.getPosition(), shopKeeper.getSpriteWidth(), shopKeeper.getSpriteHeight(),
                1);
    }

    /**
     * Gets the chest that the player is currently colliding with or adjacent to
     * 
     * @return The chest the player is near, or null if none
     */
    public Chest getChestNearPlayer() {
        if (getCurrentRoom() == null) {
            return null;
        }

        int playerWidth = player.getSpriteWidth();
        int playerHeight = player.getSpriteHeight();

        // Check all chests in current room
        for (Chest chest : getCurrentRoom().getChests()) {
            if (chest.isOpened()) {
                continue; // Skip already opened chests
            }

            int chestWidth = 7; // As assumed in original code
            int chestHeight = 7;

            if (com.aor.player.model.utils.CollisionUtils.checkAdjacency(
                    player.getPosition(), playerWidth, playerHeight,
                    chest.getPosition(), chestWidth, chestHeight,
                    1)) {
                return chest;
            }
        }
        return null;
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
     * Checks if all bosses have been defeated (victory condition)
     * 
     * @return true if all 3 bosses are defeated
     */
    public boolean areAllBossesDefeated() {
        return defeatedBosses.size() >= 3;
    }

    /**
     * Gets the time elapsed since game start in milliseconds
     */
    public long getTimePlayed() {
        return System.currentTimeMillis() - gameStartTime;
    }

    /**
     * Gets the number of slimes killed
     */
    public int getSlimesKilled() {
        return slimesKilled;
    }

    /**
     * Gets the total gold farmed (current gold + gold spent)
     * For simplicity, we'll just return current gold
     */
    public int getTotalGold() {
        return player != null ? player.getGold() : 0;
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

                    if (com.aor.player.model.utils.CollisionUtils.checkCollision(
                            checkPos, 1, 1,
                            other.getPosition(), other.getSpriteWidth(), other.getSpriteHeight())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
