package src.Common;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BoardMutatorTest {

    private Map<Posn, Tiles> boardTiles;
    private Map<AvatarColor, AvatarLocation> tokenSet;

    private TilesFactory tilesFactory = new TilesFactory();

    @Before
    public void init() {
        this.boardTiles = new HashMap<>();
        this.tokenSet = new HashMap<>();
    }

    @Test
    public void testBoardConstructors() {
        Board board = new Board.Builder().build();
        assertEquals(board.getBoardSize(), 10);

        tokenSet.put(AvatarColor.red, new AvatarLocation(0, 5, Port.G));
        Board board2 = new Board.Builder().setAvatars(tokenSet).setTiles(boardTiles).build();
        assertEquals(board2.getBoardSize(), 10);
    }

    @Test
    public void removePlayer() {
        tokenSet.put(AvatarColor.red, new AvatarLocation(0, 5, Port.G));
        Board board2 = new Board.Builder().setAvatars(tokenSet).setTiles(boardTiles).build();
        board2 = BoardMutator.removePlayer(board2, AvatarColor.red);
        assertTrue(board2.getAvatars().isEmpty());
    }

    @Test
    public void placeStartingTile() {

        tokenSet.put(AvatarColor.red, new AvatarLocation(0, 5, Port.G));
        Board board1 = new Board.Builder().setAvatars(tokenSet).setTiles(boardTiles).build();
        Tiles tile1 = tilesFactory.makeTile(0);
        Posn tile1Loc = new Posn(0, 0);



        board1 = BoardMutator.placeStartingTile(board1, tile1, tile1Loc, Rotation.R0);

        //Tests adding one tile
        assertTrue(board1.getTiles().containsValue(tile1));

        this.init();
        tokenSet.put(AvatarColor.blue, new AvatarLocation(0, 5, Port.G));
        Board board2 = new Board.Builder().setAvatars(tokenSet).setTiles(boardTiles).build();
        Tiles tile2 = tilesFactory.makeTile(6);
        Posn tileLoc2 = new Posn(0, 3);

        ArrayList<Connection> connections3 = new ArrayList<>();
        connections3.add(new Connection(Port.C, Port.G)); // CG
        connections3.add(new Connection(Port.D, Port.E)); // DE
        connections3.add(new Connection(Port.A, Port.F)); // AF
        connections3.add(new Connection(Port.B, Port.H)); // BH
        Collections.sort(connections3);
        Tiles rotatedTile2 = new Tiles(connections3);

        board2 = BoardMutator.placeStartingTile(board2, tile2, tileLoc2, Rotation.R90);

        //Tests adding rotated tile
        assertTrue(board2.getTiles().containsValue(rotatedTile2));
    }

    @Test
    public void placeAvatar() {
        Board board1 = new Board.Builder().build();
        Tiles tile1 = tilesFactory.makeTile(0);
        Posn tile1Loc = new Posn(0, 0);

        board1 = BoardMutator.placeStartingTile(board1, tile1, tile1Loc, Rotation.R0);
        board1 = BoardMutator.placeAvatar(board1, AvatarColor.red, new AvatarLocation(0, 0, Port.B));

        assertEquals(board1.getAvatars().get(AvatarColor.red).getPort(), Port.B);
    }

    @Test
    public void placeTile() {

        Board board1 = new Board.Builder().build();

        Tiles tile1 = tilesFactory.makeTile(0);
        Posn tile1Loc = new Posn(0, 0);

        board1 = BoardMutator.placeStartingTile(board1, tile1, tile1Loc, Rotation.R0);
        board1 = BoardMutator.placeAvatar(board1, AvatarColor.red, new AvatarLocation(0, 0, Port.D));

        ArrayList<Connection> connections2 = new ArrayList<>();
        connections2.add(new Connection(Port.A, Port.D));
        connections2.add(new Connection(Port.B, Port.C));
        connections2.add(new Connection(Port.D, Port.G));
        connections2.add(new Connection(Port.F, Port.H));
        Tiles tile2 = new Tiles(connections2);
        board1 = BoardMutator.placeTurnTile(board1, AvatarColor.red, tile2, Rotation.R0);

        assertEquals(Port.D, board1.getAvatars().get(AvatarColor.red).getPort());
    }


    @Test
    public void testHarnessTest3() {
        // [[18, 180], "red", 2, 0, 0]
        // tile 18: ((A C) (B E) (D H) (F G))
        Tiles red1 = tilesFactory.makeTile(18);
        red1 = red1.rotate(Rotation.R180);
        Posn r1 = new Posn(0, 0);

        boardTiles.put(r1, red1);
        tokenSet.put(AvatarColor.red, new AvatarLocation(r1, Port.C));

        // [[8, 0], 0, 1]
        // tile 8: ((A D) (B F) (C G) (E H))
        Tiles red2 = tilesFactory.makeTile(8);

        Board board = new Board.Builder().setAvatars(tokenSet).setTiles(boardTiles).build();

        board = BoardMutator.placeTurnTile(board, AvatarColor.red, red2, Rotation.R0);

        board = BoardMutator.placeStartingTile(board, red2, new Posn(8, 0), Rotation.R0);
        board = BoardMutator.placeAvatar(board, AvatarColor.blue, new AvatarLocation(8, 0, Port.E));

        // ["red", [20, 90]]
        // tile 20: ((A C) (B D) (E H) (F G))
        Tiles red3 = tilesFactory.makeTile(20);
        System.out.println(board.toString());
        board = BoardMutator.placeTurnTile(board, AvatarColor.red, red3, Rotation.R90);

        // ["red", [21, 0]]
        // tile 21: ((A C) (B D) (E G) (F H))
        Tiles red4 = tilesFactory.makeTile(21);
        board = BoardMutator.placeTurnTile(board, AvatarColor.red, red4, Rotation.R0);

        assertFalse(board.getAvatars().containsKey(AvatarColor.red));

    }

}