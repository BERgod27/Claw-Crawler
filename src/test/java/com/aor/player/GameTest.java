package com.aor.player;

import com.aor.player.gui.LanternaGUI;
import com.aor.player.states.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GameTest {
    private LanternaGUI gui;
    private Game game;

    @BeforeEach
    void setUp() {
        gui = mock(LanternaGUI.class);
        game = new Game(gui);
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game);
    }

    @Test
    void testSetState() {
        State mockState = mock(State.class);
        game.setState(mockState);
        // Since state is private and has no getter, we can't verify it directly easily
        // without reflection or a getter.
        // But we can verify behavior in start() if we could run it safely.
        // For now, just ensuring no exception is thrown.
    }
}
