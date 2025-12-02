package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.instructions.InstructionsController;
import com.aor.player.model.instructions.Instructions;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.instructions.InstructionsViewer;

public class InstructionsState extends State<Instructions> {
    private final InstructionsViewer viewer;
    private final InstructionsController controller;

    public InstructionsState(Instructions instructions) {
        super(instructions);
        this.viewer = new InstructionsViewer(instructions);
        this.controller = new InstructionsController(instructions);
    }

    @Override
    protected Viewer<Instructions> getViewer() {
        return viewer;
    }

    @Override
    protected Controller<Instructions> getController() {
        return controller;
    }
}
