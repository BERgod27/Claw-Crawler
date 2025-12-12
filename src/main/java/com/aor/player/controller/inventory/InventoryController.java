package com.aor.player.controller.inventory;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.items.Equippable;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.items.Usable;
import com.aor.player.states.GameState;

import java.io.IOException;

public class InventoryController extends Controller<Dungeon> {
    private int selectedIndex = 0;

    public InventoryController(Dungeon dungeon) {
        super(dungeon);
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
                // Close inventory and return to game
                game.setState(new GameState(getModel()));
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
                getModel().getPlayer().getInventory().remove(selectedIndex);
                // Adjust selection if needed
                if (selectedIndex >= getModel().getPlayer().getInventory().size() && selectedIndex > 0) {
                    selectedIndex--;
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
