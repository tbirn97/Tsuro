package src.Common;

import src.Common.Actions.TurnAction;
import src.Common.Rule.TurnRule;
import src.Common.Rule.TurnRuleNoInfiniteLoops;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TurnRuleNoInfiniteLoopsTest {

    private TurnRule rule = new TurnRuleNoInfiniteLoops();
    private Board testBoard;
    private Posn startingLoc = new Posn(3, 0);

    private Tiles loopTile = new Tiles(
            new ArrayList<>(Arrays.asList(
                    new Connection(Port.A, Port.B),
                    new Connection(Port.C, Port.D),
                    new Connection(Port.E, Port.F),
                    new Connection(Port.G, Port.H)
            )));

    private Tiles otherTile = new Tiles(
            new ArrayList<>(Arrays.asList(
                    new Connection(Port.A, Port.E),
                    new Connection(Port.B, Port.F),
                    new Connection(Port.C, Port.G),
                    new Connection(Port.D, Port.H)
            )));

    @Before
    public void init() {
        testBoard = new Board.Builder().build();
        testBoard = BoardMutator.placeStartingTile(testBoard, loopTile, startingLoc, Rotation.R270);
        testBoard = BoardMutator.placeAvatar(testBoard, AvatarColor.white, new AvatarLocation(startingLoc, Port.H));
    }

    @Test
    public void isValidMoveSmallLoopOptions() {
        ArrayList<Tiles> hand = new ArrayList<>(Arrays.asList(loopTile, otherTile));
        TurnAction move = new TurnAction(AvatarColor.white, loopTile, Rotation.R90);

        assertFalse(
                "src.Admin.Player has a valid move in their hand",
                rule.isValidMove(testBoard, hand, move));

        assertEquals(1, testBoard.getTiles().size());
    }

    @Test
    public void isValidMoveBigLoopOptions() {
        testBoard = BoardMutator.placeTurnTile(testBoard, AvatarColor.white, otherTile, Rotation.R0);

        ArrayList<Tiles> hand = new ArrayList<>(Arrays.asList(loopTile, otherTile));
        TurnAction move = new TurnAction(AvatarColor.white, loopTile, Rotation.R90);
        assertFalse(rule.isValidMove(testBoard, hand, move));

        assertEquals(2, testBoard.getTiles().size());
    }

    @Test
    public void isValidMoveSmallLoopNoOptions() {
        ArrayList<Tiles> hand = new ArrayList<>(Arrays.asList(loopTile, loopTile));
        TurnAction move = new TurnAction(AvatarColor.white, loopTile, Rotation.R90);
        assertTrue(rule.isValidMove(testBoard, hand, move));

        assertEquals(1, testBoard.getTiles().size());
    }

    @Test
    public void isValidMoveBigLoopNoOptions() {
        testBoard = BoardMutator.placeTurnTile(testBoard, AvatarColor.white, otherTile, Rotation.R0);

        ArrayList<Tiles> hand = new ArrayList<>(Arrays.asList(loopTile, loopTile));
        TurnAction move = new TurnAction(AvatarColor.white, loopTile, Rotation.R90);
        assertTrue(rule.isValidMove(testBoard, hand, move));

        assertEquals(2, testBoard.getTiles().size());
    }

}