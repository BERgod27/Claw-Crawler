package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.inventory.InventoryController;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.inventory.InventoryViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryStateTest {

    private Dungeon mockDungeon;
    private StateFactory mockStateFactory;
    private InventoryState inventoryState;

    @BeforeEach
    void setUp() {
        mockDungeon = mock(Dungeon.class);
        mockStateFactory = mock(StateFactory.class);
        inventoryState = new InventoryState(mockDungeon, mockStateFactory);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockDungeon, inventoryState.getModel());
    }

    @Test
    void testGetViewerReturnsInventoryViewer() {
        Viewer<Dungeon> viewer = inventoryState.getViewer();

        assertNotNull(viewer);
        assertTrue(viewer instanceof InventoryViewer);
    }

    @Test
    void testGetControllerReturnsInventoryController() {
        Controller<Dungeon> controller = inventoryState.getController();

        assertNotNull(controller);
        assertTrue(controller instanceof InventoryController);
    }

    @Test
    void testGetControllerReturnsSameInstance() {
        Controller<Dungeon> controller1 = inventoryState.getController();
        Controller<Dungeon> controller2 = inventoryState.getController();

        assertSame(controller1, controller2);
    }

    @Test
    void testGetViewerReturnsSameInstance() {
        Viewer<Dungeon> viewer1 = inventoryState.getViewer();
        Viewer<Dungeon> viewer2 = inventoryState.getViewer();

        assertSame(viewer1, viewer2);
    }

    @Test
    void testConstructorInitializesViewer() {
        Viewer<Dungeon> viewer = inventoryState.getViewer();
        assertNotNull(viewer);
    }

    @Test
    void testConstructorInitializesController() {
        Controller<Dungeon> controller = inventoryState.getController();
        assertNotNull(controller);
    }
}
