package com.aor.player.model.utils;

import com.aor.player.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionUtilsTest {

    @Test
    void checkCollision_overlapping() {
        // Overlapping rectangles
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(2, 2);
        assertTrue(CollisionUtils.checkCollision(pos1, 5, 5, pos2, 5, 5));
    }

    @Test
    void checkCollision_nonOverlapping() {
        // Far apart rectangles
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(10, 10);
        assertFalse(CollisionUtils.checkCollision(pos1, 5, 5, pos2, 5, 5));
    }

    @Test
    void checkCollision_touching() {
        // Touching edges should NOT count as collision in AABB usually (open interval)
        // or closed interval depending on implementation.
        // Implementation: x1 < x2 + w2 && x1 + w1 > x2
        // e.g. pos1(0,0) w=5 -> range [0, 5) if using pixels 0,1,2,3,4
        // pos2(5,0) w=5 -> range [5, 10). They don't overlap.

        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(5, 0);
        assertFalse(CollisionUtils.checkCollision(pos1, 5, 5, pos2, 5, 5));
    }

    @Test
    void checkAdjacency_touching() {
        // Touching should be adjacent with tolerance 1
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(5, 0);
        assertTrue(CollisionUtils.checkAdjacency(pos1, 5, 5, pos2, 5, 5, 1));
    }

    @Test
    void checkAdjacency_notTouching() {
        // Gap of 1 pixel (pos1 ends at 5, pos2 starts at 7)
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(7, 0);
        // Tolerance 1 allows gap 0 (touching).
        // x1 < x2 + w2 + tol && x1 + w1 > x2 - tol
        // 0 < 7 + 5 + 1 (13) && 5 > 7 - 1 (6) -> FALSE
        assertFalse(CollisionUtils.checkAdjacency(pos1, 5, 5, pos2, 5, 5, 1));
    }

    @Test
    void checkAdjacency_overlapping() {
        // Overlapping should also be "adjacent"
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(2, 2);
        assertTrue(CollisionUtils.checkAdjacency(pos1, 5, 5, pos2, 5, 5, 1));
    }
}
