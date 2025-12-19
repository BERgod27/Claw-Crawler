package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.game.DungeonController;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.game.GameViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameStateTest {

    private Dungeon mockDungeon;
    private StateFactory mockStateFactory;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        mockDungeon = mock(Dungeon.class);
        mockStateFactory = mock(StateFactory.class);
        gameState = new GameState(mockDungeon, mockStateFactory);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockDungeon, gameState.getModel());
    }

    @Test
    void testGetViewerReturnsGameViewer() {
        Viewer<Dungeon> viewer = gameState.getViewer();

        assertNotNull(viewer);
        assertTrue(viewer instanceof GameViewer);
    }

    @Test
    void testGetControllerReturnsDungeonController() {
        Controller<Dungeon> controller = gameState.getController();

        assertNotNull(controller);
        assertTrue(controller instanceof DungeonController);
    }

    @Test
    void testGetControllerReturnsSameInstance() {
        Controller<Dungeon> controller1 = gameState.getController();
        Controller<Dungeon> controller2 = gameState.getController();

        assertSame(controller1, controller2);
    }

    @Test
    void testGetViewerCreatesNewInstance() {
        Viewer<Dungeon> viewer1 = gameState.getViewer();
        Viewer<Dungeon> viewer2 = gameState.getViewer();

        // New GameViewer instances are created each time
        assertNotSame(viewer1, viewer2);
    }

    @Test
    void testConstructorInitializesController() {
        Controller<Dungeon> controller = gameState.getController();
        assertNotNull(controller);
    }

    @Test
    void testModelPassedToController() {
        // Verify the model is correctly stored
        assertSame(mockDungeon, gameState.getModel());
    }
}
