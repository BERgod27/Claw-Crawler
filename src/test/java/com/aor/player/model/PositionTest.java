package com.aor.player.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PositionTest {
    @Test
    public void getLeft() {
        Position p = new Position(10, 10);
        assertEquals(9, p.getLeft().getX());
        assertEquals(10, p.getLeft().getY());
    }

    @Test
    public void getRight() {
        Position p = new Position(10, 10);
        assertEquals(11, p.getRight().getX());
        assertEquals(10, p.getRight().getY());
    }

    @Test
    public void getUp() {
        Position p = new Position(10, 10);
        assertEquals(10, p.getUp().getX());
        assertEquals(9, p.getUp().getY());
    }

    @Test
    public void getDown() {
        Position p = new Position(10, 10);
        assertEquals(10, p.getDown().getX());
        assertEquals(11, p.getDown().getY());
    }

    @Test
    void testEquals() {
        Position p1 = new Position(10, 10);
        Position p2 = new Position(10, 10);
        Position p3 = new Position(11, 10);

        assertEquals(p1, p1);
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p1, null);
        assertNotEquals(p1, new Object());
    }

    @Test
    void testHashCode() {
        Position p1 = new Position(10, 10);
        Position p2 = new Position(10, 10);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testGetRandomNeighbour() {
        Position p = new Position(10, 10);
        Position neighbour = p.getRandomNeighbour();

        // The neighbour must be one of the adjacent positions
        List<Position> validNeighbours = List.of(
                p.getUp(), p.getDown(), p.getLeft(), p.getRight());

        assertTrue(validNeighbours.contains(neighbour));
    }
}
