package com.aor.player.model.game.elements;

import com.aor.player.model.Position;

public class Chest {

    private final Position position;
    private boolean opened;

    public Chest(Position position) {
        this.position = position;
        this.opened = false;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isOpened() {
        return opened;
    }

    public int open() {
        if (opened) return 0;
        opened = true;
        return 20;
    }
}
