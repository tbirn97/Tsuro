package src.Common.Rule;

import src.Common.Actions.InitialAction;
import src.Common.AvatarLocation;
import src.Common.Board;
import src.Common.BoardMutator;
import src.Common.Connection;
import src.Common.Port;
import src.Common.Tiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Function object to enforce the initial rule that no tile can be placed adjacent
 * to any tiles already on the board.
 */
public class InitialRuleNoAdjacent implements InitialRule {

    @Override
    public boolean isValidMove(Board board, List<Tiles> hand, InitialAction playerMove) {
        AvatarLocation location = playerMove.getAvatarPlacement();

        //TODO: BoardMutator method should require a location not avatar location
        for (Port port : Port.values()) {
            if (BoardMutator.hasAdjacent(board, new AvatarLocation(location.getPosn(), port))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "An initial tile placement cannot have any adjacent tiles";
    }

}
