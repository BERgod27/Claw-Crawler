package com.aor.player.states;

import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.shop.Shop;

public class StateFactory {

    public ShopState createShopState(Dungeon dungeon) {
        Shop shop = new Shop();
        return new ShopState(shop, dungeon, this);
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
        return new MenuState(new com.aor.player.model.menu.Menu(), this);
    }

    public InstructionsState createInstructionsState() {
        return new InstructionsState(new com.aor.player.model.menu.Instructions(), this);
    }
}
