package src.Player;


import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.Board;
import src.Common.Rules;
import src.Common.Tiles;

import java.util.List;

/**
 * We did not have to change the PlayerInterface to implement this class.
 * We only changed the PlayerInterface for the Administrator.
 * We removed age as an intrinsic value for players, and
 * added a name attribute. Neither of these effect this class.
 */
public class SecondS extends FirstS {

    /**
     * Search for the first legal position starting at 0,0 exclusive and continuing counter-
     * clockwise around the border of the board. Then search for the first valid port starting
     * at A inclusive and continuing clockwise around the tile.
     */
    @Override
    public InitialAction getInitialTurn(AvatarColor avatar, Board board, List<Tiles> hand, Rules rules) {
        return super.getInitialTurn(avatar, board, hand, rules);
    }

    /**
     * Search for the first legal intermediate turn starting with the first given tile with
     * zero degrees rotation and continuing clockwise rotation and increasing hand index. If
     * no legal moves are found, return the first tile with zero rotation.
     */
    @Override
    public TurnAction getTurn(AvatarColor avatar, Board board, List<Tiles> hand, Rules rules) {
        List<TurnAction> possibleActions = StrategyUtils.getAllTurns(avatar, hand, 0);
        for (TurnAction possibleAction : possibleActions) {
            if (rules.isValidTurn(board, hand, possibleAction)) {
                return possibleAction;
            }
        }
        return possibleActions.get(0);
    }
}