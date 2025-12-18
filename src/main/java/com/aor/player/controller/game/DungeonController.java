package com.aor.player.controller.game;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Chest;
import com.aor.player.model.game.elements.Door;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.states.StateFactory;

import java.io.IOException;

public class DungeonController extends Controller<Dungeon> {
    private final StateFactory stateFactory;
    private Monster pendingMonster = null;
    private final java.util.Random random = new java.util.Random();
    private boolean quitConfirmation = false;

    public DungeonController(Dungeon dungeon, StateFactory stateFactory) {
        super(dungeon);
        this.stateFactory = stateFactory;
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        // Handle movement and actions
        switch (action) {
            case UP:
                movePlayerUp();
                quitConfirmation = false; // Cancel quit confirmation on movement
                break;
            case RIGHT:
                movePlayerRight();
                quitConfirmation = false; // Cancel quit confirmation on movement
                break;
            case DOWN:
                movePlayerDown();
                quitConfirmation = false; // Cancel quit confirmation on movement
                break;
            case LEFT:
                movePlayerLeft();
                quitConfirmation = false; // Cancel quit confirmation on movement
                break;
            case SELECT:
                checkShopKeeper(game);
                checkChestInteraction();
                quitConfirmation = false; // Cancel quit confirmation on action
                break;
            case INVENTORY:
                game.setState(stateFactory.createInventoryState(getModel()));
                return;
            case QUIT:
                if (quitConfirmation) {
                    game.setState(null); // Actually quit
                } else {
                    quitConfirmation = true; // Show confirmation
                }
                return;
            case NONE:
                // Do nothing, don't cancel quit confirmation
                break;
            default:
                // Any other action cancels quit confirmation
                quitConfirmation = false;
                break;
        }

        // Check if there's a pending monster combat from movement
        if (pendingMonster != null) {
            Monster monster = pendingMonster;
            pendingMonster = null;
            game.setState(stateFactory.createCombatState(getModel(), monster));
            return;
        }

        // Update monster positions
        updateMonsters(time);
    }

    public void movePlayerLeft() {
        movePlayer(getModel().getPlayer().getPosition().getLeft());
    }

    public void movePlayerRight() {
        movePlayer(getModel().getPlayer().getPosition().getRight());
    }

    public void movePlayerUp() {
        movePlayer(getModel().getPlayer().getPosition().getUp());
    }

    public void movePlayerDown() {
        movePlayer(getModel().getPlayer().getPosition().getDown());
    }

    private void movePlayer(Position newPosition) {
        // Don't do anything if position hasn't changed
        if (newPosition.equals(getModel().getPlayer().getPosition())) {
            return;
        }

        // Check for door collision BEFORE moving
        Door door = checkDoorAtPosition(newPosition);
        if (door != null) {
            // Check if this is the demon room door and if it's unlocked
            if (door.getTargetRoomId().equals("demon") && !getModel().isDemonRoomUnlocked()) {
                // Door is locked, do nothing (could add a message here later)
                return;
            }
            // Transition to the target room
            getModel().changeRoom(door.getTargetRoomId(), door.getSpawnPosition());
            return;
        }

        // Check for monster collision BEFORE moving
        Monster monster = checkMonsterAtPosition(newPosition);
        if (monster != null) {
            pendingMonster = monster;
            return;
        }

        // Check for shop collision - block movement if would overlap
        if (checkShopAtPosition(newPosition)) {
            return; // Don't move into shop
        }

        // Check for chest collision - block movement if would overlap
        if (checkChestAtPosition(newPosition)) {
            return; // Don't move into chest
        }

        // No collision, perform normal movement
        getModel().movePlayer(newPosition);
    }

    private Monster checkMonsterAtPosition(Position position) {
        // Check if player's sprite hitbox overlaps with any monster's sprite hitbox
        // Using AABB (Axis-Aligned Bounding Box) collision detection

        int playerX = position.getX();
        int playerY = position.getY();
        int playerWidth = getModel().getPlayer().getSpriteWidth();
        int playerHeight = getModel().getPlayer().getSpriteHeight();

        for (Monster monster : getModel().getMonsters()) {
            int monsterX = monster.getPosition().getX();
            int monsterY = monster.getPosition().getY();
            int monsterWidth = monster.getSpriteWidth();
            int monsterHeight = monster.getSpriteHeight();

            // AABB collision: check if rectangles overlap
            boolean overlapX = playerX < monsterX + monsterWidth && playerX + playerWidth > monsterX;
            boolean overlapY = playerY < monsterY + monsterHeight && playerY + playerHeight > monsterY;

            if (overlapX && overlapY) {
                return monster;
            }
        }
        return null;
    }

