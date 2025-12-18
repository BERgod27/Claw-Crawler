package com.aor.player.model.menu;

public class Instructions {
    private final String title;
    private final String[] instructionLines;

    public Instructions() {
        this.title = "Instructions";
        this.instructionLines = new String[] {
                "Movement Controls:",
                "",
                "  Arrow Up    - Move Up",
                "  Arrow Down  - Move Down",
                "  Arrow Left  - Move Left",
                "  Arrow Right - Move Right",
                "",
                "Other Controls:",
                "",
                "  Q - Quit Game",
                "  Enter - Select Menu Option",
                "",
                "Press Q to return to menu..."
        };
    }

    public String getTitle() {
        return title;
    }

    public String[] getInstructionLines() {
        return instructionLines;
    }

    public int getNumberOfLines() {
        return instructionLines.length;
    }
}
