package com.aor.player.model.game.elements;

import com.aor.player.model.Position;

public class Door extends Element {
    private final String targetRoomId;
    private final Position spawnPosition;

    public Door(int x, int y, String targetRoomId, Position spawnPosition) {
        super(x, y);
        this.targetRoomId = targetRoomId;
        this.spawnPosition = spawnPosition;
    }

    public String getTargetRoomId() {
        return targetRoomId;
    }

    public Position getSpawnPosition() {
        return spawnPosition;
    }
}
