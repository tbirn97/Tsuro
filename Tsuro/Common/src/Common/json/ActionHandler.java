package src.Common.json;

import org.json.JSONArray;
import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.AvatarLocation;
import src.Common.Port;
import src.Common.Rotation;
import src.Common.Tiles;
import src.Common.TilesFactory;
import src.Common.json.serialization.TilePat;

public class ActionHandler {

    private static TilesFactory tilesFactory = new TilesFactory();

    /**
     * Given an InitialAction and converts it into it's json representation (initial-place)
     * @param initialAction an InitialAction
     * @return
     */
    public static JSONArray initialActionToJSON(InitialAction initialAction) {
        String port = initialAction.getAvatarPlacement().getPort().name();
        TilePat tile = tileAndRotationToTilePat(initialAction.getTileToPlace(), initialAction.getRotation());
        JSONArray initialActionArray = new JSONArray();
        JSONArray tilePat = constructTilePat(tile.getIndex(), tile.getDegree());
        initialActionArray.put(tilePat);
        initialActionArray.put(port);
        initialActionArray.put(initialAction.getAvatarPlacement().getX());
        initialActionArray.put(initialAction.getAvatarPlacement().getY());
        return initialActionArray;
    }

    /**
     * Given an initialaction and an avatarcolor it converts that into an InitialAction
     * @param jsonInitialAction JSONArray in form [tile-pat, port, index, index]
     * @param playerAvatar AvatarColor
     * @return
     */
    public static InitialAction jsonToInitialAction(JSONArray jsonInitialAction, AvatarColor playerAvatar) {
        TilePat tilePat  = getTilePat((JSONArray)jsonInitialAction.get(0));
        String strport = jsonInitialAction.getString(1);
        int x = jsonInitialAction.getInt(2);
        int y = jsonInitialAction.getInt(3);
        Tiles tile = tilesFactory.makeTile(tilePat.getIndex());
        Port intPort = Port.valueOf(strport);
        Rotation rotation = Rotation.getRotationFromDegrees(tilePat.getDegree());
        AvatarLocation location = new AvatarLocation(x, y, intPort);
        return new InitialAction(playerAvatar, tile,
                rotation, location);
    }

    /**
     * Given a TurnAction object it will convert this into a JSONArray tilepat
     * @param turnAction the turn to make
     * @return
     */
    public static JSONArray turnActionToJSON(TurnAction turnAction) {
        TilePat tilePat = tileAndRotationToTilePat(turnAction.getTileToPlace(), turnAction.getRotation());
        return constructTilePat(tilePat.getIndex(), tilePat.getDegree());
    }

    /**
     * Given a json turnaction object and an avatar it will create a TurnAction
     * @param jsonTurnAction a tile-pat
     * @param avatar an AvatarColor
     * @return
     */
    public static TurnAction jsonToTurnAction(JSONArray jsonTurnAction, AvatarColor avatar) {

        int tileIndex = jsonTurnAction.getInt(0);
        int rotationDegrees = jsonTurnAction.getInt(1);
        Rotation rotation = Rotation.getRotationFromDegrees(rotationDegrees);

        return new TurnAction(avatar, tilesFactory.makeTile(tileIndex),
                rotation);

    }

    private static TilePat tileAndRotationToTilePat(Tiles tile, Rotation rotation) {
        int tileIndex = tilesFactory.getTilePatFromTile(tile).getIndex();
        return new TilePat(tileIndex, rotation.getDegrees());
    }

    /**
     * Given an index and a degree construct a tilePat
     * @param index int
     * @param degree int (0, 90, 180, 270)
     * @return a tilepat
     */
    private static JSONArray constructTilePat(int index, int degree) {
        JSONArray actionPatArray = new JSONArray();
        actionPatArray.put(index);
        actionPatArray.put(degree);
        return actionPatArray;
    }

    /**
     * Given a tilePat JSON object, turn that into a TilePat
     * @param tilePat JSONArray tile-pat
     * @return
     */
    static TilePat getTilePat(JSONArray tilePat) {
        int index = tilePat.getInt(0);
        int degree = tilePat.getInt(1);
        return new TilePat(index, degree);
    }
}
