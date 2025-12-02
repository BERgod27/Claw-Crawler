package com.aor.player.viewer.menu;

import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.menu.Instructions;
import com.aor.player.viewer.Viewer;

public class InstructionsViewer extends Viewer<Instructions> {
    public InstructionsViewer(Instructions instructions) {
        super(instructions);
    }

    @Override
    public void drawElements(GUI gui) {
        // Draw title
        gui.drawText(new Position(5, 3), getModel().getTitle(), "#FFD700");

        // Draw instruction lines
        String[] lines = getModel().getInstructionLines();
        for (int i = 0; i < lines.length; i++) {
            String color = lines[i].isEmpty() ? "#FFFFFF" : (lines[i].contains(":") ? "#00FFFF" : "#FFFFFF");
            gui.drawText(new Position(5, 5 + i), lines[i], color);
        }
    }
}
