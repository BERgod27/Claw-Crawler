package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;

public class RoomArea {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public RoomArea(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getCenter() {
        return new Position(x + width / 2, y + height / 2);
    }

    public boolean overlaps(RoomArea other) {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }

    public boolean contains(Position position) {
        return position.getX() >= x && position.getX() < x + width &&
                position.getY() >= y && position.getY() < y + height;
    }
}
