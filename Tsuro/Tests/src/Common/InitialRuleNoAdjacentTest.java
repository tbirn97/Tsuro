package src.Common;

import src.Common.Actions.InitialAction;
import src.Common.Rule.InitialRule;
import src.Common.Rule.InitialRuleNoAdjacent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InitialRuleNoAdjacentTest {

    private Board testBoard;
    private int boardSize = 10;

    private Tiles tile;

    private InitialRule rule = new InitialRuleNoAdjacent();

    @Before
    public void init() {
        this.testBoard = new Board.Builder().build();

        // it doesn't matter for these tests what tiles we use
        List<Connection> connections = Arrays.asList(new Connection(Port.A, Port.B),
            new Connection(Port.C, Port.D), new Connection(Port.E, Port.F), new Connection(Port.G, Port.H));

        tile = new Tiles(new ArrayList<>(connections));
    }

    private void testIsValidMove(Posn location, boolean expected) {
        InitialAction playerAction = new InitialAction(AvatarColor.red, tile, Rotation.R90, new AvatarLocation(location, Port.A));
        assertEquals(
                String.format("Expected %b at location (%d, %d)", expected, location.getX(), location.getY()),
                expected,
                rule.isValidMove(this.testBoard, new ArrayList<>(), playerAction));
    }

    @Test
    public void isValidMoveTop() {
        this.testBoard = BoardMutator.placeStartingTile(testBoard, tile, new Posn(0,0), Rotation.R90);
        this.testIsValidMove(new Posn(1, 0), false);

        // make sure our board did not place the tile when checking the rule
        assertEquals(1, this.testBoard.getTiles().size());
    }

    @Test
    public void isValidMoveLeft() {
        this.testBoard = BoardMutator.placeStartingTile(testBoard, tile, new Posn(0,3), Rotation.R90);
        this.testIsValidMove(new Posn(0, 4), false);

        // make sure our board did not place the tile when checking the rule
        assertEquals(1, this.testBoard.getTiles().size());
    }

    @Test
    public void isValidMoveRight() {
        this.testBoard = BoardMutator.placeStartingTile(testBoard, tile, new Posn(boardSize - 1,3), Rotation.R0);
        this.testIsValidMove(new Posn(boardSize - 1, 2), false);

        // make sure our board did not place the tile when checking the rule
        assertEquals(1, this.testBoard.getTiles().size());
    }

    @Test
    public void isValidMoveBottom() {
        this.testBoard = BoardMutator.placeStartingTile(testBoard, tile, new Posn(6,boardSize - 1), Rotation.R180);
        this.testIsValidMove(new Posn(7, boardSize - 1), false);

        // make sure our board did not place the tile when checking the rule
        assertEquals(1, this.testBoard.getTiles().size());
    }

    @Test
    public void isValidMoveMultiple() {
        this.testBoard = BoardMutator.placeStartingTile(testBoard, tile, new Posn(boardSize - 1,boardSize - 1), Rotation.R180);
        this.testBoard = BoardMutator.placeStartingTile(testBoard, tile, new Posn(boardSize - 1, 8), Rotation.R270);
        this.testIsValidMove(new Posn(boardSize - 1, 9), false);

        // make sure our board did not place the tile when checking the rule
        assertEquals(2, this.testBoard.getTiles().size());
    }

    @Test
    public void isValidMoveNoAdjOnEdge() {
        this.testIsValidMove(new Posn(0, 4), true);

        // make sure our board did not place the tile when checking the rule
        assertEquals(0, this.testBoard.getTiles().size());

        this.testBoard = BoardMutator.placeStartingTile(testBoard, tile, new Posn(0, 4), Rotation.R90);
        this.testIsValidMove(new Posn(0, 2), true);

        // make sure our board did not place the tile when checking the rule
        assertEquals(1, this.testBoard.getTiles().size());
    }

    @Test
    public void isValidMoveNoAdjOnCorner() {
        this.testIsValidMove(new Posn(0, 0), true);

        // make sure our board did not place the tile when checking the rule
        assertEquals(0, this.testBoard.getTiles().size());

        this.testBoard = BoardMutator.placeStartingTile(testBoard, tile, new Posn(0, 4), Rotation.R90);
        this.testIsValidMove(new Posn(0, 0), true);

        // make sure our board did not place the tile when checking the rule
        assertEquals(1, this.testBoard.getTiles().size());
    }
}