package com.aor.player.viewer.game;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.elements.Monster;

public class MonsterViewer implements ElementViewer<Monster> {
    @Override
    public void draw(Monster monster, GUI gui) {
        gui.drawMonster(monster.getPosition());
    }
}