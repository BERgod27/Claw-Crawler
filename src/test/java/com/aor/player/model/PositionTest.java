package com.aor.player.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
