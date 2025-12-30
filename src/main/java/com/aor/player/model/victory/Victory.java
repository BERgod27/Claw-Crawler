package com.aor.player.model.victory;

public class Victory {
    private final long timePlayed;
    private final int totalGold;
    private final int slimesKilled;

    public Victory(long timePlayed, int totalGold, int slimesKilled) {
        this.timePlayed = timePlayed;
        this.totalGold = totalGold;
        this.slimesKilled = slimesKilled;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public int getTotalGold() {
        return totalGold;
    }

    public int getSlimesKilled() {
        return slimesKilled;
    }

    public String getFormattedTime() {
        long seconds = timePlayed / 1000;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}
