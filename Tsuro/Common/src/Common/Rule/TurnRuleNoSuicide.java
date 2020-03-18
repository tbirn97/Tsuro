package src.Common.Rule;

import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.Board;
import src.Common.BoardMutator;
import src.Common.Rotation;
import src.Common.Tiles;

import java.util.List;

public class TurnRuleNoSuicide implements TurnRule {

    @Override
    public boolean isValidMove(Board board, List<Tiles> hand, TurnAction playerMove) {
        AvatarColor playerAvatar = playerMove.getPlayerAvatar();
        // if the board throws an exception, fail
        if ( this.isGivenMoveValid(board, playerAvatar,
                playerMove.getTileToPlace(), playerMove.getRotation())) {
            return true;
        }
        else {
            // if we get here, the given configuration caused player suicide

            // NOTE: there is one duplicate check here, assuming the player gave a tile from their hand and a valid rotation
            for (Tiles tile : hand) {
                for (Rotation rotation : Rotation.values()) {

                    if (this.isGivenMoveValid(board, playerAvatar, tile, rotation)) {
                        // there is a valid move for this tile
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private boolean isGivenMoveValid(Board board, AvatarColor playerAvatar, Tiles tile, Rotation rotation) {
        // if the board throws an exception, fail
        Board newBoard;
        try {
            newBoard = BoardMutator.placeTurnTile(board, playerAvatar, tile, rotation);
        }
        catch (IllegalArgumentException e) {
            return false;
        }

        // if the player is still in the game, valid move
        return newBoard.getAvatars().containsKey(playerAvatar);
    }

    @Override
    public String toString() {
        return "On a turn, a player is not allowed to suicide unless it is their only choice.";
    }
}
