package com.aor.player.viewer.game;


import com.aor.player.gui.GUI;
import com.aor.player.model.game.elements.player;

public class HeroViewer implements ElementViewer<player> {
    @Override
    public void draw(player hero, GUI gui) {
        gui.drawPlayer(hero.getPosition());
    }
}