package com.aor.player.states;

import com.aor.player.controller.Controller;
import com.aor.player.controller.shop.ShopController;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.shop.Shop;
import com.aor.player.viewer.Viewer;
import com.aor.player.viewer.shop.ShopMenuViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopStateTest {

    private Shop mockShop;
    private Dungeon mockDungeon;
    private Player mockPlayer;
    private StateFactory mockStateFactory;
    private ShopState shopState;

    @BeforeEach
    void setUp() {
        mockShop = mock(Shop.class);
        mockDungeon = mock(Dungeon.class);
        mockPlayer = mock(Player.class);
        mockStateFactory = mock(StateFactory.class);

        when(mockDungeon.getPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getGold()).thenReturn(100);

        shopState = new ShopState(mockShop, mockDungeon, mockStateFactory);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockShop, shopState.getModel());
    }

    @Test
    void testGetDungeonReturnsStoredDungeon() {
        assertEquals(mockDungeon, shopState.getDungeon());
    }

    @Test
    void testGetViewerReturnsShopMenuViewer() {
        Viewer<Shop> viewer = shopState.getViewer();

        assertNotNull(viewer);
        assertTrue(viewer instanceof ShopMenuViewer);
    }

    @Test
    void testGetControllerReturnsShopController() {
        Controller<Shop> controller = shopState.getController();

        assertNotNull(controller);
        assertTrue(controller instanceof ShopController);
    }

    @Test
    void testGetControllerReturnsSameInstance() {
        Controller<Shop> controller1 = shopState.getController();
        Controller<Shop> controller2 = shopState.getController();

        assertSame(controller1, controller2);
    }

    @Test
    void testGetViewerReturnsSameInstance() {
        Viewer<Shop> viewer1 = shopState.getViewer();
        Viewer<Shop> viewer2 = shopState.getViewer();

        assertSame(viewer1, viewer2);
    }

    @Test
    void testGetViewerUpdatesPlayerGold() {
        // Verify viewer is updated with player gold
        shopState.getViewer();

        verify(mockDungeon, atLeastOnce()).getPlayer();
    }

    @Test
    void testConstructorInitializesViewer() {
        Viewer<Shop> viewer = shopState.getViewer();
        assertNotNull(viewer);
    }

    @Test
    void testConstructorInitializesController() {
        Controller<Shop> controller = shopState.getController();
        assertNotNull(controller);
    }
}
