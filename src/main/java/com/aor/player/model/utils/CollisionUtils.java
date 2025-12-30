package com.aor.player.model.utils;

import com.aor.player.model.Position;

public class CollisionUtils {

    /**
     * Checks if two rectangles overlap (Axis-Aligned Bounding Box collision).
     *
     * @param pos1 Position of the first rectangle (top-left)
     * @param w1   Width of the first rectangle
     * @param h1   Height of the first rectangle
     * @param pos2 Position of the second rectangle (top-left)
     * @param w2   Width of the second rectangle
     * @param h2   Height of the second rectangle
     * @return true if the rectangles overlap, false otherwise
     */
    public static boolean checkCollision(Position pos1, int w1, int h1, Position pos2, int w2, int h2) {
        int x1 = pos1.getX();
        int y1 = pos1.getY();

        int x2 = pos2.getX();
        int y2 = pos2.getY();

        return x1 < x2 + w2 && x1 + w1 > x2 &&
                y1 < y2 + h2 && y1 + h1 > y2;
    }

    /**
     * Checks if two rectangles are adjacent or overlapping (within a tolerance).
     * useful for interaction ranges.
     *
     * @param pos1      Position of the first rectangle
     * @param w1        Width of the first rectangle
     * @param h1        Height of the first rectangle
     * @param pos2      Position of the second rectangle
     * @param w2        Width of the second rectangle
     * @param h2        Height of the second rectangle
     * @param tolerance Extra pixels to expand the checking box by (e.g. 1 for
     *                  touching)
     * @return true if they are within tolerance
     */
    public static boolean checkAdjacency(Position pos1, int w1, int h1, Position pos2, int w2, int h2, int tolerance) {
        int x1 = pos1.getX();
        int y1 = pos1.getY();

        int x2 = pos2.getX();
        int y2 = pos2.getY();

        return x1 < x2 + w2 + tolerance && x1 + w1 > x2 - tolerance &&
                y1 < y2 + h2 + tolerance && y1 + h1 > y2 - tolerance;
    }
}
