package com.aor.player.model.utils;

import com.aor.player.model.Position;

public class CollisionUtils {

    public static boolean checkCollision(Position pos1, int w1, int h1, Position pos2, int w2, int h2) {
        int x1 = pos1.getX();
        int y1 = pos1.getY();

        int x2 = pos2.getX();
        int y2 = pos2.getY();

        return x1 < x2 + w2 && x1 + w1 > x2 &&
                y1 < y2 + h2 && y1 + h1 > y2;
    }

    public static boolean checkAdjacency(Position pos1, int w1, int h1, Position pos2, int w2, int h2, int tolerance) {
        int x1 = pos1.getX();
        int y1 = pos1.getY();

        int x2 = pos2.getX();
        int y2 = pos2.getY();

        return x1 < x2 + w2 + tolerance && x1 + w1 > x2 - tolerance &&
                y1 < y2 + h2 + tolerance && y1 + h1 > y2 - tolerance;
    }
}
