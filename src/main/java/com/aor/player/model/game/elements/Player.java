package com.aor.player.model.game.elements;

public class Player extends Element {
    private int Speed;
    private int Armor;
    private int Health;


    public Player(int x, int y){
        super(x,y);
        this.Speed = 10;
        this.Armor = 10;
        this.Health = 10;
    }

    public int getArmor() {
        return Armor;
    }

    public int getHealth() {
        return Health;
    }

    public int getSpeed() {
        return Speed;
    }

}
