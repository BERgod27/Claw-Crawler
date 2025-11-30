package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.Game;
import com.aor.player.viewer.Viewer;

import java.io.IOException;

public abstract class State<T> {
    private final T model;

    public State(T model) {
        this.model = model;
    }

    protected abstract Viewer<T> getViewer();

    protected abstract Controller<T> getController();

    public T getModel() {
        return model;
    }

    public void step(Game game, GUI gui, long time) throws IOException {
        GUI.ACTION action = gui.getNextAction();
        getController().step(game, action, time);
        getViewer().draw(gui);
    }
}
