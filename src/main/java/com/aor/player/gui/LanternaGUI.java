package com.aor.player.gui;

import com.aor.player.model.Position;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class LanternaGUI implements GUI {
    private final Screen screen;

    public LanternaGUI(int width, int height) throws IOException {
        Terminal terminal = createTerminal(width, height);
        this.screen = createScreen(terminal);
    }

    public LanternaGUI(Screen screen) {
        this.screen = screen;
    }

    private Screen createScreen(Terminal terminal) throws IOException {
        final Screen screen;
        screen = new TerminalScreen(terminal);

        screen.setCursorPosition(null);
        screen.startScreen();
        screen.doResizeIfNecessary();
        return screen;
    }

    private Terminal createTerminal(int width, int height) throws IOException {
        TerminalSize terminalSize = new TerminalSize(width, height);
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setInitialTerminalSize(terminalSize);
        Terminal terminal = terminalFactory.createTerminal();
        return terminal;
    }

    @Override
    public ACTION getNextAction() throws IOException {

        KeyStroke keyStroke = screen.pollInput();
        if (keyStroke == null)
            return ACTION.NONE;

        if (keyStroke.getKeyType() == KeyType.EOF)
            return ACTION.QUIT;
        if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'q')
            return ACTION.QUIT;

        if (keyStroke.getKeyType() == KeyType.ArrowUp)
            return ACTION.UP;
        if (keyStroke.getKeyType() == KeyType.ArrowRight)
            return ACTION.RIGHT;
        if (keyStroke.getKeyType() == KeyType.ArrowDown)
            return ACTION.DOWN;
        if (keyStroke.getKeyType() == KeyType.ArrowLeft)
            return ACTION.LEFT;

        if (keyStroke.getKeyType() == KeyType.Enter)
            return ACTION.SELECT;

        return ACTION.NONE;
    }

    @Override
    public void drawPlayer(Position position) {

    }

    @Override
    public void drawWall(Position position) {

    }

    @Override
    public void drawMonster(Position position) {

    }

    @Override
    public void drawText(Position position, String text, String color) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void refresh() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
