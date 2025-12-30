package com.aor.player.states;

import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.victory.Victory;
import com.aor.player.model.menu.Menu;
import com.aor.player.model.menu.Instructions;

public class StateFactory {

    public ShopState createShopState(Dungeon dungeon) {
        return new ShopState(dungeon.getShop(), dungeon, this);
    }

    public InventoryState createInventoryState(Dungeon dungeon) {
        return new InventoryState(dungeon, this);
    }

    public GameState createGameState(Dungeon dungeon) {
        return new GameState(dungeon, this);
    }

    public CombatState createCombatState(Dungeon dungeon, Monster monster) {
        return new CombatState(dungeon, monster, this);
    }

    public MenuState createMenuState() {
        return new MenuState(new Menu(), this);
    }

    public InstructionsState createInstructionsState() {
        return new InstructionsState(new Instructions(), this);
    }

    public VictoryState createVictoryState(Dungeon dungeon) {
        Victory victory = new Victory(
                dungeon.getTimePlayed(),
                dungeon.getTotalGold(),
                dungeon.getSlimesKilled());
        return new VictoryState(victory, this);
    }
}
