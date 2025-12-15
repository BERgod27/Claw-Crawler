package com.aor.player.controller.shop;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.items.Equippable;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.items.Usable;
import com.aor.player.model.game.shop.Shop;
import com.aor.player.states.GameState;
import com.aor.player.states.StateFactory;

import java.io.IOException;

public class ShopController extends Controller<Shop> {
    private final Dungeon dungeon;
    private final StateFactory stateFactory;
    private int selectedIndex = 0;

    public ShopController(Dungeon dungeon, StateFactory stateFactory) {
        super(new Shop());
        this.dungeon = dungeon;
        this.stateFactory = stateFactory;
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        switch (action) {
            case UP:
                selectedIndex = Math.max(0, selectedIndex - 1);
                break;
            case DOWN:
                selectedIndex = Math.min(getModel().getAvailableItems().size() - 1, selectedIndex + 1);
                break;
            case SELECT:
                purchaseSelectedItem(game);
                break;
            case QUIT:
                // Return to game using factory
                game.setState(stateFactory.createGameState(dungeon));
                break;
            case LEFT:
            case RIGHT:
            case NONE:
            case INVENTORY:
                // No action for these keys in shop
                break;
        }
    }

    private void purchaseSelectedItem(Game game) {
        if (selectedIndex >= 0 && selectedIndex < getModel().getAvailableItems().size()) {
            Item item = getModel().getAvailableItems().get(selectedIndex);
            boolean success = getModel().purchase(item, dungeon.getPlayer());

            if (success) {
                // Auto-use potions, auto-equip equipment
                if (item instanceof Usable) {
                    ((Usable) item).use(dungeon.getPlayer());
                }
            }
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
