package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.menu.InstructionsController;
import com.aor.player.model.menu.Instructions;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.menu.InstructionsViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructionsStateTest {

    private Instructions mockInstructions;
    private StateFactory mockStateFactory;
    private InstructionsState instructionsState;

    @BeforeEach
    void setUp() {
        mockInstructions = mock(Instructions.class);
        mockStateFactory = mock(StateFactory.class);
        instructionsState = new InstructionsState(mockInstructions, mockStateFactory);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockInstructions, instructionsState.getModel());
    }

    @Test
    void testGetViewerReturnsInstructionsViewer() {
        Viewer<Instructions> viewer = instructionsState.getViewer();

        assertNotNull(viewer);
        assertTrue(viewer instanceof InstructionsViewer);
    }

    @Test
    void testGetControllerReturnsInstructionsController() {
        Controller<Instructions> controller = instructionsState.getController();

        assertNotNull(controller);
        assertTrue(controller instanceof InstructionsController);
    }

    @Test
    void testGetViewerCreatesNewInstance() {
        Viewer<Instructions> viewer1 = instructionsState.getViewer();
        Viewer<Instructions> viewer2 = instructionsState.getViewer();

        // New instances created each time
        assertNotSame(viewer1, viewer2);
    }

    @Test
    void testGetControllerCreatesNewInstance() {
        Controller<Instructions> controller1 = instructionsState.getController();
        Controller<Instructions> controller2 = instructionsState.getController();

        // New instances created each time
        assertNotSame(controller1, controller2);
    }

    @Test
    void testConstructorInitializesViewer() {
        Viewer<Instructions> viewer = instructionsState.getViewer();
        assertNotNull(viewer);
    }

    @Test
    void testConstructorInitializesController() {
        Controller<Instructions> controller = instructionsState.getController();
        assertNotNull(controller);
    }
}
