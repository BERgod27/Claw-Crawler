package com.aor.player.gui;

import com.aor.player.model.Position;
import com.aor.player.model.Sprite;
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

import java.awt.*;
import java.io.IOException;

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
        Screen screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);
        screen.startScreen();
        screen.doResizeIfNecessary();
        return screen;
    }

    private Terminal createTerminal(int width, int height) throws IOException {
        TerminalSize terminalSize = new TerminalSize(width, height);
        AWTTerminalFontConfiguration fontConfig = AWTTerminalFontConfiguration.newInstance(
                new Font("Monospaced", Font.PLAIN, 12));
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setInitialTerminalSize(terminalSize)
                .setTerminalEmulatorTitle("Dungeon Crawler")
                .setTerminalEmulatorFontConfiguration(fontConfig)
                .setForceTextTerminal(false);
        return terminalFactory.createTerminal();
    }

    @Override
    public ACTION getNextAction() throws IOException {
        KeyStroke keyStroke = screen.pollInput();
        if (keyStroke == null)
            return ACTION.NONE;

        if (keyStroke.getKeyType() == KeyType.EOF ||
                (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'q'))
            return ACTION.QUIT;

        // Handle inventory key
        if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'i')
            return ACTION.INVENTORY;

        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                return ACTION.UP;
            case ArrowRight:
                return ACTION.RIGHT;
            case ArrowDown:
                return ACTION.DOWN;
            case ArrowLeft:
                return ACTION.LEFT;
            case Enter:
                return ACTION.SELECT;
            default:
                return ACTION.NONE;
        }
    }

    @Override
    public void drawPlayer(Position position) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFD700"));
        graphics.putString(position.getX(), position.getY(), "P");
    }

    @Override
    public void drawWall(Position position) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString("#8B8B8B"));
        graphics.setBackgroundColor(TextColor.Factory.fromString("#2d2d2d"));
        graphics.putString(position.getX(), position.getY(), "█");
    }

    @Override
    public void drawMonster(Position position) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString("#FF4444"));
        graphics.setBackgroundColor(TextColor.Factory.fromString("#4a0000"));
        graphics.putString(position.getX(), position.getY(), "M");
    }

    @Override
    public void drawDoor(Position position) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString("#00FF00"));
        graphics.setBackgroundColor(TextColor.Factory.fromString("#003300"));
        graphics.putString(position.getX(), position.getY(), "▓");
    }

    @Override
    public void drawShopKeeper(Position position) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFD700"));
        graphics.setBackgroundColor(TextColor.Factory.fromString("#4a3800"));
        graphics.putString(position.getX(), position.getY(), "S");
    }

    @Override
    public void drawText(Position position, String text, String color) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString(color));
        graphics.putString(position.getX(), position.getY(), text);
    }

    @Override
    public void drawText(int x, int y, String text, String color) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString(color));
        graphics.putString(x, y, text);
    }

    @Override
    public void drawSprite(Position position, Sprite sprite) {
        TextGraphics graphics = screen.newTextGraphics();
        for (int y = 0; y < sprite.getHeight(); y++) {
            for (int x = 0; x < sprite.getWidth(); x++) {
                Color color = sprite.getPixel(x, y);
                if (color != null && color.getAlpha() > 128) {
                    String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                    graphics.setForegroundColor(TextColor.Factory.fromString(hex));
                    graphics.putString(position.getX() + x, position.getY() + y, "█");
                }
            }
        }
    }

    @Override
    public void drawChest(Position position, Sprite sprite, boolean opened) {
        TextGraphics graphics = screen.newTextGraphics();
        for (int y = 0; y < sprite.getHeight(); y++) {
            for (int x = 0; x < sprite.getWidth(); x++) {
                Color color = sprite.getPixel(x, y);
                if (color != null && color.getAlpha() > 128) {
                    if (opened) {
                        color = new Color(color.getRed() / 2, color.getGreen() / 2, color.getBlue() / 2);
                    }
                    String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                    graphics.setForegroundColor(TextColor.Factory.fromString(hex));
                    graphics.putString(position.getX() + x, position.getY() + y, "█");
                }
            }
        }
    }

    @Override
    public void clear() {
        screen.clear();
    }

    @Override
    public void refresh() throws IOException {
        screen.refresh();
    }

    @Override
    public void close() throws IOException {
        screen.close();
    }
}