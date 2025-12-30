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
        // We can't verify getter, but we can verify it's used in start() - see
        // testStart
    }

    @Test
    @org.junit.jupiter.api.Timeout(value = 2, unit = java.util.concurrent.TimeUnit.SECONDS)
    void testStart() throws IOException {
        State mockState = mock(State.class);
        game.setState(mockState);

        // Define behavior: when step is called, set state to null to break loop
        doAnswer(invocation -> {
            game.setState(null);
            return null;
        }).when(mockState).step(any(Game.class), any(LanternaGUI.class), anyLong());

        game.start();

        // Verify loop executed step at least once
        verify(mockState, atLeastOnce()).step(any(Game.class), any(LanternaGUI.class), anyLong());

        // Verify cleanup
        verify(gui).close();
    }
}
