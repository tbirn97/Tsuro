package src.Common;


public class Connection implements Comparable<Connection> {
    private Port a;
    private Port b;

    public Connection(Port start, Port end) {
        if (start == end) {
            throw new IllegalArgumentException("Given identical ports: " + start);
        }

        if (start.ordinal() < end.ordinal()) {
            this.a = start;
            this.b = end;
        } else {
            this.a = end;
            this.b = start;
        }
    }

    /**
     * Returns the side that this port is on.
     * @param port port number
     * @return Facing enum value
     */
    public static Facing getPortSide(Port port) {
        switch (port) {
            case A:
            case B:
                return Facing.NORTH;
            case C:
            case D:
                return Facing.EAST;
            case E:
            case F:
                return Facing.SOUTH;
            case G:
            case H:
                return Facing.WEST;
            default:
                throw new IllegalArgumentException("Given an unimplemented Port value: " + port);
        }
    }

    @Override
    public int compareTo(Connection other) {
        return this.a.ordinal() - other.getStart().ordinal();
    }

    public Port getStart() {
        return this.a;
    }

    public Port getEnd() {
        return this.b;
    }

    /**
     * Gets the rotated version of this connection. This does not mutate, but returns a new object.
     */
    public Connection getRotatedConnection(Rotation rotation) {
        int rotationIndex = rotation.getDegrees() / 45;
        int newA = (this.a.ordinal() + rotationIndex) % 8;
        int newB = (this.b.ordinal() + rotationIndex) % 8;
        return new Connection(Port.getPortFromInteger(newA), Port.getPortFromInteger(newB));
    }

    @Override
    public String toString() {
        return String.format("Start: %s End: %s", this.a, this.b);
    }

    /**
     * Returns true if the given port is a part of this connection.
     */
    public boolean hasPort(Port port) {
        return port != null && (this.a == port || this.b == port);
    }

    public Port getEndOfConnection(Port start) {
        if (start == this.a) {
            return this.b;
        }
        else if (start == this.b) {
            return this.a;
        }
        else {
            throw new IllegalArgumentException("Given a port that is not in this connection");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Connection) {
            Connection otherConnection = (Connection) other;
            return otherConnection.getStart() == this.a && otherConnection.getEnd() == this.b;
        }
        return false;
    }

    @Override
    public Connection clone() {
        return new Connection(this.a, this.b);
    }
}
