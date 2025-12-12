package com.aor.player.gui;

import com.aor.player.model.Position;
import com.aor.player.model.Sprite;

import java.io.IOException;

public interface GUI {
    ACTION getNextAction() throws IOException;

    void drawPlayer(Position position);

    void drawWall(Position position);

    void drawMonster(Position position);

    void drawText(Position position, String text, String color);

    void drawSprite(Position position, Sprite sprite);

    void drawDoor(Position position);

    void drawShopKeeper(Position position);

    void drawText(int x, int y, String text, String color);

    void clear();

    void refresh() throws IOException;

    void close() throws IOException;

    enum ACTION {
        UP, RIGHT, DOWN, LEFT, NONE, QUIT, SELECT, INVENTORY
    }
}
