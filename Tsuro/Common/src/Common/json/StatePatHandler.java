package src.Common.json;

import org.json.JSONArray;
import src.Common.AvatarColor;
import src.Common.AvatarLocation;
import src.Common.Board;
import src.Common.Port;
import src.Common.Posn;
import src.Common.Rotation;
import src.Common.Tiles;
import src.Common.TilesFactory;
import src.Common.json.serialization.InitialPlace;
import src.Common.json.serialization.IntermediatePlace;
import src.Common.json.serialization.TilePat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatePatHandler {

    private static TilesFactory factory = new TilesFactory();

    /**
     * This function takes in a json array of state-pats and converts them into a board object
     * @param jsonArray state-pats
     * @return
     */
    public static Board jsonToBoard(JSONArray jsonArray) {
        Map<AvatarColor, AvatarLocation> avatars = new HashMap<>();
        Map<Posn, Tiles> tiles = new HashMap<>();
        for(Object o : jsonArray) {
            JSONArray turn = (JSONArray)o;
            if(turn.length() == 5) {
                InitialPlace action = createInitialPlace(turn);
                Posn tileLocation = new Posn(action.getX(), action.getY());
                AvatarColor avatarColor = action.getAvatarColor();
                Port portInt = Port.valueOf(action.getPort());
                AvatarLocation avatarLocation = new AvatarLocation(tileLocation, portInt);
                Tiles tile = factory.makeTile(action.getTilePat().getIndex());
                Rotation rotation = Rotation.getRotationFromDegrees(action.getTilePat().getDegree());
                Tiles rotatedTile = tile.rotate(rotation);

                tiles.put(tileLocation, rotatedTile);
                avatars.put(avatarColor, avatarLocation);

            } else if(turn.length() == 3) {
                IntermediatePlace action = createIntermediatePlace(turn);
                Posn tileLocation = new Posn(action.getX(), action.getY());
                Tiles tile = factory.makeTile(action.getTilePat().getIndex());
                Rotation rotation = Rotation.getRotationFromDegrees(action.getTilePat().getDegree());
                Tiles rotatedTile = tile.rotate(rotation);

                tiles.put(tileLocation, rotatedTile);
            } else {
                throw new RuntimeException("Malformed Turn");
            }
        }
        return new Board.Builder().setAvatars(avatars).setTiles(tiles).build();
    }

    /**
     * This function takes in a board and converts it into an array of state-pats
     * @param board a Board object
     * @return
     */
    public static JSONArray boardToJSON(Board board) {
        JSONArray statePats = new JSONArray();
        Map<AvatarColor, AvatarLocation> avatars = board.getAvatars();
        Map<Posn, Tiles> tiles = board.getTiles();
        Set<Posn> locations = new HashSet<>();
        for(AvatarColor avatarColor : avatars.keySet()) {
            AvatarLocation avatarLocation = avatars.get(avatarColor);
            Posn avatarTilePosn = avatarLocation.getPosn();
            Tiles avatarTile = tiles.get(avatarTilePosn);

            TilePat tilePat = factory.getTilePatFromTile(avatarTile);
            String port = avatarLocation.getPort().name();
            int x = avatarTilePosn.getX();
            int y = avatarTilePosn.getY();

            InitialPlace initialPlace = new InitialPlace(tilePat, avatarColor, port, x, y);
            statePats.put(createJSONArrayInitialPlace(initialPlace));
            locations.add(avatarTilePosn);
        }
        for (Posn tileLocation : tiles.keySet()) {
            if(!locations.contains(tileLocation)) {
                int x = tileLocation.getX();
                int y = tileLocation.getY();
                TilePat tilePat = factory.getTilePatFromTile(tiles.get(tileLocation));
                IntermediatePlace intermediatePlace = new IntermediatePlace(tilePat, x, y);
                statePats.put(createJSONArrayIntermediatePlace(intermediatePlace));
            }
        }
        return statePats;
    }

    /**
     * Given an initialplace JSONArray (a state-pat of length 5) and turns it into an InitialPlace
     * @param initialPlace a state-pat
     * @return
     */
    private static InitialPlace createInitialPlace(JSONArray initialPlace) {
        if(initialPlace.length() != 5) {
            throw new RuntimeException("Unable to create Intermediate Turn Action, length of " +
                    "the array is less than 5.");
        }
        TilePat tilePat = ActionHandler.getTilePat((JSONArray) initialPlace.get(0));
        AvatarColor color = AvatarColor.valueOf(initialPlace.get(1).toString());
        String port = initialPlace.getString(2);
        int x = initialPlace.getInt(3);
        int y = initialPlace.getInt(4);
        return new InitialPlace(tilePat, color, port, x, y);
    }

    /**
     * Given an intermediateplace JSONArray (a state-pat of length 3) and turns it into an
     * IntermediatePlace
     * @param intermediatePlace a state-pat
     * @return
     */
    private static IntermediatePlace createIntermediatePlace(JSONArray intermediatePlace) {
        if(intermediatePlace.length() != 3) {
            throw new RuntimeException("Unable to create Turn Action, length of " +
                    "the array is less than 3.");
        }
        TilePat tilePat = ActionHandler.getTilePat((JSONArray) intermediatePlace.get(0));
        int x = intermediatePlace.getInt(1);
        int y = intermediatePlace.getInt(2);
        return new IntermediatePlace(tilePat, x, y);
    }


    /**
     * Given an InitialPlace and turns it into a state-pat representation
     * @param initialPlace the InitialPlace
     * @return
     */
    private static JSONArray createJSONArrayInitialPlace(InitialPlace initialPlace) {
        JSONArray initial = new JSONArray();
        JSONArray tilePat = new JSONArray();
        tilePat.put(initialPlace.getTilePat().getIndex());
        tilePat.put(initialPlace.getTilePat().getDegree());
        initial.put(tilePat);
        initial.put(initialPlace.getAvatarColor());
        initial.put(initialPlace.getPort());
        initial.put(initialPlace.getX());
        initial.put(initialPlace.getY());
        return initial;
    }

    /**
     * Given an IntermediatePlace and turns it into a state-pat representation
     * @param intermediatePlace an IntermediatePlace
     * @return
     */
    private static JSONArray createJSONArrayIntermediatePlace(IntermediatePlace intermediatePlace) {
        JSONArray intermediate = new JSONArray();
        JSONArray tilePat = new JSONArray();
        tilePat.put(intermediatePlace.getTilePat().getIndex());
        tilePat.put(intermediatePlace.getTilePat().getDegree());
        intermediate.put(tilePat);
        intermediate.put(intermediatePlace.getX());
        intermediate.put(intermediatePlace.getY());
        return intermediate;
    }
}
