package com.aor.player.viewer.combat;

import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.Player;
import com.aor.player.viewer.Viewer;

import java.util.List;

/**
 * Viewer for rendering the combat UI.
 * Displays player stats, monster stats, combat actions, and combat log.
 */
public class CombatViewer extends Viewer<Dungeon> {
    private final Monster monster;
    private int selectedAction;
    private List<String> combatLog;

    /**
     * Creates a combat viewer
     * 
     * @param model   The dungeon model
     * @param monster The monster being fought
     */
    public CombatViewer(Dungeon model, Monster monster) {
        super(model);
        this.monster = monster;
        this.selectedAction = 0;
        this.combatLog = List.of();
    }

    /**
     * Sets the currently selected action
     * 
     * @param selectedAction Action index (0-3)
     */
    public void setSelectedAction(int selectedAction) {
        this.selectedAction = selectedAction;
    }

    /**
     * Sets the combat log messages
     * 
     * @param combatLog List of log messages
     */
    public void setCombatLog(List<String> combatLog) {
        this.combatLog = combatLog;
    }

    @Override
    public void draw(GUI gui) {
        gui.clear();
        drawCombatUI(gui);
    }

    @Override
    public void drawElements(GUI gui) {
        // Combat viewer doesn't need separate element drawing
        // All rendering is done in drawCombatUI
    }

    /**
     * Draws the complete combat UI
     * 
     * @param gui The GUI to draw on
     */
    private void drawCombatUI(GUI gui) {
        Player player = getModel().getPlayer();

        // Title
        gui.drawText(new Position(2, 1), "=== COMBAT ===", "#FFFF00");

        // Player stats
        gui.drawText(new Position(2, 3), "PLAYER", "#00FF00");
        gui.drawText(new Position(2, 4), "HP: " + player.getHealth() + "/" + player.getMaxHealth(),
                getHealthColor(player.getHealth(), player.getMaxHealth()));
        gui.drawText(new Position(2, 5), "ATK: " + player.getAttack(), "#FF8800");
        gui.drawText(new Position(2, 6), "DEF: " + player.getArmor(), "#00FFFF");

        // Monster stats
        gui.drawText(new Position(30, 3), monster.getName().toUpperCase(), "#FF0000");
        gui.drawText(new Position(30, 4), "HP: " + monster.getHealth() + "/" + monster.getMaxHealth(),
                getHealthColor(monster.getHealth(), monster.getMaxHealth()));
        gui.drawText(new Position(30, 5), "ATK: " + monster.getAttack(), "#FF8800");
        gui.drawText(new Position(30, 6), "DEF: " + monster.getDefense(), "#00FFFF");

        // Combat log
        gui.drawText(new Position(2, 8), "--- Combat Log ---", "#AAAAAA");
        int logY = 9;
        for (String logEntry : combatLog) {
            gui.drawText(new Position(2, logY), logEntry, "#FFFFFF");
            logY++;
        }

        // Actions menu
        gui.drawText(new Position(2, 15), "--- Actions ---", "#AAAAAA");
        drawAction(gui, 0, "Attack", 16);
        drawAction(gui, 1, "Defend", 17);
        drawAction(gui, 2, "Use Item", 18);
        drawAction(gui, 3, "Flee", 19);

        // Instructions
        gui.drawText(new Position(2, 21), "Arrow keys to select, ENTER to confirm", "#888888");
    }

    /**
     * Draws a single action option
     * 
     * @param gui         The GUI to draw on
     * @param actionIndex The action's index
     * @param actionName  The action's name
     * @param y           The y position to draw at
     */
    private void drawAction(GUI gui, int actionIndex, String actionName, int y) {
        String prefix = (actionIndex == selectedAction) ? "> " : "  ";
        String color = (actionIndex == selectedAction) ? "#FFFF00" : "#FFFFFF";
        gui.drawText(new Position(2, y), prefix + actionName, color);
    }

    /**
     * Gets a color based on health percentage
     * 
     * @param current Current health
     * @param max     Maximum health
     * @return Color string
     */
    private String getHealthColor(int current, int max) {
        double percentage = (double) current / max;
        if (percentage > 0.6) {
            return "#00FF00"; // Green
        } else if (percentage > 0.3) {
            return "#FFFF00"; // Yellow
        } else {
            return "#FF0000"; // Red
        }
    }
}
