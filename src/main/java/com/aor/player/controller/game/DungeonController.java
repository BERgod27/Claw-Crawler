package com.aor.player.controller.game;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Chest;

import java.io.IOException;

public class DungeonController extends Controller<Dungeon> {
    public DungeonController(Dungeon dungeon) {
        super(dungeon);
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        switch (action) {
            case UP:
                movePlayerUp(game);
                break;
            case RIGHT:
                movePlayerRight(game);
                break;
            case DOWN:
                movePlayerDown(game);
                break;
            case LEFT:
                movePlayerLeft(game);
                break;
            case SELECT:
                checkShopKeeper(game);
                checkChestInteraction();
                break;
            case QUIT:
                game.setState(null);
                break;
            default:
                break;
        }
    }

    private void movePlayerLeft(Game game) { movePlayer(game, getModel().getPlayer().getPosition().getLeft()); }
    private void movePlayerRight(Game game) { movePlayer(game, getModel().getPlayer().getPosition().getRight()); }
    private void movePlayerUp(Game game) { movePlayer(game, getModel().getPlayer().getPosition().getUp()); }
    private void movePlayerDown(Game game) { movePlayer(game, getModel().getPlayer().getPosition().getDown()); }

    private void movePlayer(Game game, Position position) {
        getModel().movePlayer(position);

        // Check for door transition
        checkDoorTransition();

        // Check for shopkeeper interaction
        checkShopKeeper(game);

        // Check for chest interaction automatically on move
        checkChestInteraction();
    }

    private void checkDoorTransition() {
        Position playerPos = getModel().getPlayer().getPosition();
        for (int dy = 0; dy < getModel().getPlayer().getSpriteHeight(); dy++) {
            for (int dx = 0; dx < getModel().getPlayer().getSpriteWidth(); dx++) {
                Position checkPos = new Position(playerPos.getX() + dx, playerPos.getY() + dy);
                var door = getModel().getDoorAt(checkPos);
                if (door != null) {
                    getModel().changeRoom(door.getTargetRoomId(), door.getSpawnPosition());
                    return;
                }
            }
        }
    }

    private void checkShopKeeper(Game game) {
        if (isPlayerNearShopKeeper()) {
            game.setState(new com.aor.player.states.ShopState(
                    new com.aor.player.model.game.shop.Shop(), getModel()
            ));
        }
    }

    private void checkChestInteraction() {
        Position playerPos = getModel().getPlayer().getPosition();
        var currentRoom = getModel().getCurrentRoom();

        // Check all chests in current room
        for (Chest chest : currentRoom.getChests()) {
            if (chest.getPosition().equals(playerPos) && !chest.isOpened()) {
                int gold = chest.open();
                getModel().getPlayer().addGold(gold); // Give player 20 gold
            }
        }
    }

    public boolean isPlayerNearShopKeeper() {
        return getModel().isPlayerNearShopKeeper();
    }
}
