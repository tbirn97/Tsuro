package src.Player;

import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.AvatarLocation;
import src.Common.Board;
import src.Common.BoardMutator;
import src.Common.Connection;
import src.Common.Facing;
import src.Common.Port;
import src.Common.Posn;
import src.Common.Rotation;
import src.Common.Tiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StrategyUtils {

  /**
   * Returns a list of valid ports that an avatar can be placed at. Will start at Port.A and
   * continue clockwise around the tile.
   */
  public static List<Port> getValidPorts(Board board, Posn loc, boolean clockwise) {
    List<Facing> borders = getLocationBorders(board, loc);
    List<Port> allOrderedPorts = getOrderedPorts(clockwise);
    return getPortList(borders, allOrderedPorts);
  }

  /**
   * Get a list of Posns that are on the border of the given board and are not adjacent to
   * any tiles that have already been placed. Returns the list starting from (0,0) exclusive
   * and continuing clockwise around the board.
   */
  public static List<Posn> getNonAdjacentBorderPosns(Board board, boolean clockwise) {
    List<Posn> allBorderPosns = getBorderPosns(board);
    List<Posn> orderedBorderPosns = getDirectedPosnList(allBorderPosns, clockwise);
    return getFilteredPosns(orderedBorderPosns, board);
  }

  /**
   * Gets all possible intermediate turns from a hand starting from a specific tile in the hand and
   * with zero degrees of rotation. It will then rotate 90 degrees and continue to the next tile in
   * the hand.
   */
  public static List<TurnAction> getAllTurns(AvatarColor avatar, List<Tiles> hand, int startTile) {
    List<TurnAction> allPossibleTurnActions = new ArrayList<>();
    for (int i = 0; i < hand.size(); i++) {
      Tiles tile = hand.get((i + startTile) % hand.size());
      for (Rotation rotation : Rotation.values()) {
        TurnAction turn = new TurnAction(avatar, tile, rotation);
        allPossibleTurnActions.add(turn);
      }
    }
    return allPossibleTurnActions;
  }

  /**
   * Return a list of Facing objects for every board boundary a given location is touching.
   */
  private static List<Facing> getLocationBorders(Board board, Posn loc) {
    List<Posn> boardBounds = board.getBoardBounds();
    Posn topLeft = boardBounds.get(0);
    Posn bottomRight = boardBounds.get(1);

    // list of Facing objects that are invalid for this loc
    ArrayList<Facing> borders = new ArrayList<>();

    if (loc.getX() == topLeft.getX()) {
      // on left border
      borders.add(Facing.WEST);
    }

    if(loc.getY() == topLeft.getY()) {
      // on top border
      borders.add(Facing.NORTH);
    }

    if (loc.getX() == bottomRight.getX()) {
      // on right border
      borders.add(Facing.EAST);
    }

    if (loc.getY() == bottomRight.getY()) {
      // on bottom border
      borders.add(Facing.SOUTH);
    }
    return borders;
  }

  /**
   * Get a list of ordered ports along a tile starting at A inclusive and continuing either
   * clockwise or counter-clockwise depending on it's argument.
   */
  private static List<Port> getOrderedPorts(boolean clockwise) {
    List<Port> allPorts = Arrays.asList(Port.values());
    if (clockwise) {
      return allPorts;
    } else {
      List<Port> reversedPorts = allPorts.stream()
          .sorted(Collections.reverseOrder())
          .collect(Collectors.toList());
      Collections.rotate(reversedPorts, 1);
      return reversedPorts;
    }
  }

  /**
   * Given a list of borders for a given tile where ports are not valid and a list of ordered
   * ports, return a list of valid ports for a location with the given borders.
   */
  private static List<Port> getPortList(List<Facing> borders, List<Port> allOrderedPorts) {
    return allOrderedPorts.stream().filter(port -> {
      Facing portSide = Connection.getPortSide(port);
      return !borders.contains(portSide);
    }).collect(Collectors.toList());
  }

  /**
   * Helper function to get all border posns regardless of if they are valid
   * @param board board object so we know the size of the board
   */
  private static List<Posn> getBorderPosns(Board board) {
    List<Posn> possiblePosns = new ArrayList<>();
    List<Posn> boardBounds = board.getBoardBounds();
    Posn topLeft = boardBounds.get(0);
    Posn bottomRight = boardBounds.get(1);

    for (int x = topLeft.getX() + 1; x <= bottomRight.getX(); x++) {
      Posn locationToCheck = new Posn(x, topLeft.getY());
      possiblePosns.add(locationToCheck);
    }

    for (int y = topLeft.getY(); y <= bottomRight.getY(); y++) {
      Posn locationToCheck = new Posn(bottomRight.getX(), y);
      possiblePosns.add(locationToCheck);
    }

    for (int x = bottomRight.getX(); x >= topLeft.getX(); x--) {
      Posn locationToCheck = new Posn(x, bottomRight.getY());
      possiblePosns.add(locationToCheck);
    }

    // NOTE: not >= here for duplicate check of (0,0)
    for (int y = bottomRight.getY(); y >= topLeft.getY(); y--) {
      Posn locationToCheck = new Posn(topLeft.getX(), y);
      possiblePosns.add(locationToCheck);
    }

    return possiblePosns;
  }

  /**
   * Given a list of Posns starting from 0,0 exclusive and continuing clockwise, change it
   * so that it starts at 0,0 exclusive and continues in the direction indicated by the boolean.
   */
  private static List<Posn> getDirectedPosnList(List<Posn> posns, boolean clockwise) {
    if (clockwise) {
      return posns;
    } else {
      Collections.reverse(posns);
      Collections.rotate(posns, -1);
      return posns;
    }
  }

  /**
   * Filter a list of border posns so that only valid initial turn placement posns remain that
   * are unoccupied and have no adjacent tiles.
   */
  private static List<Posn> getFilteredPosns(List<Posn> orderedBorderPosns, Board board) {
    return orderedBorderPosns.stream().filter(posn ->
        !board.getTile(posn).isPresent() && !hasAnyAdjacent(board, posn)
    ).collect(Collectors.toList());
  }

  /**
   * Return true if the given location has any adjacent tiles to it on the given board.
   */
  private static boolean hasAnyAdjacent(Board board, Posn loc) {
    return BoardMutator.hasAdjacent(board, new AvatarLocation(loc, Port.A))
        || BoardMutator.hasAdjacent(board, new AvatarLocation(loc, Port.C))
        || BoardMutator.hasAdjacent(board, new AvatarLocation(loc, Port.E))
        || BoardMutator.hasAdjacent(board, new AvatarLocation(loc, Port.G));
  }
}
