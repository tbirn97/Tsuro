package src.Common;

import src.Common.Connection;
import src.Common.Facing;
import src.Common.Tiles;
import org.junit.Before;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TilesTest {

    private Tiles test1;
    private Tiles test2;
    private Tiles test3;
    private ArrayList<Connection> connections3;
    private Tiles brokenTile;

    @Before
    public void init() {
        ArrayList<Connection> connections = new ArrayList<>();
        connections.add(new Connection(Port.A, Port.E));
        connections.add(new Connection(Port.B, Port.F));
        connections.add(new Connection(Port.H, Port.D));
        connections.add(new Connection(Port.G, Port.C));
        this.test1 = new Tiles(connections);

        ArrayList<Connection> connections2 = new ArrayList<>();
        connections2.add(new Connection(Port.A, Port.B));
        connections2.add(new Connection(Port.C, Port.D));
        connections2.add(new Connection(Port.E, Port.F));
        connections2.add(new Connection(Port.G, Port.H));
        this.test2 = new Tiles(connections2);

        this.connections3 = new ArrayList<>();
        connections3.add(new Connection(Port.A, Port.H));
        connections3.add(new Connection(Port.B, Port.E));
        connections3.add(new Connection(Port.C, Port.F));
        connections3.add(new Connection(Port.D, Port.G));
        this.test3 = new Tiles(connections3);

        ArrayList<Connection> brokenConnections = new ArrayList<>();
        brokenConnections.add(new Connection(Port.A, Port.B));
        brokenConnections.add(new Connection(Port.A, Port.B));
        brokenConnections.add(new Connection(Port.A, Port.B));
        brokenConnections.add(new Connection(Port.A, Port.B));
        this.brokenTile = new Tiles(brokenConnections);
    }

    @Test
    public void invalidConstructorArguments() {
        try {
            ArrayList<Connection> c = new ArrayList<>();
            new Tiles(c);
            fail("Expecting fail because lack of connections passed through.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Connections array is the incorrect size"));
        }
        try {
            ArrayList<Connection> c = new ArrayList<>();
            c.add(new Connection(Port.D, Port.E));
            new Tiles(c);
            fail("Expecting fail because lack of connections passed through.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Connections array is the incorrect size"));
        }
    }

    @Test
    public void rotate() {

        Tiles test1Rotate90 = this.test1.rotate(Rotation.R90);
        assertNotSame(this.test1, test1Rotate90);

        ArrayList<Connection> connections = new ArrayList<>();
        connections.add(new Connection(Port.A, Port.F));
        connections.add(new Connection(Port.B, Port.C));
        connections.add(new Connection(Port.D, Port.G));
        connections.add(new Connection(Port.E, Port.H));
        Tiles test3Expected90 = new Tiles(connections);
        assertEquals(test3Expected90.toString(), this.test3.rotate(Rotation.R90).toString());
    }

    @Test
    public void getEndOfConnection() throws InvalidObjectException {
        assertEquals(Port.E, this.test1.getEndOfConnection(Port.A));
        assertEquals(Port.A, this.test1.getEndOfConnection(Port.E));
        assertEquals(Port.F, this.test2.getEndOfConnection(Port.E));
    }


    @Test(expected = IllegalArgumentException.class)
    public void invalidTileStructureGetEndOfConnection() {
        this.brokenTile.getEndOfConnection(Port.E);
    }

    @Test
    public void generateAllPossibleTiles() {
    }
}