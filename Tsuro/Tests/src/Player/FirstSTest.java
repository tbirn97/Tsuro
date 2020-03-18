package src.Player;

import src.Common.*;
import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FirstSTest {
    private FirstS strat = new FirstS();
    private TilesFactory tilesFactory = new TilesFactory();

    private Board board;
    private List<Tiles> hand;
    private final int size = 10;

    @Before
    public void init() {
        board = new Board.Builder().build();
        hand = Arrays.asList(tilesFactory.makeTile(0), tilesFactory.makeTile(1), tilesFactory.makeTile(2));
    }

    private void placeTilesRow(int y, int minX, int maxX) {
        for (int x = minX; x < maxX; x++) {
            board = BoardMutator.placeStartingTile(board, tilesFactory.makeTile(0), new Posn(x,y), Rotation.R0);
        }
    }

    private void placeTilesCol(int x, int minY, int maxY) {
        for (int y = minY; y < maxY; y++) {
            board = BoardMutator.placeStartingTile(board, tilesFactory.makeTile(0), new Posn(x, y), Rotation.R0);
        }
    }

    private InitialAction getExpectedInitialAction(int x, int y, Port port) {
        return new InitialAction(AvatarColor.white, tilesFactory.makeTile(2), Rotation.R0, new AvatarLocation(x, y, port));
    }

    private void initialPlacement(InitialAction expected) {
        assertEquals(expected, strat.getInitialTurn(AvatarColor.white, board, hand, new Rules()));
    }

    @Test
    public void testInitialPlaceOnEmptyBoard() {
        InitialAction expectedAction = getExpectedInitialAction(1, 0, Port.C);
        initialPlacement(expectedAction);
    }

    @Test
    public void testInitialPlacementOnNonEmptyBoard() {
        board = BoardMutator.placeStartingTile(board, tilesFactory.makeTile(0), new Posn(1,0), Rotation.R90);

        InitialAction expectedAction = getExpectedInitialAction(3, 0, Port.C);
        initialPlacement(expectedAction);
    }

    @Test
    public void testInitialPlacementOnRight() {

        placeTilesRow(0, 0, size - 2);

        InitialAction expectedAction = getExpectedInitialAction(9, 0, Port.E);
        initialPlacement(expectedAction);

        board= BoardMutator.placeStartingTile(board, tilesFactory.makeTile(0), new Posn(9, 0), Rotation.R0);

        expectedAction = getExpectedInitialAction(9, 2, Port.A);
        initialPlacement(expectedAction);
    }


    @Test
    public void testInitialPlacementOnBottom() {

        placeTilesRow(0, 0, size);
        placeTilesCol(size - 1, 1, size - 2);

        InitialAction expectedAction = getExpectedInitialAction(9, 9, Port.A);
        initialPlacement(expectedAction);

        board = BoardMutator.placeStartingTile(board, tilesFactory.makeTile(0), new Posn(9, 9), Rotation.R0);

        expectedAction = getExpectedInitialAction(7, 9, Port.A);
        initialPlacement(expectedAction);
    }

    @Test
    public void testInitialPlacementLeft() {
        placeTilesRow(0, 0, size);
        placeTilesCol(size - 1, 1, size);
        placeTilesRow(size - 1, 2, size - 1);

        InitialAction expectedAction = getExpectedInitialAction(0, 9, Port.A);
        initialPlacement(expectedAction);

        board = BoardMutator.placeStartingTile(board, tilesFactory.makeTile(0), new Posn(0, 9), Rotation.R0);

        expectedAction = getExpectedInitialAction(0, 7, Port.A);
        initialPlacement(expectedAction);
    }

    @Test
    public void testTurnPlacement() {
        TurnAction expected = new TurnAction(AvatarColor.white, hand.get(0), Rotation.R0);
        assertEquals(expected, strat.getTurn(AvatarColor.white, board, hand, new Rules()));
    }
}