package src.Common;

import src.Common.Actions.TurnAction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TurnActionTest {

    private Tiles tile1 = new Tiles(
            new ArrayList<>(Arrays.asList(
                    new Connection(Port.A, Port.B),
                    new Connection(Port.C, Port.D),
                    new Connection(Port.E, Port.F),
                    new Connection(Port.G, Port.H)
            )));
    private Tiles tile2 = new Tiles(
            new ArrayList<>(Arrays.asList(
                    new Connection(Port.A, Port.E),
                    new Connection(Port.B, Port.F),
                    new Connection(Port.C, Port.G),
                    new Connection(Port.D, Port.H)
            )));
    private TurnAction action1 = new TurnAction(AvatarColor.red, tile1, Rotation.R0);

    private TurnAction action2 = new TurnAction(AvatarColor.white, tile2, Rotation.R90);


    @Test
    public void getPlayerAvatar() {
        assertEquals(AvatarColor.red, action1.getPlayerAvatar());
        assertEquals(AvatarColor.white, action2.getPlayerAvatar());
    }

    @Test
    public void getTileToPlace() {
        assertEquals(tile1, action1.getTileToPlace());
        assertEquals(tile2, action2.getTileToPlace());
    }

    @Test
    public void getRotation() {
        assertEquals(Rotation.R0, action1.getRotation());
        assertEquals(Rotation.R90, action2.getRotation());
    }

}