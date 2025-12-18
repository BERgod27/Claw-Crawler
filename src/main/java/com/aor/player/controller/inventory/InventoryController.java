package com.aor.player.controller.inventory;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.items.Equippable;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.items.Usable;
import com.aor.player.states.GameState;
import com.aor.player.states.StateFactory;

import java.io.IOException;

public class InventoryController extends Controller<Dungeon> {
    private final StateFactory stateFactory;
    private int selectedIndex = 0;

    public InventoryController(Dungeon dungeon, StateFactory stateFactory) {
        super(dungeon);
        this.stateFactory = stateFactory;
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        switch (action) {
            case UP:
                selectedIndex = Math.max(0, selectedIndex - 1);
                break;
            case DOWN:
                int maxIndex = getModel().getPlayer().getInventory().size() - 1;
                selectedIndex = Math.min(maxIndex, selectedIndex + 1);
                break;
            case SELECT:
                useSelectedItem();
                break;
            case QUIT:
            case INVENTORY:
                // Close inventory and return to game using factory
                game.setState(stateFactory.createGameState(getModel()));
                break;
            case LEFT:
            case RIGHT:
            case NONE:
                // No action for these keys in inventory
                break;
        }
    }

    private void useSelectedItem() {
        if (selectedIndex >= 0 && selectedIndex < getModel().getPlayer().getInventory().size()) {
            Item item = getModel().getPlayer().getInventory().get(selectedIndex);

            if (item instanceof Usable) {
                // Use consumable item
                ((Usable) item).use(getModel().getPlayer());
                // Remove from inventory after use
                getModel().getPlayer().removeItem(selectedIndex);
                // Adjust selection if needed - reset to 0 or stay at last valid index
                int inventorySize = getModel().getPlayer().getInventory().size();
                if (inventorySize == 0) {
                    selectedIndex = 0;
                } else if (selectedIndex >= inventorySize) {
                    selectedIndex = inventorySize - 1;
                }
            } else if (item instanceof Equippable) {
                // Equip item
                ((Equippable) item).equip(getModel().getPlayer());
            }
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
