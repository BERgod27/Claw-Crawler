package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.menu.MenuController;
import com.aor.player.model.menu.Menu;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.menu.MenuViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuStateTest {

    private Menu mockMenu;
    private StateFactory mockStateFactory;
    private MenuState menuState;

    @BeforeEach
    void setUp() {
        mockMenu = mock(Menu.class);
        mockStateFactory = mock(StateFactory.class);
        menuState = new MenuState(mockMenu, mockStateFactory);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockMenu, menuState.getModel());
    }

    @Test
    void testGetViewerReturnsMenuViewer() {
        Viewer<Menu> viewer = menuState.getViewer();

        assertNotNull(viewer);
        assertTrue(viewer instanceof MenuViewer);
    }

    @Test
    void testGetControllerReturnsMenuController() {
        Controller<Menu> controller = menuState.getController();

        assertNotNull(controller);
        assertTrue(controller instanceof MenuController);
    }

    @Test
    void testGetControllerReturnsSameInstance() {
        Controller<Menu> controller1 = menuState.getController();
        Controller<Menu> controller2 = menuState.getController();

        assertSame(controller1, controller2);
    }

    @Test
    void testGetViewerReturnsSameInstance() {
        Viewer<Menu> viewer1 = menuState.getViewer();
        Viewer<Menu> viewer2 = menuState.getViewer();

        assertSame(viewer1, viewer2);
    }

    @Test
    void testConstructorInitializesViewer() {
        Viewer<Menu> viewer = menuState.getViewer();
        assertNotNull(viewer);
    }

    @Test
    void testConstructorInitializesController() {
        Controller<Menu> controller = menuState.getController();
        assertNotNull(controller);
    }
}
