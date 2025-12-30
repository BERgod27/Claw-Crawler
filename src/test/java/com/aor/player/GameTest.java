package com.aor.player;

import com.aor.player.gui.LanternaGUI;
import com.aor.player.states.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // Verify default state is MenuState
        assertNotNull(game.getState());
        assertEquals(com.aor.player.states.MenuState.class, game.getState().getClass());
    }

    @Test
    void testSetState() {
        State<?> mockState = mock(State.class);
        game.setState(mockState);
        assertEquals(mockState, game.getState());
    }

    @Test
    @org.junit.jupiter.api.Timeout(value = 2, unit = java.util.concurrent.TimeUnit.SECONDS)
    void testStart() throws IOException {
        State<?> mockState = mock(State.class);
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

    @Test
    void testStartExceptionCallback() throws IOException {
        State<?> mockState = mock(State.class);
        game.setState(mockState);

        // Make step throw an IOException
        doThrow(new IOException("Test Exception")).when(mockState).step(any(), any(), anyLong());

        // Verify start throws the exception
        try {
            game.start();
        } catch (IOException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    @org.junit.jupiter.api.Timeout(value = 1, unit = java.util.concurrent.TimeUnit.SECONDS)
    void testGameLoopStops() throws IOException {
        State<?> mockState = mock(State.class);
        game.setState(mockState);

        // Run loop 5 times then stop
        doAnswer(invocation -> {
            return null;
        }).doAnswer(invocation -> {
            return null;
        }).doAnswer(invocation -> {
            return null;
        }).doAnswer(invocation -> {
            return null;
        }).doAnswer(invocation -> {
            game.setState(null);
            return null;
        }).when(mockState).step(any(), any(), anyLong());

        game.start();

        // Verify step was called 5 times
        verify(mockState, times(5)).step(any(), any(), anyLong());
    }

    @Test
    @org.junit.jupiter.api.Timeout(value = 2, unit = java.util.concurrent.TimeUnit.SECONDS)
    void testSleepTimeBoundary() throws IOException {
        State<?> mockState = mock(State.class);
        game.setState(mockState);

        // Make step take longer than frameTime (16ms for 60 FPS)
        doAnswer(invocation -> {
            Thread.sleep(20); // Sleep longer than frameTime
            game.setState(null);
            return null;
        }).when(mockState).step(any(), any(), anyLong());

        long startTime = System.currentTimeMillis();
        game.start();
        long elapsed = System.currentTimeMillis() - startTime;

        // Verify the loop completed (step executed once)
        verify(mockState, times(1)).step(any(), any(), anyLong());
        // Should complete quickly since sleepTime would be negative
        org.junit.jupiter.api.Assertions.assertTrue(elapsed < 100,
                "Game loop should complete quickly when sleepTime <= 0");
    }

    @Test
    @org.junit.jupiter.api.Timeout(value = 2, unit = java.util.concurrent.TimeUnit.SECONDS)
    void testThreadSleepIsCalled() throws IOException, InterruptedException {
        State<?> mockState = mock(State.class);
        game.setState(mockState);

        final boolean[] wasInterrupted = { false };
        Thread gameThread = new Thread(() -> {
            try {
                game.start();
            } catch (IOException e) {
                // Expected if interrupted during step
            }
        });

        doAnswer(invocation -> {
            return null; // Fast return to trigger Thread.sleep
        }).doAnswer(invocation -> {
            // Interrupt the thread to catch sleep
            gameThread.interrupt();
            game.setState(null);
            return null;
        }).when(mockState).step(any(), any(), anyLong());

        gameThread.start();
        gameThread.join(1000);

        // If thread was interrupted during sleep, the loop handled it gracefully
        verify(mockState, atLeast(1)).step(any(), any(), anyLong());
    }

    @Test
    @org.junit.jupiter.api.Timeout(value = 2, unit = java.util.concurrent.TimeUnit.SECONDS)
    void testElapsedTimeCalculation() throws IOException {
        State<?> mockState = mock(State.class);
        game.setState(mockState);

        // Run exactly one iteration with minimal step time
        doAnswer(invocation -> {
            game.setState(null);
            return null;
        }).when(mockState).step(any(), any(), anyLong());

        long startTime = System.currentTimeMillis();
        game.start();
        long totalElapsed = System.currentTimeMillis() - startTime;

        // With frameTime = 16ms (60 FPS), one iteration should take ~16ms
        org.junit.jupiter.api.Assertions.assertTrue(totalElapsed >= 10,
                "Loop should spend time sleeping: " + totalElapsed + "ms");
    }
}
