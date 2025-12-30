package com.aor.player.viewer.game;

import com.aor.player.gui.GUI;
import com.aor.player.model.game.elements.Door;
import com.aor.player.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DoorViewerTest {

    private Door mockDoor;
    private GUI mockGUI;
    private DoorViewer doorViewer;

    @BeforeEach
    void setUp() {
        mockDoor = mock(Door.class);
        mockGUI = mock(GUI.class);

        when(mockDoor.getPosition()).thenReturn(new Position(3, 7));

        doorViewer = new DoorViewer();
    }

    @Test
    void testDrawDoor() {
        doorViewer.draw(mockDoor, mockGUI);

        verify(mockGUI).drawDoor(any());
    }

    @Test
    void testDrawDoorAtDifferentPosition() {
        when(mockDoor.getPosition()).thenReturn(new Position(8, 12));

        doorViewer.draw(mockDoor, mockGUI);

        verify(mockGUI).drawDoor(any());
    }

    @Test
    void testDrawMultipleDoors() {
        Door door2 = mock(Door.class);
        when(door2.getPosition()).thenReturn(new Position(8, 12));

        doorViewer.draw(mockDoor, mockGUI);
        doorViewer.draw(door2, mockGUI);

        verify(mockGUI, times(2)).drawDoor(any());
    }
}
