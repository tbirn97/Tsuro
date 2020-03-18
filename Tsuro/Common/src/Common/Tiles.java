package src.Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Tiles {
    private ArrayList<Connection> connections;

    public Tiles(ArrayList<Connection> connections) {
        if (connections.size() != 4) {
            throw new IllegalArgumentException("Connections array is the incorrect size");
        }
        this.connections = connections;
        Collections.sort(this.connections);
    }

    @Override
    public Tiles clone() {
        ArrayList<Connection> connectionsCopy = new ArrayList<>();
        for (Connection c : this.connections) {
            connectionsCopy.add(c.clone());
        }
        return new Tiles(connectionsCopy);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Tiles) {
            Tiles otherTile = (Tiles) other;
            for (int i = 0; i < this.connections.size(); i++) {
                if (!this.connections.get(i).equals(otherTile.connections.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Tiles rotate(Rotation rotation) {
        ArrayList<Connection> temp = new ArrayList<>();
        for (Connection c : this.connections) {
            temp.add(c.getRotatedConnection(rotation));
        }
        Collections.sort(temp);
        return new Tiles(temp);
    }

    /**
     * Gets the port that connects to the given port on this tile.
     */
    public Port getEndOfConnection(Port startingPort) {
        for (Connection c : connections) {
            if (c.hasPort(startingPort)) {
                return c.getEndOfConnection(startingPort);
            }
        }
        throw new IllegalArgumentException(String.format("Tile does not contain a connection for %d%n", startingPort));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Connection c : this.connections) {
            builder.append("[");
            builder.append(c.toString());
            builder.append("]");
        }
        builder.append("]");
        return builder.toString();
    }
}