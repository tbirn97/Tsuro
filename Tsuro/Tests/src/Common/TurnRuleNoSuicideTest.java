package src.Common;

import src.Common.Actions.TurnAction;
import src.Common.Rule.TurnRule;
import src.Common.Rule.TurnRuleNoSuicide;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TurnRuleNoSuicideTest {

    private TurnRule rule = new TurnRuleNoSuicide();
    private Board testBoard;
    private Posn startingLoc = new Posn(3, 0);
    private Tiles suicideTile = new Tiles(
            new ArrayList<>(Arrays.asList(
                    new Connection(Port.A, Port.B),
                    new Connection(Port.C, Port.D),
                    new Connection(Port.E, Port.F),
                    new Connection(Port.G, Port.H))));

    private Tiles nonSuicideTile = new Tiles(
            new ArrayList<>(Arrays.asList(
                    new Connection(Port.A, Port.E),
                    new Connection(Port.B, Port.F),
                    new Connection(Port.C, Port.G),
                    new Connection(Port.D, Port.H)
            )));


    @Before
    public void init() {
        testBoard = new Board.Builder().build();
        List<Connection> conn = Arrays.asList(
                new Connection(Port.A, Port.D),
                new Connection(Port.B, Port.F),
                new Connection(Port.C, Port.G),
                new Connection(Port.E, Port.H));
        Tiles startingTile = new Tiles(new ArrayList<>(conn));
        testBoard = BoardMutator.placeStartingTile(testBoard, startingTile, startingLoc, Rotation.R90);
        testBoard = BoardMutator.placeAvatar(testBoard, AvatarColor.blue, new AvatarLocation(startingLoc, Port.H));
        testBoard = BoardMutator.placeAvatar(testBoard, AvatarColor.red, new AvatarLocation(9, 9, Port.B));

    }

    @Test
    public void isValidMoveFastSuicide() {
        ArrayList<Tiles> nonSuicideHand = new ArrayList<>(Arrays.asList(
                suicideTile,
                nonSuicideTile
        ));
        TurnAction move = new TurnAction(AvatarColor.blue, suicideTile, Rotation.R0);
        assertFalse("src.Admin.Player has valid tile to play in their hand", rule.isValidMove(testBoard, nonSuicideHand, move));

    }

    @Test
    public void isValidMoveSlowSuicide() {
        testBoard = BoardMutator.placeTurnTile(testBoard, AvatarColor.blue, nonSuicideTile, Rotation.R0);

        ArrayList<Tiles> nonSuicideHand = new ArrayList<>(Arrays.asList(
                suicideTile,
                nonSuicideTile
        ));
        TurnAction move = new TurnAction(AvatarColor.blue, suicideTile, Rotation.R0);
        assertFalse("src.Admin.Player has valid tile to play in their hand", rule.isValidMove(testBoard, nonSuicideHand, move));
    }

    @Test
    public void isValidMoveNoOtherOptions() {
        testBoard = BoardMutator.placeTurnTile(testBoard, AvatarColor.blue, nonSuicideTile, Rotation.R0);

        ArrayList<Tiles> suicideHand = new ArrayList<>(Arrays.asList(
                suicideTile,
                suicideTile
        ));
        TurnAction move = new TurnAction(AvatarColor.blue, suicideTile, Rotation.R0);
        assertTrue("src.Admin.Player has no valid tiles to play in their hand", rule.isValidMove(testBoard, suicideHand, move));
    }
}