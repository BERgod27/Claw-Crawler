package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.elements.ShopKeeper;
import com.aor.player.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ShopKeeperViewerTest {

    private ShopKeeper mockShopKeeper;
    private GUI mockGUI;
    private ShopKeeperViewer shopKeeperViewer;

    @BeforeEach
    void setUp() {
        mockShopKeeper = mock(ShopKeeper.class);
        mockGUI = mock(GUI.class);

        when(mockShopKeeper.getPosition()).thenReturn(new Position(7, 9));

        shopKeeperViewer = new ShopKeeperViewer();
    }

    @Test
    void testDrawShopKeeper() {
        shopKeeperViewer.draw(mockShopKeeper, mockGUI);

        verify(mockGUI).drawSprite(any(), any());
    }

    @Test
    void testDrawShopKeeperAtDifferentPosition() {
        when(mockShopKeeper.getPosition()).thenReturn(new Position(12, 15));

        shopKeeperViewer.draw(mockShopKeeper, mockGUI);

        verify(mockGUI).drawSprite(any(), any());
    }
}