    private Door checkDoorAtPosition(Position position) {
        // Check all cells that the player sprite would occupy
        for (int dy = 0; dy < getModel().getPlayer().getSpriteHeight(); dy++) {
            for (int dx = 0; dx < getModel().getPlayer().getSpriteWidth(); dx++) {
                Position checkPos = new Position(position.getX() + dx, position.getY() + dy);
                Door door = getModel().getDoorAt(checkPos);
                if (door != null) {
                    return door;
                }
            }
        }
        return null;
    }

    private boolean checkShopAtPosition(Position position) {
        // Check if player would overlap with shop at the new position
        if (getModel().getCurrentRoom() == null || getModel().getCurrentRoom().getShopKeeper() == null) {
            return false;
        }

        var shopKeeper = getModel().getCurrentRoom().getShopKeeper();
        Position shopPos = shopKeeper.getPosition();

        int playerX = position.getX();
        int playerY = position.getY();
        int playerWidth = getModel().getPlayer().getSpriteWidth();
        int playerHeight = getModel().getPlayer().getSpriteHeight();

        int shopX = shopPos.getX();
        int shopY = shopPos.getY();
        int shopWidth = shopKeeper.getSpriteWidth();
        int shopHeight = shopKeeper.getSpriteHeight();

        // AABB collision
        boolean overlapX = playerX < shopX + shopWidth && playerX + playerWidth > shopX;
        boolean overlapY = playerY < shopY + shopHeight && playerY + playerHeight > shopY;

        return overlapX && overlapY;
    }

    private boolean checkChestAtPosition(Position position) {
        // Check if player would overlap with any chest at the new position
        if (getModel().getCurrentRoom() == null) {
            return false;
        }

        int playerX = position.getX();
        int playerY = position.getY();
        int playerWidth = getModel().getPlayer().getSpriteWidth();
        int playerHeight = getModel().getPlayer().getSpriteHeight();

        for (Chest chest : getModel().getCurrentRoom().getChests()) {
            Position chestPos = chest.getPosition();
            int chestX = chestPos.getX();
            int chestY = chestPos.getY();
            int chestWidth = 7; // Chest sprite size
            int chestHeight = 7;

            // AABB collision
            boolean overlapX = playerX < chestX + chestWidth && playerX + playerWidth > chestX;
            boolean overlapY = playerY < chestY + chestHeight && playerY + playerHeight > chestY;

            if (overlapX && overlapY) {
                return true; // Found collision with a chest
            }
        }
        return false;
    }

    private void checkShopKeeper(Game game) {
        if (isPlayerNearShopKeeper()) {
            game.setState(stateFactory.createShopState(getModel()));
        }
    }

    private void checkChestInteraction() {
        Chest chest = getModel().getChestNearPlayer();
        if (chest != null && !chest.isOpened()) {
            int gold = chest.open();
            getModel().getPlayer().addGold(gold);
        }
    }

    public boolean isPlayerNearShopKeeper() {
        return getModel().isPlayerNearShopKeeper();
    }

    /**
     * Updates all monsters in the current room
     * Moves monsters randomly at intervals defined by their type
     * 
     * @param time Current game time in milliseconds
     */
    private void updateMonsters(long time) {
        for (Monster monster : getModel().getMonsters()) {
            // Check if enough time has passed since last move
            if (time - monster.getLastMoveTime() < monster.getMoveDelay())
                continue;

            // Random direction: 0=up, 1=right, 2=down, 3=left
            int direction = random.nextInt(4);
            Position currentPos = monster.getPosition();
            Position newPos = currentPos;

            switch (direction) {
                case 0: // Up
                    newPos = currentPos.getUp();
                    break;
                case 1: // Right
                    newPos = currentPos.getRight();
                    break;
                case 2: // Down
                    newPos = currentPos.getDown();
                    break;
                case 3: // Left
                    newPos = currentPos.getLeft();
                    break;
            }

            // Validate and apply movement
            if (getModel().canMonsterMoveTo(monster, newPos)) {
                monster.setPosition(newPos);
            }

            // Update last move time regardless of whether move succeeded
            monster.setLastMoveTime(time);
        }
    }

    public boolean isQuitConfirmation() {
        return quitConfirmation;
    }

}
