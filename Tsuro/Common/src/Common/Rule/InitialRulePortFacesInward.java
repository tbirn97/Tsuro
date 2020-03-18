package src.Common.Rule;

import src.Common.Actions.InitialAction;
import src.Common.Board;
import src.Common.Port;
import src.Common.Posn;
import src.Common.Tiles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Function object to enforce the initial rule that the
 * port location must face inward (cannot be on the edge
 * of the board)
 */
public class InitialRulePortFacesInward implements InitialRule {
    @Override
    public boolean isValidMove(Board board, List<Tiles> hand, InitialAction playerMove) {

        Posn location = playerMove.getAvatarPlacement().getPosn();
        Port port = playerMove.getAvatarPlacement().getPort();

        List<Posn> boardBounds = board.getBoardBounds();
        Posn upperLeft = boardBounds.get(0);
        Posn lowerRight = boardBounds.get(1);

        Set<Port> invalid = new HashSet<>();

        // if on left
        if (upperLeft.getX() == location.getX()) {
            invalid.add(Port.G);
            invalid.add(Port.H);
        }
        // if on top
        if (upperLeft.getY() == location.getY()) {
            invalid.add(Port.A);
            invalid.add(Port.B);
        }
        // if on right
        if (lowerRight.getX() == location.getX()) {
            invalid.add(Port.C);
            invalid.add(Port.D);
        }
        // if on bottom
        if (lowerRight.getY() == location.getY()) {
            invalid.add(Port.E);
            invalid.add(Port.F);
        }

        return !invalid.contains(port);
    }

    @Override
    public String toString() {
        return "An initial avatar location cannot face an edge of the board.";
    }
}
