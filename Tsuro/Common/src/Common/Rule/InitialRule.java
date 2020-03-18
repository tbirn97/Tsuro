package src.Common.Rule;

import src.Common.Actions.InitialAction;
import src.Common.Board;
import src.Common.Tiles;

import java.util.List;

/**
 * Interface for function objects that enforce rules on the
 * initial move in a Tsuro game
 */
public interface InitialRule {

    boolean isValidMove(Board board, List<Tiles> hand, InitialAction playerTurn);
}
