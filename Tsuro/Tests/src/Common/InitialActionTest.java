package src.Common;

import src.Common.Actions.InitialAction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class InitialActionTest {

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
    private InitialAction action1 = new InitialAction(AvatarColor.red, tile1, Rotation.R0, new AvatarLocation(0, 0, Port.C));

    private InitialAction action2 = new InitialAction(AvatarColor.white, tile2, Rotation.R90, new AvatarLocation(5, 8, Port.G));

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

    @Test
    public void getAvatarPlacement() {
        assertEquals(new AvatarLocation(0, 0, Port.C), action1.getAvatarPlacement());
        assertEquals(new AvatarLocation(5, 8, Port.G), action2.getAvatarPlacement());
    }
}