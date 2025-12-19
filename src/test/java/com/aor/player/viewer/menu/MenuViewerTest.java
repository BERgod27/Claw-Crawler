package com.aor.player.viewer.menu;

import com.aor.player.gui.GUI;
import com.aor.player.model.menu.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuViewerTest {

    private Menu mockMenu;
    private GUI mockGUI;
    private MenuViewer menuViewer;

    @BeforeEach
    void setUp() {
        mockMenu = mock(Menu.class);
        mockGUI = mock(GUI.class);

        when(mockMenu.getNumberEntries()).thenReturn(3);
        when(mockMenu.getEntry(0)).thenReturn("Start Game");
        when(mockMenu.getEntry(1)).thenReturn("Instructions");
        when(mockMenu.getEntry(2)).thenReturn("Exit");
        when(mockMenu.isSelected(0)).thenReturn(true);
        when(mockMenu.isSelected(1)).thenReturn(false);
        when(mockMenu.isSelected(2)).thenReturn(false);

        menuViewer = new MenuViewer(mockMenu);
    }

    @Test
    void testConstructorStoresModel() {
        assertEquals(mockMenu, menuViewer.getModel());
    }

    @Test
    void testDrawCallsClearAndRefresh() throws IOException {
        menuViewer.draw(mockGUI);

        verify(mockGUI).clear();
        verify(mockGUI).refresh();
    }

    @Test
    void testDrawDisplaysMenuTitle() {
        menuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(any(), eq("Menu"), any());
    }

    @Test
    void testDrawDisplaysAllMenuEntries() {
        menuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(any(), eq("Start Game"), any());
        verify(mockGUI).drawText(any(), eq("Instructions"), any());
        verify(mockGUI).drawText(any(), eq("Exit"), any());
    }

    @Test
    void testDrawHighlightsSelectedEntry() {
        menuViewer.drawElements(mockGUI);

        // Selected entry should have different color (#FFD700)
        verify(mockGUI).drawText(any(), eq("Start Game"), eq("#FFD700"));
    }

    @Test
    void testDrawNonSelectedEntriesNormalColor() {
        menuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(any(), eq("Instructions"), eq("#FFFFFF"));
        verify(mockGUI).drawText(any(), eq("Exit"), eq("#FFFFFF"));
    }

    @Test
    void testDrawWithDifferentSelectedIndex() {
        when(mockMenu.isSelected(0)).thenReturn(false);
        when(mockMenu.isSelected(1)).thenReturn(true);

        menuViewer.drawElements(mockGUI);

        verify(mockGUI).drawText(any(), eq("Instructions"), eq("#FFD700"));
    }
}
