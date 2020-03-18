package src.Common;

import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.Rule.InitialRule;
import src.Common.Rule.TurnRule;

import java.util.ArrayList;
import java.util.List;

/**
 * src.Admin.Rule checker object. Contains InitialRules and TurnRules
 * and verifies given player src.Admin.Actions against these rules.
 */
public class Rules {
    private static final String nullMessage = "Given rule was null";
    private ArrayList<InitialRule> initialPlacementRules;
    private ArrayList<TurnRule> turnRules;

    /**
     * Shorthand empty Rules constructor.
     */
    public Rules() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor for Rules object.
     * @param initialPlacementRules empty list | list of InitialRule
     * @param turnRules empty list | list of TurnRule
     */
    public Rules(List<InitialRule> initialPlacementRules, List<TurnRule> turnRules) {
        this.initialPlacementRules = new ArrayList<>(initialPlacementRules);
        this.turnRules = new ArrayList<>(turnRules);
    }

    /**
     * Getter for the InitialRule list
     * @return list of the current initial rules
     */
    public List<InitialRule> getInitialRules() {
        return new ArrayList<>(initialPlacementRules);
    }

    /**
     * Getter for the TurnRule list
     * @return list of the current turn rules
     */
    public List<TurnRule> getTurnRules() {
        return new ArrayList<>(turnRules);
    }

    /**
     * Adds a given InitialRule to the rules to check.
     * @param rule InitialRule
     */
    public void addInitialRule(InitialRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException(nullMessage);
        }
        this.initialPlacementRules.add(rule);
    }

    /**
     * Adds a given TurnRule to the rules to check.
     * @param rule TurnRule
     */
    public void addTurnRule(TurnRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException(nullMessage);
        }
        this.turnRules.add(rule);
    }

    /**
     * Checks all the initial placement rules. If any return false for isValidMove, return false.
     * @param board Board on which the move will be executed
     * @param hand player hand of tiles
     * @param playerMove InitialAction object with the player's move information
     * @return false if any isValidMove call is false. true otherwise
     */
    public boolean isValidInitialMove(Board board, List<Tiles> hand, InitialAction playerMove) {
        for (InitialRule r : this.initialPlacementRules) {
            if (!r.isValidMove(board, hand, playerMove)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks all the general turn rules. If any return false for isValidMove, return false.
     * @param board Board on which the move will be executed
     * @param hand player hand of tiles
     * @param playerMove Action object with the player's move information
     * @return false if any isValidMove call is false. true otherwise
     */
    public boolean isValidTurn(Board board, List<Tiles> hand, TurnAction playerMove) {
        for (TurnRule r : this.turnRules) {
            if (!r.isValidMove(board, hand, playerMove)) {
                return false;
            }
        }
        return true;
    }
}