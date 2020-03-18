package src.Common;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BoardTest {

    private Map<Posn, Tiles> boardTiles;
    private Map<AvatarColor, AvatarLocation> tokenSet;

    private TilesFactory tilesFactory = new TilesFactory();

    @Before
    public void init() {
        this.boardTiles = new HashMap<>();
        this.tokenSet = new HashMap<>();
    }

    @Test
    public void getBoardSize() {
        Board board = new Board.Builder().build();
        assertEquals(board.getBoardSize(), 10);
        Board board1 = new Board.Builder().setBoardSize(20).build();
        assertEquals(board1.getBoardSize(), 20);


        tokenSet.put(AvatarColor.red, new AvatarLocation(0, 5, Port.G));
        Board board2 = new Board.Builder().setAvatars(tokenSet).setTiles(boardTiles).build();
        assertEquals(board2.getBoardSize(), 10);

        this.init();
        tokenSet.put(AvatarColor.blue, new AvatarLocation(0, 5, Port.G));
        Board board3 = new Board.Builder().setAvatars(tokenSet).setTiles(boardTiles).setBoardSize(40).build();
        assertEquals(board3.getBoardSize(), 40);
    }

    @Test
    public void getTokens() {
        Board board = new Board.Builder().build();
        assertEquals(board.getAvatars(), tokenSet);

        tokenSet.put(AvatarColor.red, new AvatarLocation(0, 5, Port.G));
        Board board1 = new Board.Builder().setAvatars(tokenSet).setTiles(boardTiles).build();
        assertEquals(board1.getAvatars().get(AvatarColor.red), tokenSet.get(AvatarColor.red));
    }

    @Test
    public void getBoard() {
        tokenSet.put(AvatarColor.red, new AvatarLocation(0, 5, Port.G));
        Board board2 = new Board.Builder().setAvatars(tokenSet).setTiles(boardTiles).build();
        assertEquals(board2.getTiles(), boardTiles);
    }
}