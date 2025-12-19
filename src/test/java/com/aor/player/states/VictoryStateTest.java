package com.aor.player.states;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.controller.victory.VictoryController;
import com.aor.player.gui.GUI;
import com.aor.player.model.victory.Victory;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.victory.VictoryViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VictoryStateTest {

    private Victory mockVictory;
    private StateFactory mockStateFactory;
    private VictoryState victoryState;

    @BeforeEach
    void setUp() {
        mockVictory = mock(Victory.class);
        mockStateFactory = mock(StateFactory.class);
        victoryState = new VictoryState(mockVictory, mockStateFactory);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockVictory, victoryState.getModel());
    }

    @Test
    void testGetViewerReturnsVictoryViewer() {
        Viewer<Victory> viewer = victoryState.getViewer();

        assertNotNull(viewer);
        assertTrue(viewer instanceof VictoryViewer);
    }

    @Test
    void testGetControllerReturnsVictoryController() {
        Controller<Victory> controller = victoryState.getController();

        assertNotNull(controller);
        assertTrue(controller instanceof VictoryController);
    }

    @Test
    void testGetControllerReturnsSameInstance() {
        Controller<Victory> controller1 = victoryState.getController();
        Controller<Victory> controller2 = victoryState.getController();

        assertSame(controller1, controller2);
    }

    @Test
    void testGetViewerReturnsSameInstance() {
        Viewer<Victory> viewer1 = victoryState.getViewer();
        Viewer<Victory> viewer2 = victoryState.getViewer();

        assertSame(viewer1, viewer2);
    }

    @Test
    void testStepCallsGUIForAction() throws IOException {
        Game mockGame = mock(Game.class);
        GUI mockGUI = mock(GUI.class);
        when(mockGUI.getNextAction()).thenReturn(GUI.ACTION.QUIT);

        victoryState.step(mockGame, mockGUI, 1000L);

        verify(mockGUI).getNextAction();
    }

    @Test
    void testStepWithSelectAction() throws IOException {
        Game mockGame = mock(Game.class);
        GUI mockGUI = mock(GUI.class);
        when(mockGUI.getNextAction()).thenReturn(GUI.ACTION.SELECT);

        victoryState.step(mockGame, mockGUI, 1000L);

        verify(mockGUI).getNextAction();
    }

    @Test
    void testStepWithMultipleActions() throws IOException {
        Game mockGame = mock(Game.class);
        GUI mockGUI = mock(GUI.class);

        when(mockGUI.getNextAction()).thenReturn(GUI.ACTION.SELECT);
        victoryState.step(mockGame, mockGUI, 1000L);

        when(mockGUI.getNextAction()).thenReturn(GUI.ACTION.QUIT);
        victoryState.step(mockGame, mockGUI, 2000L);

        verify(mockGUI, times(2)).getNextAction();
    }
}
