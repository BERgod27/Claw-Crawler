package com.aor.player.model.game.dungeon;

import com.aor.player.model.Position;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.elements.Wall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dungeon {
    private final int width;
    private final int height;

    private Player player;

    private List<Monster> monsters;
    private List<Wall> walls;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.monsters = new ArrayList<>();
        this.walls = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Monster> getMonsters() {
        return Collections.unmodifiableList(monsters);
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = new ArrayList<>(monsters);
    }

    public List<Wall> getWalls() {
        return Collections.unmodifiableList(walls);
    }

    public void setWalls(List<Wall> walls) {
        this.walls = new ArrayList<>(walls);
    }

    public void addMonster(Monster monster) {
        this.monsters.add(monster);
    }

    public void removeMonster(Monster monster) {
        this.monsters.remove(monster);
    }

    public boolean isEmpty(Position position) {
        for (Wall wall : walls)
            if (wall.getPosition().equals(position))
                return false;
        return true;
    }

    public boolean isMonster(Position position) {
        for (Monster monster : monsters)
            if (monster.getPosition().equals(position))
                return true;
        return false;
    }

    public void movePlayer(Position newPosition) {
        if (isEmpty(newPosition)) {
            player.setPosition(newPosition);
        }
    }
}
