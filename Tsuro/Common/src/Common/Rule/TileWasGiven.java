package src.Common.Rule;

import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.Board;
import src.Common.Tiles;

import java.util.List;

public class TileWasGiven implements InitialRule, TurnRule {
  @Override
  public boolean isValidMove(Board board, List<Tiles> hand, InitialAction playerTurn) {
    Tiles tileToPlace = playerTurn.getTileToPlace();
    return hand.contains(tileToPlace);
  }

  @Override
  public boolean isValidMove(Board board, List<Tiles> hand, TurnAction playerMove) {
    Tiles tileToPlace = playerMove.getTileToPlace();
    return hand.contains(tileToPlace);
  }
}
