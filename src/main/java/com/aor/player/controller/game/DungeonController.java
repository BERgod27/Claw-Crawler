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

    public DungeonController(Dungeon dungeon, StateFactory stateFactory) {
        super(dungeon);
        this.stateFactory = stateFactory;
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        // Handle movement
        switch (action) {
            case UP:
                movePlayerUp();
                break;
            case RIGHT:
                movePlayerRight();
                break;
            case DOWN:
                movePlayerDown();
                break;
            case LEFT:
                movePlayerLeft();
                break;
            case SELECT:
                checkShopKeeper(game);
                checkChestInteraction();
                break;
            case INVENTORY:
                game.setState(stateFactory.createInventoryState(getModel()));
                return;
            case QUIT:
                game.setState(null);
                return;
            default:
                break;
        }

        // Check if there's a pending monster combat from movement
        if (pendingMonster != null) {
            Monster monster = pendingMonster;
            pendingMonster = null;
            game.setState(stateFactory.createCombatState(getModel(), monster));
        }
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

        // No collision, perform normal movement
        getModel().movePlayer(newPosition);

        // Check for chest interaction automatically after moving
        checkChestInteraction();
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

    private void checkShopKeeper(Game game) {
        if (isPlayerNearShopKeeper()) {
            game.setState(stateFactory.createShopState(getModel()));
        }
    }

    private void checkChestInteraction() {
        Position playerPos = getModel().getPlayer().getPosition();
        var currentRoom = getModel().getCurrentRoom();

        // Check all chests in current room
        for (Chest chest : currentRoom.getChests()) {
            if (chest.getPosition().equals(playerPos) && !chest.isOpened()) {
                int gold = chest.open();
                getModel().getPlayer().addGold(gold);
            }
        }
    }

    public boolean isPlayerNearShopKeeper() {
        return getModel().isPlayerNearShopKeeper();
    }
}
