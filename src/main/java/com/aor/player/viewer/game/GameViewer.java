package com.aor.player.viewer.game;

import com.aor.player.controller.game.DungeonController;
import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Element;
import com.aor.player.viewer.Viewer;

import java.util.List;

public class GameViewer extends Viewer<Dungeon> {
    private final DungeonController controller;

    public GameViewer(Dungeon dungeon, DungeonController controller) {
        super(dungeon);
        this.controller = controller;
    }

    @Override
    public void drawElements(GUI gui) {
        drawElements(gui, getModel().getWalls(), new WallViewer());
        drawElements(gui, getModel().getMonsters(), new MonsterViewer());
        drawElements(gui, getModel().getDoors(), new DoorViewer());

        // Draw chests
        drawChests(gui, getModel().getChests());

        // Draw shopkeeper if present in current room
        if (getModel().getCurrentRoom() != null && getModel().getCurrentRoom().getShopKeeper() != null) {
            new ShopKeeperViewer().draw(getModel().getCurrentRoom().getShopKeeper(), gui);
        }

        drawElement(gui, getModel().getPlayer(), new PlayerViewer());

        // Display player stats horizontally at row 36 (below bottom boundary wall at
        // row 35)
        String stats = String.format("HP: %d | Armor: %d | Attack: %d",
                getModel().getPlayer().getHealth(),
                getModel().getPlayer().getArmor(),
                getModel().getPlayer().getAttack());
        gui.drawText(new Position(2, 36), stats, "#FFD700");

        // Display quit confirmation prompt (takes priority over other prompts)
        if (controller.isQuitConfirmation()) {
            gui.drawText(new Position(2, 37), "Press Q again to quit, or any key to cancel", "#FF0000");
        } else {
            // Display interaction prompts only when not in quit confirmation mode
            boolean nearShop = getModel().isPlayerNearShopKeeper();
            boolean nearChest = getModel().getChestNearPlayer() != null;

            if (nearShop && nearChest) {
                // Both prompts - display on one line
                gui.drawText(new Position(2, 37), "Press ENTER: open shop | open chest", "#00FF00");
            } else if (nearShop) {
                gui.drawText(new Position(2, 37), "Press ENTER to open shop", "#00FF00");
            } else if (nearChest) {
                gui.drawText(new Position(2, 37), "Press ENTER to open chest", "#00FF00");
            }
        }
    }

    private void drawChests(GUI gui, java.util.List<com.aor.player.model.game.elements.Chest> chests) {
        ChestViewer chestViewer = new ChestViewer();
        for (com.aor.player.model.game.elements.Chest chest : chests) {
            chestViewer.draw(chest, gui);
        }
    }

    private <T extends Element> void drawElements(GUI gui, List<T> elements, ElementViewer<T> viewer) {
        for (T element : elements)
            drawElement(gui, element, viewer);
    }

    private <T extends Element> void drawElement(GUI gui, T element, ElementViewer<T> viewer) {
        viewer.draw(element, gui);
    }
}