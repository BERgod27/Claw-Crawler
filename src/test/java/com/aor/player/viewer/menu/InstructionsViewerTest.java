
package com.aor.player.viewer.menu;

import com.aor.player.gui.GUI;
import com.aor.player.model.Position;
import com.aor.player.model.menu.Instructions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class InstructionsViewerTest {
    private Instructions instructions;
    private InstructionsViewer viewer;
    private GUI gui;

    @BeforeEach
    void setUp() {
        instructions = mock(Instructions.class);
        viewer = new InstructionsViewer(instructions);
        gui = mock(GUI.class);
    }

    @Test
    void testDrawElements() {
        // Mock behavior
        when(instructions.getTitle()).thenReturn("INSTRUCTIONS");
        when(instructions.getInstructionLines()).thenReturn(new String[] {
                "Line 1",
                "Key: Value",
                ""
        });

        viewer.drawElements(gui);

        // Verify title drawn
        verify(gui).drawText(new Position(5, 3), "INSTRUCTIONS", "#FFD700");

        // Verify lines drawn
        verify(gui).drawText(new Position(5, 5), "Line 1", "#FFFFFF"); // Regular line
        verify(gui).drawText(new Position(5, 6), "Key: Value", "#00FFFF"); // Key-value highlighted
        verify(gui).drawText(new Position(5, 7), "", "#FFFFFF"); // Empty line
    }
}
