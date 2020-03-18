package src.Player;

import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.Board;
import src.Common.Rules;
import src.Common.Tiles;

import java.util.List;

public interface Strategy {

    // TODO: determine if the strategy needs information on the previous move (presumably the player passes this in)
    /**
     * Gets an InitialAction based on the board state, hand, and optionally checking if this move has already been attempted.
     * @param board
     * @param hand
     * @return
     */
    InitialAction getInitialTurn(AvatarColor avatar, Board board, List<Tiles> hand, Rules rules);

    /**
     *
     * @param board
     * @param hand
     * @return
     */
    TurnAction getTurn(AvatarColor avatar, Board board, List<Tiles> hand, Rules rules);
}
