package com.aor.player.viewer.game;
import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Element;
import com.aor.player.model.game.elements.Wall;
import com.aor.player.viewer.Viewer;

import java.util.List;

public class WallViewer implements ElementViewer<Wall> {
    @Override
    public void draw(Wall wall, GUI gui) {
        gui.drawWall(wall.getPosition());
    }
}

