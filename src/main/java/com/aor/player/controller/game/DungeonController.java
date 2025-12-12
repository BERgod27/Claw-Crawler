package com.aor.player.controller.game;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;

import java.io.IOException;

public class DungeonController extends Controller<Dungeon> {
    public DungeonController(Dungeon dungeon) {
        super(dungeon);
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        if (action == GUI.ACTION.UP)
            movePlayerUp();
        if (action == GUI.ACTION.RIGHT)
            movePlayerRight();
        if (action == GUI.ACTION.DOWN)
            movePlayerDown();
        if (action == GUI.ACTION.LEFT)
            movePlayerLeft();
        if (action == GUI.ACTION.SELECT && isPlayerNearShopKeeper()) {
            // Open shop menu
            game.setState(new com.aor.player.states.ShopState(new com.aor.player.model.game.shop.Shop(), getModel()));
        }
        if (action == GUI.ACTION.INVENTORY) {
            // Open inventory
            game.setState(new com.aor.player.states.InventoryState(getModel()));
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

    private void movePlayer(Position position) {
        getModel().movePlayer(position);

        // Check if player is on a door after moving
        checkDoorTransition();

        // Check if player is near shopkeeper
        checkShopKeeperCollision();
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

    