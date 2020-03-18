package src.Common;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import src.Common.json.serialization.TilePat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Knows how to construct Tiles objects from their index and vice-versa.
 */
public class TilesFactory {

    private static Map<Integer, Tiles> allTiles;

    /**
     * Create a TilesFactory object that has a valid allTiles map.
     */
    public TilesFactory() {
        if (TilesFactory.allTiles == null) {
            initializeTileMap();
        }
    }

    /**
     * Return an ArrayList of all the possible Tiles.
     */
    public ArrayList<Tiles> getAllTiles() {
        ArrayList<Tiles> generatedTiles = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            generatedTiles.add(this.makeTile(i));
        }
        return generatedTiles;
    }

    /**
     * Return a tile that corresponds to the given tile-index.
     */
    public Tiles makeTile(int index) {
        if (index < 0 || index >= allTiles.size()) {
            throw new IllegalArgumentException("Given index is an invalid tile index.");
        } else {
            return allTiles.get(index);
        }
    }

    /**
     * Returns a TilePat that describes a given tile. If a tile has rotational symmetry, the lowest
     * valid rotation degrees will be returned.
     */
    public TilePat getTilePatFromTile(Tiles tile) {
        for (Integer index : allTiles.keySet()) {
            Tiles maybeTile = allTiles.get(index);
            for (Rotation rotation : Rotation.values()) {
                if (tile.equals(maybeTile.rotate(rotation))) {
                    return new TilePat(index, rotation.getDegrees());
                }
            }
        }
        throw new IllegalArgumentException("Given a tile that is not represented in the all tiles map.");
    }

    /**
     * Initialize the map from tile index to tile object using the tsuro-tiles-index.json
     * resource file as a definition of tiles.
     */
    private void initializeTileMap() {
        try (InputStream inputStream = TilesFactory.class.getClassLoader()
            .getResourceAsStream("tsuro-tiles-index.json")) {

            if (inputStream == null) {
                throw new IllegalStateException("Input stream couldn't be read properly");
            }

            JSONTokener tokener = new JSONTokener(inputStream);
            TilesFactory.allTiles = parseJSONList(tokener);
        } catch (IOException e) {
            throw new IllegalStateException("The tsuro tile defining file could not be found", e);
        }
    }

    /**
     * With a JSONTokener that has a list of Tile-Pat objects as defined in the
     * Tsuro tiles assignment page, return a map from tile index to a corresponding
     * Tiles object.
     */
    private Map<Integer, Tiles> parseJSONList(JSONTokener tokener) {
        Map<Integer, Tiles> tileMap = new HashMap<>();
        while (tokener.more()) {
            Object nextObject = tokener.nextValue();
            if (nextObject instanceof JSONObject) {
                JSONObject nextJSONTile = (JSONObject) nextObject;
                Integer index = nextJSONTile.getInt("tile#");
                JSONArray edges = nextJSONTile.getJSONArray("edges");
                Tiles thisTile = createTilesFromJSONEdges(edges);

                tileMap.put(index, thisTile);
            }
        }
        return tileMap;
    }

    /**
     * Given a JSONArray of edges within a tile, return a corresponding Tiles object.
     */
    private Tiles createTilesFromJSONEdges(JSONArray edges) {
        ArrayList<Connection> connections = new ArrayList<>();
        for (int i = 0; i < edges.length(); i++) {
            JSONArray edge = edges.getJSONArray(i);

            Port connectionStart = Port.valueOf(edge.getString(0));
            Port connectionEnd = Port.valueOf(edge.getString(1));
            Connection connection = new Connection(connectionStart, connectionEnd);
            connections.add(connection);
        }

        return new Tiles(connections);
    }
}


