package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.menu.MenuController;
import com.aor.player.model.game.dungeon.DungeonFactory;
import com.aor.player.model.menu.Menu;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.menu.MenuViewer;

public class MenuState extends State<Menu> {
    private final MenuViewer viewer;
    private final MenuController controller;

    public MenuState(Menu model) {
        super(model);
        this.viewer = new MenuViewer(model);
        this.controller = new MenuController(model, new DungeonFactory());
    }

    @Override
    protected Viewer<Menu> getViewer() {
        return viewer;
    }

    @Override
    protected Controller<Menu> getController() {
        return controller;
    }
}
