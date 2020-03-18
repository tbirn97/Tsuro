package xboard;

import harness.TsuroTestHarness;
import src.Common.*;
import org.json.JSONArray;

import java.util.HashMap;

public class BoardTestHarness extends TsuroTestHarness {

    public static void main(String[] argv) {
        JSONArray input = TsuroTestHarness.parseInput();
        // run on board
        BoardTestHarness testHarness = new BoardTestHarness();

        JSONArray statePats = TsuroTestHarness.parseObjToArray(input.remove(0));

        Board board = testHarness.setUpBoard(statePats);

        boolean redWasInGame = board.getAvatars().containsKey(AvatarColor.RED);

        if (redWasInGame) {
            testHarness.parseActionPats(board, TsuroTestHarness.parseObjToArray(input));
        }

        // print output
        testHarness.printOutput(board, redWasInGame);
    }

    public void printOutput(Board board, boolean redWasInGame) {
        if (!board.getAvatars().containsKey(AvatarColor.RED) && redWasInGame) {
            System.out.println("\"red died\"");
        }
        else if (!redWasInGame) {
            System.out.println("\"red never played\"");
        }
        else {
            // where is the red avatar now?
            JSONArray placement = new JSONArray();
            JSONArray tilePat = new JSONArray();
            AvatarLocation tokenPos = board.getAvatars().get(AvatarColor.RED);
            Tiles tile = board.getTiles().get(tokenPos.getPosn());
            int[] indexAndRotation = this.getIndexFromTile(tile);
            tilePat.put(indexAndRotation[0]);

            tilePat.put(indexAndRotation[1]);
            placement.put(tilePat);
            placement.put(String.format("%s", AvatarColor.RED));
            placement.put(tokenPos.getPort());
            placement.put(tokenPos.getX());
            placement.put(tokenPos.getY());
            System.out.println(placement.toString());
        }
    }

    public Board parseActionPats(Board board, JSONArray actionPats) {
        for (Object obj : actionPats) {
            JSONArray actionPat = TsuroTestHarness.parseObjToArray(obj);
            AvatarColor avaColor = getAvatarColor(actionPat.get(0).toString());
            JSONArray tilePat = TsuroTestHarness.parseObjToArray(actionPat.get(1));
            Facing dir = getFacingFromInt(TsuroTestHarness.parseObjToInt(tilePat.get(1)));
            Tiles tile = getTileFromTilePat(tilePat);

            board = BoardMutator.placeTurnTile(board, avaColor, tile, dir);

            BoardState state = board.getState();

            if (state.equals(BoardState.LOOP)) {
                //System.out.println("\"infinite\"");
                System.exit(0);
            } else if (state.equals(BoardState.COLLISION)) {
                //System.out.println("\"collision\"");
                System.exit(0);
            }
        }
        return board;
    }

    public Board setUpBoard(JSONArray statePats) {
        HashMap<AvatarColor, AvatarLocation> tokens = new HashMap<>();
        HashMap<Posn, Tiles> boardInner = new HashMap<>();

        for (Object obj : statePats) {
            JSONArray statePat = TsuroTestHarness.parseObjToArray(obj);

            switch (statePat.length()) {
                case 5:
                    // initial place
                    JSONArray tilePat = TsuroTestHarness.parseObjToArray(statePat.get(0));
                    Tiles tile = getTileFromTilePat(tilePat);
                    tile = tile.rotate(getFacingFromInt(TsuroTestHarness.parseObjToInt(tilePat.get(1))));
                    // ^maybe move into getTileFromTilePat ?


                    Posn loc = new Posn(TsuroTestHarness.parseObjToInt(statePat.get(3)), TsuroTestHarness.parseObjToInt(statePat.get(4)));
                    //TODO: Make a port enum so that this logic isn't so terrible
                    AvatarLocation aLoc = new AvatarLocation(loc, statePat.getString(2).charAt(0) - 'A');

                    tokens.put(getAvatarColor(statePat.get(1).toString()), aLoc);
                    boardInner.put(loc, tile);
                    break;
                case 3:
                    // intermediate place
                    tilePat = TsuroTestHarness.parseObjToArray(statePat.get(0));
                    tile = getTileFromTilePat(tilePat);
                    tile = tile.rotate(getFacingFromInt(TsuroTestHarness.parseObjToInt(tilePat.get(1))));
                    loc = new Posn(
                            TsuroTestHarness.parseObjToInt(statePat.get(1)),
                            TsuroTestHarness.parseObjToInt(statePat.get(2)));
                    boardInner.put(loc, tile);
                    break;

            }
        }
        return new Board.Builder().setAvatars(tokens).setTiles(boardInner).build();
    }

    private Tiles getTileFromTilePat(JSONArray tilePat) {
        int tileIndex = TsuroTestHarness.parseObjToInt(tilePat.get(0));
        return getTileFromIndex(tileIndex);
    }
}