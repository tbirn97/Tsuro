package src.Common;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ConnectionTest {

    @Test
    public void testBadConnectionConstruction() {
        try {
            new Connection(Port.A, Port.A);
            fail("Expect an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Given identical ports: A"));
        }
    }

    @Test
    public void testGetStart() {
        Connection con1 = new Connection(Port.D, Port.E);
        Connection con2 = new Connection(Port.E, Port.C);
        Connection con3 = new Connection(Port.F, Port.H);
        assertEquals(Port.D, con1.getStart());
        assertEquals(Port.C, con2.getStart());
        assertEquals(Port.F, con3.getStart());
    }

    @Test
    public void testGetEnd() {
        Connection con1 = new Connection(Port.B, Port.D);
        Connection con2 = new Connection(Port.E, Port.C);
        Connection con3 = new Connection(Port.B, Port.H);
        assertEquals(Port.D, con1.getEnd());
        assertEquals(Port.E, con2.getEnd());
        assertEquals(Port.H, con3.getEnd());
    }

    @Test
    public void testGetEndOfConnection() {
        Connection con1 = new Connection(Port.C, Port.E);
        Connection con2 = new Connection(Port.D, Port.E);
        Connection con3 = new Connection(Port.E, Port.C);
        assertEquals(Port.E, con1.getEndOfConnection(Port.C));
        assertEquals(Port.E, con2.getEndOfConnection(Port.D));
        assertEquals(Port.E, con3.getEndOfConnection(Port.C));
    }

    @Test
    public void testGetRotatedConnectionSimple() {
        Connection con1 = new Connection(Port.B, Port.C);

        Connection conRotatedNinty = con1.getRotatedConnection(Rotation.R90);
        assertEquals(Port.D, conRotatedNinty.getStart());
        assertEquals(Port.E, conRotatedNinty.getEnd());

        Connection conRotatedOneEighty = con1.getRotatedConnection(Rotation.R180);
        assertEquals(Port.F, conRotatedOneEighty.getStart());
        assertEquals(Port.G, conRotatedOneEighty.getEnd());

        Connection conRotatedTwoSeventy = con1.getRotatedConnection(Rotation.R270);
        assertEquals(Port.A, conRotatedTwoSeventy.getStart());
        assertEquals(Port.H, conRotatedTwoSeventy.getEnd());
    }

    @Test
    public void testGetRotatedConnectionHard() {
        Connection con2 = new Connection(Port.G, Port.B);

        Connection conRotatedNinty = con2.getRotatedConnection(Rotation.R90);
        assertEquals(Port.A, conRotatedNinty.getStart());
        assertEquals(Port.D, conRotatedNinty.getEnd());

        Connection conRotatedOneEighty = con2.getRotatedConnection(Rotation.R180);
        assertEquals(Port.C, conRotatedOneEighty.getStart());
        assertEquals(Port.F, conRotatedOneEighty.getEnd());

        Connection conRotatedTwoSeventy = con2.getRotatedConnection(Rotation.R270);
        assertEquals(Port.E, conRotatedTwoSeventy.getStart());
        assertEquals(Port.H, conRotatedTwoSeventy.getEnd());
    }

}