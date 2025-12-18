package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.victory.VictoryController;
import com.aor.player.model.victory.Victory;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.victory.VictoryViewer;

public class VictoryState extends State<Victory> {
    private final VictoryController controller;
    private final VictoryViewer viewer;

    public VictoryState(Victory victory, StateFactory stateFactory) {
        super(victory);
        this.controller = new VictoryController(victory, stateFactory);
        this.viewer = new VictoryViewer(victory);
    }

    @Override
    public Viewer<Victory> getViewer() {
        return viewer;
    }

    @Override
    public Controller<Victory> getController() {
        return controller;
    }
}
