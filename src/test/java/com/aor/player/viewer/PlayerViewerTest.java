package com.aor.player.viewer;

import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Player;
import com.aor.player.viewer.game.PlayerViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PlayerViewerTest {
    private Player player;
    private PlayerViewer viewer;
    private GUI gui;

    @BeforeEach
    void setUp() {
        player = new Player(10, 10);
        viewer = new PlayerViewer();
        gui = Mockito.mock(GUI.class);
    }

    @Test
    void drawElement() {
        viewer.draw(player, gui);
        // Verify that drawSprite is called (sprite loading happens in static block)
        Mockito.verify(gui, Mockito.times(1)).drawSprite(
                Mockito.eq(new Position(10, 10)),
                Mockito.any());
    }
}
