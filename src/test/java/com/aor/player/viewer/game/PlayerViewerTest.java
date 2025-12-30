package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class PlayerViewerTest {
    private PlayerViewer viewer;
    private GUI mockGUI;

    @BeforeEach
    void setUp() {
        viewer = new PlayerViewer();
        mockGUI = mock(GUI.class);
    }

    @Test
    void testDrawCallsGui() {
        Player player = mock(Player.class);
        when(player.getPosition()).thenReturn(new Position(5, 5));

        viewer.draw(player, mockGUI);

        // Verify either sprite or fallback drawing is called
        verify(mockGUI, atMostOnce()).drawSprite(eq(new Position(5, 5)), any());
        verify(mockGUI, atMostOnce()).drawPlayer(eq(new Position(5, 5)));
    }
}
