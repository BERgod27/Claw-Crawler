package com.aor.player.viewer.victory;

import com.aor.player.gui.GUI;
import com.aor.player.model.victory.Victory;
import com.aor.player.viewer.Viewer;

import java.io.IOException;

public class VictoryViewer extends Viewer<Victory> {

    public VictoryViewer(Victory victory) {
        super(victory);
    }

    @Override
    public void drawElements(GUI gui) {
        gui.clear();

        // Draw victory banner
        gui.drawText(28, 8, "===========================", "#FFD700");
        gui.drawText(28, 9, "       VICTORY!            ", "#FFD700");
        gui.drawText(28, 10, "===========================", "#FFD700");

        gui.drawText(23, 12, "All bosses have been defeated!", "#00FF00");
        gui.drawText(26, 13, "You are the champion!", "#00FF00");

        // Draw scoreboard
        gui.drawText(28, 16, "===== SCOREBOARD =====", "#FFFF00");

        String timeText = "Time to Victory: " + getModel().getFormattedTime();
        gui.drawText(27, 18, timeText, "#FFFFFF");

        String goldText = "Total Gold: " + getModel().getTotalGold();
        gui.drawText(27, 20, goldText, "#FFFFFF");

        String slimesText = "Slimes Defeated: " + getModel().getSlimesKilled();
        gui.drawText(27, 22, slimesText, "#FFFFFF");

        gui.drawText(28, 25, "======================", "#FFFF00");

        // Draw controls
        gui.drawText(26, 28, "ENTER: New Game | Q: Exit", "#888888");

        try {
            gui.refresh();
        } catch (IOException e) {
            // Ignore refresh errors
        }
    }
}
