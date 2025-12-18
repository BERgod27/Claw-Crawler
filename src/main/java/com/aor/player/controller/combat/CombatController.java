package com.aor.player.controller.combat;

import com.aor.player.Game;
import com.aor.player.controller.Controller;
import com.aor.player.gui.GUI;
import com.aor.player.model.game.dungeon.Dungeon;
import com.aor.player.model.game.elements.Monster;
import com.aor.player.model.game.elements.Player;
import com.aor.player.model.game.items.Item;
import com.aor.player.model.game.items.Usable;
import com.aor.player.states.StateFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for turn-based combat encounters.
 * Handles player actions (Attack, Defend, Use Item, Flee) and monster AI.
 * Manages combat flow and state transitions.
 */
public class CombatController extends Controller<Dungeon> {
    private final Monster monster;
    private final StateFactory stateFactory;
    private int selectedAction = 0;
    private boolean isDefending = false;
    private boolean combatOver = false;
    private boolean playerDefeated = false;
    private boolean allBossesDefeated = false;
    private final List<String> combatLog;
    private static final int MAX_LOG_ENTRIES = 5;
    private static final int LAST_ACTION_INDEX = 3;

    /**
     * Creates a combat controller\
     * 
     * @param dungeon      The game dungeon
     * @param monster      The monster being fought
     * @param stateFactory Factory for creating states
     */
    public CombatController(Dungeon dungeon, Monster monster, StateFactory stateFactory) {
        super(dungeon);
        this.monster = monster;
        this.stateFactory = stateFactory;
        this.combatLog = new ArrayList<>();
        addLogEntry("Combat started with " + monster.getName() + "!");
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        // If combat is over, wait for Q to close
        if (combatOver) {
            if (action == GUI.ACTION.QUIT) {
                if (allBossesDefeated) {
                    // All bosses defeated - show victory screen
                    game.setState(stateFactory.createVictoryState(getModel()));
                } else if (playerDefeated) {
                    // Player defeated - return to menu
                    game.setState(stateFactory.createMenuState());
                } else {
                    // Victory - continue game
                    game.setState(stateFactory.createGameState(getModel()));
                }
            }
            return;
        }

        // Navigate menu
        if (action == GUI.ACTION.UP) {
            selectedAction = Math.max(0, selectedAction - 1);
        }
        if (action == GUI.ACTION.DOWN) {
            selectedAction = Math.min(LAST_ACTION_INDEX, selectedAction + 1);
        }

        // Execute selected action
        if (action == GUI.ACTION.SELECT) {
            executeAction(game);
        }

        // Quit game
        if (action == GUI.ACTION.QUIT) {
            game.setState(null);
        }
    }

    /**
     * Executes the currently selected combat action
     * 
     * @param game The game instance for state transitions
     */
    private void executeAction(Game game) {
        Player player = getModel().getPlayer();

        switch (selectedAction) {
            case 0: // Attack
                performAttack(player);
                break;
            case 1: // Defend
                isDefending = true;
                addLogEntry("You brace for impact!");
                break;
            case 2: // Use Item
                useItem(player);
                break;
            case 3: // Flee
                flee(game);
                return;
        }

        // Check if monster is dead
        if (monster.isDead()) {
            victory(game);
            return;
        }

        // Monster's turn
        monsterTurn(player);

        // Check if player is dead
        if (player.isDead()) {
            defeat(game);
        }
    }

    private void performAttack(Player player) {
        int damage = Math.max(1, player.getAttack() - monster.getDefense());
        monster.takeDamage(damage);
        addLogEntry("You hit " + monster.getName() + " for " + damage + " damage!");
    }

    private void monsterTurn(Player player) {
        // Calculate damage after armor reduction
        int damage = Math.max(1, monster.getAttack() - player.getArmor());

        if (isDefending) {
            damage = Math.max(1, damage / 2);
            isDefending = false;
            addLogEntry(monster.getName() + " attacks! You block some damage!");
        }

        // Directly reduce health (armor already calculated)
        player.reduceHealth(damage);
        addLogEntry(monster.getName() + " deals " + damage + " damage!");
    }

    private void useItem(Player player) {
        for (Item item : player.getInventory()) {
            if (item instanceof Usable) {
                Usable usable = (Usable) item;
                usable.use(player);
                addLogEntry("You used " + item.getName() + "!");
                return;
            }
        }
        addLogEntry("No usable items in inventory!");
    }

    private void flee(Game game) {
        addLogEntry("You fled from combat!");
        game.setState(stateFactory.createGameState(getModel()));
    }

    private void victory(Game game) {
        int goldReward = monster.getType().getGoldReward();
        getModel().getPlayer().addGold(goldReward);

        addLogEntry("Victory! " + monster.getName() + " defeated!");
        addLogEntry("You received " + goldReward + " gold!");

        getModel().removeMonster(monster);

        // Check if all bosses are defeated
        if (getModel().areAllBossesDefeated()) {
            addLogEntry("All bosses defeated! You win!");
            addLogEntry("Press Q to continue...");
            combatOver = true;
            allBossesDefeated = true;
        } else {
            addLogEntry("Press Q to continue...");
            combatOver = true;
        }
    }

    private void defeat(Game game) {
        addLogEntry("You have been defeated...");
        addLogEntry("Press Q to return to menu...");
        combatOver = true;
        playerDefeated = true;
    }

    private void addLogEntry(String message) {
        combatLog.add(message);
        if (combatLog.size() > MAX_LOG_ENTRIES) {
            combatLog.remove(0);
        }
    }

    public int getSelectedAction() {
        return selectedAction;
    }

    public List<String> getCombatLog() {
        return new ArrayList<>(combatLog);
    }

    public Monster getMonster() {
        return monster;
    }
}
