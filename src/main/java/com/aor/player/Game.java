package com.aor.player;

import com.aor.player.gui.GUI;
import com.aor.player.gui.LanternaGUI;
import com.aor.player.states.State;

import java.io.IOException;

public class Game {
    private final GUI gui;
    private State<?> state;

    public Game() throws IOException {
        this.gui = new LanternaGUI(20, 20);
    }

    public static void main(String[] args) throws IOException {
        new Game().start();
    }

    public void setState(State<?> state) {
        this.state = state;
    }

    private void start() throws IOException {
        int FPS = 10;
        int frameTime = 1000 / FPS;

        while (this.state != null) {
            long startTime = System.currentTimeMillis();

            state.step(this, gui, startTime);

            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = frameTime - elapsedTime;

            try {
                if (sleepTime > 0)
                    Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
        }

        gui.close();
    }
}
