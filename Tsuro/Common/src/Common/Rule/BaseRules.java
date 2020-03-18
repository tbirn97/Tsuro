package src.Common.Rule;

import src.Common.Rules;

import java.util.Arrays;

/**
 * This rule set defines all rules that are always enforced including no exceptions being thrown
 * by the board and that the player is not playing a tile that wasn't given.
 */
public class BaseRules extends Rules {

    public BaseRules() {
        super(Arrays.asList(new NoBoardExceptionsRule(), new TileWasGiven()),
                Arrays.asList(new NoBoardExceptionsRule(), new TileWasGiven()));
    }
}
