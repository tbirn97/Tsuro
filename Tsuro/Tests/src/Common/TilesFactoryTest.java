package src.Common;

import src.Common.Connection;
import src.Common.Tiles;
import src.Common.TilesFactory;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TilesFactoryTest {

    @Test
    public void makeTile() {
        Connection AB = new Connection(Port.A, Port.B);
        Connection CD = new Connection(Port.C, Port.D);
        Connection EF = new Connection(Port.E, Port.F);
        Connection GH = new Connection(Port.G, Port.H);
        ArrayList<Connection> tile34Connections = new ArrayList<>();
        tile34Connections.add(AB);
        tile34Connections.add(CD);
        tile34Connections.add(EF);
        tile34Connections.add(GH);
        Tiles t34 = new Tiles(tile34Connections);

        Connection AE = new Connection(Port.A, Port.E);
        Connection BC = new Connection(Port.B, Port.C);
        Connection DH = new Connection(Port.D, Port.H);
        Connection FG = new Connection(Port.F, Port.G);
        ArrayList<Connection> tile5Connections = new ArrayList<>();
        tile5Connections.add(AE);
        tile5Connections.add(BC);
        tile5Connections.add(DH);
        tile5Connections.add(FG);
        Tiles t5 = new Tiles(tile5Connections);

        Connection AC = new Connection(Port.A, Port.C);
        Connection BF = new Connection(Port.B, Port.F);
        Connection EG = new Connection(Port.E, Port.G);
        ArrayList<Connection> tile16Connections = new ArrayList<>();
        tile16Connections.add(AC);
        tile16Connections.add(BF);
        tile16Connections.add(DH);
        tile16Connections.add(EG);
        Tiles t16 = new Tiles(tile16Connections);

        Connection AD = new Connection(Port.A, Port.D);
        Connection BE = new Connection(Port.B, Port.E);
        Connection CG = new Connection(Port.C, Port.G);
        Connection FH = new Connection(Port.F, Port.H);
        ArrayList<Connection> tile10Connections = new ArrayList<>();
        tile10Connections.add(AD);
        tile10Connections.add(BE);
        tile10Connections.add(CG);
        tile10Connections.add(FH);
        Tiles t10 = new Tiles(tile10Connections);

        TilesFactory tileFac = new TilesFactory();

        assertEquals(tileFac.makeTile(34), t34);
        assertEquals(tileFac.makeTile(5), t5);
        assertEquals(tileFac.makeTile(16), t16);
        assertEquals(tileFac.makeTile(10), t10);

        try {
            tileFac.makeTile(35);
            fail("Expected valid tile index number.");
        } catch (Exception e) {
            assertEquals(e.getMessage(),"Given index is an invalid tile index.");
        }
        try {
            tileFac.makeTile(-1);
            fail("Expected valid tile index number.");
        } catch (Exception e) {
            assertEquals(e.getMessage(),"Given index is an invalid tile index.");
        }
    }
}