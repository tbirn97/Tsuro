package src.Player;

import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.AvatarLocation;
import src.Common.Board;
import src.Common.Port;
import src.Common.Posn;
import src.Common.Rotation;
import src.Common.Rules;
import src.Common.Tiles;

import java.util.List;

public class FirstS implements Strategy {

    /**
     * Search for the first legal position starting at 0,0 exclusive and continuing counter-
     * clockwise around the border of the board. Then search for the first valid port starting
     * at A inclusive and continuing clockwise around the tile.
     */
    @Override
    public InitialAction getInitialTurn(AvatarColor avatar, Board board, List<Tiles> hand, Rules rules) {
        List<Posn> possiblePositions = StrategyUtils.getNonAdjacentBorderPosns(board, true);
        Posn position = possiblePositions.get(0);

        List<Port> possiblePorts = StrategyUtils.getValidPorts(board, position, true);
        Port port = possiblePorts.get(0);

        return new InitialAction(
                avatar,
                hand.get(2),
                Rotation.R0,
                new AvatarLocation(
                        position,
                        port));
    }

    /**
     * Return the first tile given to the player with no rotation. Does not check the legality
     * of the move.
     */
    @Override
    public TurnAction getTurn(AvatarColor avatar, Board board, List<Tiles> hand, Rules rules) {
        return new TurnAction(avatar, hand.get(0), Rotation.R0);
    }


}