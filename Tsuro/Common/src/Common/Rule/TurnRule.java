package src.Common.Rule;

import src.Common.Actions.TurnAction;
import src.Common.Board;
import src.Common.Tiles;

import java.util.List;

public interface TurnRule {

    boolean isValidMove(Board board, List<Tiles> hand, TurnAction playerMove);
}
