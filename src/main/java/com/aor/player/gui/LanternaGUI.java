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
        final Screen screen;
        screen = new TerminalScreen(terminal);

        screen.setCursorPosition(null);
        screen.startScreen();
        screen.doResizeIfNecessary();
        return screen;
    }

    private Terminal createTerminal(int width, int height) throws IOException {
        TerminalSize terminalSize = new TerminalSize(width, height);

        // Configure font for proper window sizing
        AWTTerminalFontConfiguration fontConfig = AWTTerminalFontConfiguration.newInstance(
                new Font("Monospaced", Font.PLAIN, 12));

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setInitialTerminalSize(terminalSize)
                .setTerminalEmulatorTitle("Dungeon Crawler")
                .setTerminalEmulatorFontConfiguration(fontConfig)
                .setForceTextTerminal(false);

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

        if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'i')
            return ACTION.INVENTORY;

        return ACTION.NONE;
    }

    @Override
    public void drawPlayer(Position position) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFD700"));
        graphics.putString(position.getX(), position.getY(), "P");
        graphics.setBackgroundColor(TextColor.Factory.fromString("#1a1a2e"));
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
    public void drawText(Position position, String text, String color) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString(color));
        graphics.putString(position.getX(), position.getY(), text);
    }

    @Override
    public void drawSprite(Position position, com.aor.player.model.Sprite sprite) {
        TextGraphics graphics = screen.newTextGraphics();

        for (int y = 0; y < sprite.getHeight(); y++) {
            for (int x = 0; x < sprite.getWidth(); x++) {
                Color color = sprite.getPixel(x, y);

                // Skip transparent pixels
                if (color != null && color.getAlpha() > 128) {
                    // Convert AWT Color to Lanterna TextColor
                    String hexColor = String.format("#%02x%02x%02x",
                            color.getRed(), color.getGreen(), color.getBlue());
                    graphics.setForegroundColor(TextColor.Factory.fromString(hexColor));
                    graphics.putString(position.getX() + x, position.getY() + y, "█");
                }
            }
        }
    }

    @Override
    public void drawDoor(Position position) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString("#00FF00")); // Green for doors
        graphics.setBackgroundColor(TextColor.Factory.fromString("#003300"));
        graphics.putString(position.getX(), position.getY(), "▓");
    }

    @Override
    public void drawShopKeeper(Position position) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFD700")); // Gold for shopkeeper
        graphics.setBackgroundColor(TextColor.Factory.fromString("#4a3800"));
        graphics.putString(position.getX(), position.getY(), "S");
    }

    @Override
    public void drawText(int x, int y, String text, String color) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString(color));
        graphics.putString(x, y, text);
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
    }}

    
    
            
            
    