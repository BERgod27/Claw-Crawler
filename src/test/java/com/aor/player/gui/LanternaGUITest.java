package com.aor.player.gui;

import com.aor.player.model.Position;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

class LanternaGUITest {
    private Screen screen;
    private LanternaGUI gui;
    private TextGraphics tg;

    @BeforeEach
    void setUp() {
        screen = Mockito.mock(Screen.class);
        tg = Mockito.mock(TextGraphics.class);

        Mockito.when(screen.newTextGraphics()).thenReturn(tg);

        gui = new LanternaGUI(screen);
    }

    @Test
    void drawHero() {
        gui.drawPlayer(new Position(1, 1));

        Mockito.verify(tg, Mockito.times(1)).setForegroundColor(new TextColor.RGB(255, 215, 0));
        Mockito.verify(tg, Mockito.times(1)).putString(1, 1, "P");
        Mockito.verify(tg, Mockito.times(1)).setBackgroundColor(new TextColor.RGB(26, 26, 46));
    }

    @Test
    void drawText() {
        gui.drawText(new Position(1, 1), "Hello World", "#336699");

        Mockito.verify(tg, Mockito.times(1)).setForegroundColor(new TextColor.RGB(51, 102, 153));
        Mockito.verify(tg, Mockito.times(1)).putString(1, 1, "Hello World");
    }

    @Test
    void drawWall() {
        gui.drawWall(new Position(1, 1));

        Mockito.verify(tg, Mockito.times(1)).setForegroundColor(new TextColor.RGB(139, 139, 139));
        Mockito.verify(tg, Mockito.times(1)).setBackgroundColor(new TextColor.RGB(45, 45, 45));
        Mockito.verify(tg, Mockito.times(1)).putString(1, 1, "█");
    }

    @Test
    void drawMonster() {
        gui.drawMonster(new Position(1, 1));

        Mockito.verify(tg, Mockito.times(1)).setForegroundColor(new TextColor.RGB(255, 68, 68));
        Mockito.verify(tg, Mockito.times(1)).setBackgroundColor(new TextColor.RGB(74, 0, 0));
        Mockito.verify(tg, Mockito.times(1)).putString(1, 1, "M");
    }

    @Test
    void clear() {
        gui.clear();
        Mockito.verify(screen, Mockito.times(1)).clear();
    }

    @Test
    void refresh() throws IOException {
        gui.refresh();
        Mockito.verify(screen, Mockito.times(1)).refresh();
    }

    @Test
    void close() throws IOException {
        gui.close();
        Mockito.verify(screen, Mockito.times(1)).close();
    }
}