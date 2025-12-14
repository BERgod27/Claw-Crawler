package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.menu.InstructionsController;
import com.aor.player.model.menu.Instructions;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.menu.InstructionsViewer;

public class InstructionsState extends State<Instructions> {
    private final StateFactory stateFactory;

    public InstructionsState(Instructions model, StateFactory stateFactory) {
        super(model);
        this.stateFactory = stateFactory;
    }

    @Override
    public Viewer<Instructions> getViewer() {
        return new InstructionsViewer(getModel());
    }

    @Override
    public Controller<Instructions> getController() {
        return new InstructionsController(getModel(), stateFactory);
    }
}
