package com.aor.player.controller.game;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Door;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.states.StateFactory;

import java.io.IOException;

public class DungeonController extends Controller<Dungeon> {
    private final StateFactory stateFactory;

    public DungeonController(Dungeon dungeon, StateFactory stateFactory) {
        super(dungeon);
        this.stateFactory = stateFactory;
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        // Handle movement
        if (action == GUI.ACTION.UP)
            movePlayerUp();
        if (action == GUI.ACTION.RIGHT)
            movePlayerRight();
        if (action == GUI.ACTION.DOWN)
            movePlayerDown();
        if (action == GUI.ACTION.LEFT)
            movePlayerLeft();

        // Check if there's a pending monster combat from movement
        if (pendingMonster != null) {
            Monster monster = pendingMonster;
            pendingMonster = null;
            game.setState(stateFactory.createCombatState(getModel(), monster));
            return;
        }

        // Handle other actions
        if (action == GUI.ACTION.SELECT && isPlayerNearShopKeeper()) {
            game.setState(stateFactory.createShopState(getModel()));
        }
        if (action == GUI.ACTION.INVENTORY) {
            game.setState(stateFactory.createInventoryState(getModel()));
        }
        if (action == GUI.ACTION.QUIT)
            game.setState(null);
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

        // Check if player is near shopkeeper after moving
        checkShopKeeperCollision();
    }

    private com.aor.player.model.game.elements.Monster pendingMonster = null;

    private Monster checkMonsterAtPosition(Position position) {
        // Check if any part of the player sprite (7x7) would collide with any monster
        for (int dy = 0; dy < getModel().getPlayer().getSpriteHeight(); dy++) {
            for (int dx = 0; dx < getModel().getPlayer().getSpriteWidth(); dx++) {
                Position checkPos = new Position(position.getX() + dx, position.getY() + dy);
                for (Monster monster : getModel().getMonsters()) {
                    if (monster.getPosition().equals(checkPos)) {
                        return monster;
                    }
                }
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

    private void checkShopKeeperCollision() {
        if (getModel().isPlayerNearShopKeeper()) {
            // Player touched shopkeeper - open shop menu
            // Note: We need access to Game to change state
            // This will be handled in the step method
        }
    }

    public boolean isPlayerNearShopKeeper() {
        return getModel().isPlayerNearShopKeeper();
    }

    private void checkDoorTransition() {
        Position playerPos = getModel().getPlayer().getPosition();

        // Check all cells that the player sprite occupies (7x7)
        for (int dy = 0; dy < getModel().getPlayer().getSpriteHeight(); dy++) {
            for (int dx = 0; dx < getModel().getPlayer().getSpriteWidth(); dx++) {
                Position checkPos = new Position(playerPos.getX() + dx, playerPos.getY() + dy);
                var door = getModel().getDoorAt(checkPos);
                if (door != null) {
                    // Transition to the target room
                    getModel().changeRoom(door.getTargetRoomId(), door.getSpawnPosition());
                    return; // Exit after first door found
                }
            }
        }
    }

}
