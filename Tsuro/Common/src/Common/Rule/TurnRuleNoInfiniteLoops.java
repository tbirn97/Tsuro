package src.Common.Rule;

import src.Common.Actions.TurnAction;
import src.Common.Board;
import src.Common.BoardMutator;
import src.Common.BoardState;
import src.Common.Rotation;
import src.Common.Tiles;

import java.util.List;

/**
 * TurnRule that enforces the player cannot play a turn that puts them into an
 * infinite loop unless it's their only option.
 */
public class TurnRuleNoInfiniteLoops implements TurnRule {

    @Override
    public boolean isValidMove(Board board, List<Tiles> hand, TurnAction playerMove) {
        if (this.isGivenMoveValid(board, playerMove)) {
            return true;
        }
        else {
            // infinite loop, check other configurations
            // NOTE: there is one duplicate check here, assuming
            // the player gave a tile from their hand and a valid rotation
            for (Tiles tile : hand) {
                for (Rotation rotation : Rotation.values()) {
                    if (this.isGivenMoveValid(board, new TurnAction(playerMove.getPlayerAvatar(), tile, rotation))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private boolean isGivenMoveValid(Board board, TurnAction playerMove) {
        Board newBoard = BoardMutator.placeTurnTile(board, playerMove.getPlayerAvatar(), playerMove.getTileToPlace(), playerMove.getRotation());
        return !newBoard.getState().equals(BoardState.LOOP);
    }

    @Override
    public String toString() {
        return "On a turn, infinite loops are only allowed if they are the player's only option";
    }
}
