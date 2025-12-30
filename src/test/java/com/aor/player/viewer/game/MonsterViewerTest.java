package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.MonsterType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class MonsterViewerTest {
    private MonsterViewer viewer;
    private GUI mockGUI;

    @BeforeEach
    void setUp() {
        viewer = new MonsterViewer();
        mockGUI = mock(GUI.class);
    }

    @Test
    void testDrawCallsGui() {
        Monster monster = mock(Monster.class);
        when(monster.getPosition()).thenReturn(new Position(10, 10));
        when(monster.getType()).thenReturn(MonsterType.SLIME);

        viewer.draw(monster, mockGUI);

        verify(mockGUI, atMostOnce()).drawSprite(eq(new Position(10, 10)), any());
        verify(mockGUI, atMostOnce()).drawMonster(eq(new Position(10, 10)));
    }
}
