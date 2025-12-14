package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.combat.CombatController;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.combat.CombatViewer;

/**
 * State representing a combat encounter with a monster.
 * Uses CombatController for combat logic and CombatViewer for rendering.
 */
public class CombatState extends State<Dungeon> {
    private final CombatController controller;
    private final CombatViewer viewer;

    /**
     * Creates a combat state
     * 
     * @param dungeon      The game dungeon
     * @param monster      The monster to fight
     * @param stateFactory Factory for creating states
     */
    public CombatState(Dungeon dungeon, Monster monster, StateFactory stateFactory) {
        super(dungeon);
        this.controller = new CombatController(dungeon, monster, stateFactory);
        this.viewer = new CombatViewer(dungeon, monster);
    }

    @Override
    public Viewer<Dungeon> getViewer() {
        // Update viewer with current controller state
        viewer.setSelectedAction(controller.getSelectedAction());
        viewer.setCombatLog(controller.getCombatLog());
        return viewer;
    }

    @Override
    public Controller<Dungeon> getController() {
        return controller;
    }
}
