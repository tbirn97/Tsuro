package harness;

import org.json.JSONException;
import src.Common.AvatarColor;
import src.Common.Connection;
import src.Common.Facing;
import src.Common.Tiles;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TsuroTestHarness {
    protected JSONArray tilesWithIndices;
    protected Map<String, Integer> portAlphaToNumber = new HashMap<>();
    protected Map<Integer, String> portNumberToAlpha = new HashMap<>();

    public TsuroTestHarness() {
        try {
            JSONTokener tokener = new JSONTokener(new FileReader("Other/tsuro-tiles-index.json"));
            this.tilesWithIndices = TokenerHandler.getNextJSONArray(tokener);
        } catch (FileNotFoundException e) {
            System.out.println("Internal error, tsuro-tiles-index.json not found.");
        } catch (JSONException e) {
            System.out.println("tsuro-tiles-index.json invalid json");
        }

        portAlphaToNumber.put("A", 0);
        portAlphaToNumber.put("B", 1);
        portAlphaToNumber.put("C", 2);
        portAlphaToNumber.put("D", 3);
        portAlphaToNumber.put("E", 4);
        portAlphaToNumber.put("F", 5);
        portAlphaToNumber.put("G", 6);
        portAlphaToNumber.put("H", 7);

        portNumberToAlpha.put(0, "A");
        portNumberToAlpha.put(1, "B");
        portNumberToAlpha.put(2, "C");
        portNumberToAlpha.put(3, "D");
        portNumberToAlpha.put(4, "E");
        portNumberToAlpha.put(5, "F");
        portNumberToAlpha.put(6, "G");
        portNumberToAlpha.put(7, "H");
    }

    protected static JSONArray parseInput() {
        JSONTokener tokener = new JSONTokener(System.in);
        JSONArray allInputs = new JSONArray();
        while (tokener.more()) {
            allInputs.put(TokenerHandler.getNextJSONArray(tokener));
        }
        return allInputs.length() == 1 ? allInputs.getJSONArray(0) : allInputs;
    }

    /**
     * Gets a tile given a index value using the read in tsuro-tiles-index.json file.
     * @param index tile index
     * @return Tile object
     */
    protected Tiles getTileFromIndex(int index) {
        JSONObject tileJson = (JSONObject) this.tilesWithIndices.get(index);
        // create connections based on JSON rep
        JSONArray edges = (JSONArray) tileJson.get("edges");
        ArrayList<Connection> connections = new ArrayList<>();

        for (Object edgeObj : edges) {
            JSONArray edge = (JSONArray) edgeObj;
            int a = this.portAlphaToNumber.get(edge.get(0));
            int b = this.portAlphaToNumber.get(edge.get(1));

            connections.add(new Connection(a, b));
        }

        return new Tiles(connections);

    }

    /**
     *
     * @param dir
     * @return
     */
    private int getDirectionFromFacing(Facing dir) {
        switch (dir) {
            case EAST:
                return 90;
            case SOUTH:
                return 180;
            case WEST:
                return 270;
            case NORTH:
            default:
                return 0;
        }
    }

    protected int[] getIndexFromTile(Tiles tile) {
        for (Facing dir : Facing.values()) {
            tile = tile.rotate(dir);
            for (int i = 0; i < this.tilesWithIndices.length(); i++) {
                Tiles otherTile = getTileFromIndex(i);
                if (tile.equals(otherTile)) {
                    int[] vals = new int[2];
                    vals[0] = i;
                    vals[1] = getDirectionFromFacing(dir);
                    return vals;
                }
            }
        }
        return new int[2];
    }

    protected static String parseObjToString(Object obj) {
        //JSONParser jsonParser = new JSONParser();
        return obj.toString();
        /*
        try {

            return jsonParser.parse(obj.toString()).toString();
        }
        catch (ParseException e) {
            System.out.println(obj.toString());
            invalidInput();
            return "";
        }*/
    }

    protected static int parseObjToInt(Object obj) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        } else {
            throw new IllegalArgumentException("Given an object that is not an integer," + obj);
        }
    }


    protected static JSONArray parseObjToArray(Object obj) {
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        } else {
            throw new IllegalArgumentException("Given an object that is not a JSONArray, " + obj);
        }
    }


    protected AvatarColor getAvatarColor(String color) {
        switch(color) {
            case "white":
                return AvatarColor.WHITE;
            case "black":
                return AvatarColor.BLACK;
            case "red":
                return AvatarColor.RED;
            case "green":
                return AvatarColor.GREEN;
            case "blue":
                return AvatarColor.BLUE;
            default:
                return AvatarColor.WHITE;
        }
    }

    protected Facing getFacingFromInt(int rotationVal) {
        switch (rotationVal) {
            case 90:
                return Facing.EAST;
            case 180:
                return Facing.SOUTH;
            case 270:
                return Facing.WEST;
            case 0:
                return Facing.NORTH;
            default:
                return Facing.EAST;
        }
    }

}
