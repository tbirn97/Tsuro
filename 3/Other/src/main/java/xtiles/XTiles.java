package xtiles;

import jdk.nashorn.internal.parser.Token;
import org.json.JSONException;
import org.json.JSONTokener;
import src.Common.Connection;
import src.Common.Facing;
import src.Common.Tiles;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class XTiles {

    private JSONArray tilesWithIndicies;
    public Map<String, Integer> portAlphaToNumber = new HashMap<>();
    public Map<Integer, String> portNumberToAlpha = new HashMap<>();

    /**
     * Constructor for the test harness. Reads in the tsuro tiles file.
     */
    public XTiles() {
        try {
            JSONTokener tokener = new JSONTokener(new FileReader("Other/tsuro-tiles-index.json"));
            this.tilesWithIndicies = TokenerHandler.getNextJSONArray(tokener);
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

    /**
     * Gets a tile given a index value using the read in tsuro-tiles-index.json file.
     * @param index tile index
     * @return Tile object
     */
    private Tiles getTileFromIndex(int index) {
        JSONObject tileJson = (JSONObject) this.tilesWithIndicies.get(index);
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

    private static JSONArray parseInput() {
        JSONTokener tokener = new JSONTokener(System.in);
        return TokenerHandler.getNextJSONArray(tokener);
    }


    public static void main(String[] argv) {
        JSONArray input = parseInput();
        if (input.length() != 3) {
            System.out.println("Error! Invalid input");
            System.exit(1);
        }

        int tileIndex = Integer.parseInt(input.get(0).toString());
        int degrees = Integer.parseInt(input.get(1).toString());
        String inPort = input.get(2).toString();

        XTiles instance = new XTiles();

        Facing rotation = degreesToFacing(degrees);
        Tiles tile = instance.getTileFromIndex(tileIndex).rotate(rotation);
        int outPort = tile.getEndOfConnection(instance.portAlphaToNumber.get(inPort));
        System.out.println(
                String.format("[\"if \", \"%s\", \" is the entrance, \", \"%s\", \" is the exit.\"]",
                        inPort,
                        instance.portNumberToAlpha.get(outPort)));

    }

    /**
     * Convert an integer degrees to a Facing enum;
     */
    private static Facing degreesToFacing(int degrees) {
        if (degrees % 90 != 0) {
            throw new IllegalArgumentException(String.format("%d is not a valid rotation amount", degrees));
        }
        int turns = (degrees / 90) % 4;
        return Facing.values()[turns];
    }
}