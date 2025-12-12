package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.elements.Door;

public class DoorViewer implements ElementViewer<Door> {
    @Override
    public void draw(Door door, GUI gui) {
        gui.drawDoor(door.getPosition());
    }
}
