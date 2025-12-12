package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Element;
import com.aor.player.viewer.Viewer;

import java.util.List;

public class GameViewer extends Viewer<Dungeon> {
    public GameViewer(Dungeon dungeon) {
        super(dungeon);
    }

    @Override
    public void drawElements(GUI gui) {
        drawElements(gui, getModel().getWalls(), new WallViewer());
        drawElements(gui, getModel().getMonsters(), new MonsterViewer());
        drawElements(gui, getModel().getDoors(), new DoorViewer());

        // Draw shopkeeper if present in current room
        if (getModel().getCurrentRoom() != null && getModel().getCurrentRoom().getShopKeeper() != null) {
            new ShopKeeperViewer().draw(getModel().getCurrentRoom().getShopKeeper(), gui);
        }

        drawElement(gui, getModel().getPlayer(), new PlayerViewer());

        // Display player stats horizontally at row 36 (below bottom boundary wall at
        // row 35)
        String stats = String.format("HP: %d | Armor: %d | Speed: %d",
                getModel().getPlayer().getHealth(),
                getModel().getPlayer().getArmor(),
                getModel().getPlayer().getSpeed());
        gui.drawText(new Position(2, 36), stats, "#FFD700");
    }

    private <T extends Element> void drawElements(GUI gui, List<T> elements, ElementViewer<T> viewer) {
        for (T element : elements)
            drawElement(gui, element, viewer);
    }

    private <T extends Element> void drawElement(GUI gui, T element, ElementViewer<T> viewer) {
        viewer.draw(element, gui);
    }
}