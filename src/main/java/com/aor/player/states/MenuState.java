package com.aor.player.states;
import com.aor.player.controller.Controller;
import com.aor.player.controller.menu.MenuController;
import com.aor.player.model.menu.Menu;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.menu.MenuViewer;

public class MenuState extends State<Menu> {
    public MenuState(Menu model) {
        super(model);
    }

    @Override
    protected Viewer<Menu> getViewer() {
        return new MenuViewer(getModel());
    }

    @Override
    protected Controller<Menu> getController() {
        return new MenuController(getModel());
    }
}


