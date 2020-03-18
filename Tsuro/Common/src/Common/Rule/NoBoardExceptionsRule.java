package src.Common.Rule;

import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.Board;
import src.Common.BoardMutator;
import src.Common.Tiles;

import java.util.List;

public class NoBoardExceptionsRule implements InitialRule, TurnRule {
  @Override
  public boolean isValidMove(Board board, List<Tiles> hand, InitialAction playerTurn) {
    try {
      Board newBoard = BoardMutator.placeStartingTile(board, playerTurn.getTileToPlace(),
          playerTurn.getAvatarPlacement().getPosn(),
          playerTurn.getRotation());
      newBoard = BoardMutator.placeAvatar(newBoard, playerTurn.getPlayerAvatar(), playerTurn.getAvatarPlacement());
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @Override
  public boolean isValidMove(Board board, List<Tiles> hand, TurnAction playerMove) {
    try {
      Board newBoard = BoardMutator.placeTurnTile(board, playerMove.getPlayerAvatar(),
          playerMove.getTileToPlace(),
          playerMove.getRotation());
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
