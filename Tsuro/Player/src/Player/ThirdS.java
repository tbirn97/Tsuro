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

public class ThirdS implements Strategy {

  /**
   * Searches for the first legal spot avaiable to place a tile starting from (0, 0) exclusive and searching the
   * border of the board counter-clockwise. Will then search for a valid port starting from Port.A and searching
   * counter-clockwise around the tile.
   */
  @Override
  public InitialAction getInitialTurn(AvatarColor avatar, Board board, List<Tiles> hand, Rules rules) {
    List<Posn> validPosns = StrategyUtils.getNonAdjacentBorderPosns(board, false);
    Posn selectedPosn = validPosns.get(0);

    List<Port> validPorts = StrategyUtils.getValidPorts(board, selectedPosn, false);
    Port selectedPort = validPorts.get(0);

    return new InitialAction(avatar, hand.get(2), Rotation.R0, new AvatarLocation(selectedPosn, selectedPort));
  }

  /**
   * Searches for the first legal turn that can be made starting at the second tile in the hand before checking
   * the first tile. Will start with 0 degrees of rotation before checking 90 then 180 and finally 270. This
   * will return the first valid TurnAction it finds, or the second tile with zero rotation if no valid move
   * is found.
   */
  @Override
  public TurnAction getTurn(AvatarColor avatar, Board board, List<Tiles> hand, Rules rules) {
    List<TurnAction> allTurns = StrategyUtils.getAllTurns(avatar, hand, 1);

    for (TurnAction turn : allTurns) {
      if (rules.isValidTurn(board, hand, turn)) {
        return turn;
      }
    }
    return allTurns.get(0);
  }
}
