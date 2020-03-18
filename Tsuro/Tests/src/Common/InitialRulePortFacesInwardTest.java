package src.Common;

import src.Common.Actions.InitialAction;
import src.Common.Rule.InitialRule;
import src.Common.Rule.InitialRulePortFacesInward;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class InitialRulePortFacesInwardTest {

    private Board testBoard;
    private InitialRule rule = new InitialRulePortFacesInward();
    private Random randomizer = new Random();
    private int boardSize = 10;

    @Before
    public void createEmptyBoard() {
        testBoard = new Board.Builder().build();
    }

    private boolean isValid(Posn location, Port p) {
        return rule.isValidMove(this.testBoard, null, new InitialAction(AvatarColor.green, null,
            Rotation.R0, new AvatarLocation(location, p)));
    }

    private int getRandomEdgeNum() {
        return 1 + randomizer.nextInt(8);

    }

    private void checkPorts(List<Integer> ports, Posn loc, boolean expected) {
        for (Integer portInteger : ports) {
            Port port = Port.getPortFromInteger(portInteger);
            assertEquals(
                    String.format("Failed on: Posn(%d, %d) port = %s", loc.getX(), loc.getY(), port),
                    expected, this.isValid(loc, port));
        }
    }

    @Test
    public void isValidMoveTopLeft() {
        Posn topLeft = new Posn(0, 0);

        List<Integer> validPorts = Arrays.asList(2, 3, 4, 5);
        this.checkPorts(validPorts, topLeft, true);

        List<Integer> invalidPorts = Arrays.asList(0, 1, 6, 7);
        this.checkPorts(invalidPorts, topLeft, false);
    }

    @Test
    public void isValidMoveTopRight() {
        Posn topRight = new Posn(boardSize - 1, 0);

        List<Integer> validPorts = Arrays.asList(4, 5, 6, 7);
        this.checkPorts(validPorts, topRight, true);

        List<Integer> invalidPorts = Arrays.asList(0, 1, 2, 3);
        this.checkPorts(invalidPorts, topRight, false);
    }

    @Test
    public void isValidMoveBottomLeft() {
        Posn bottomLeft = new Posn(0, boardSize - 1);

        List<Integer> validPorts = Arrays.asList(0, 1, 2, 3);
        this.checkPorts(validPorts, bottomLeft, true);

        List<Integer> invalidPorts = Arrays.asList(4, 5, 6, 7);
        this.checkPorts(invalidPorts, bottomLeft, false);

    }

    @Test
    public void isValidMoveBottomRight() {
        Posn bottomRight = new Posn(boardSize - 1, boardSize - 1);

        List<Integer> validPorts = Arrays.asList(0, 1, 6, 7);
        this.checkPorts(validPorts, bottomRight, true);

        List<Integer> invalidPorts = Arrays.asList(2, 3, 4, 5);
        this.checkPorts(invalidPorts, bottomRight, false);
    }

    @Test
    public void isValidMoveTop() {
        List<Integer> validPorts = Arrays.asList(2, 3, 4, 5, 6, 7);
        this.checkPorts(validPorts, new Posn(this.getRandomEdgeNum(), 0), true);

        List<Integer> invalidPorts = Arrays.asList(0, 1);
        this.checkPorts(invalidPorts, new Posn(this.getRandomEdgeNum(), 0), false);
    }


    @Test
    public void isValidMoveLeft() {
        List<Integer> validPorts = Arrays.asList(0, 1, 2, 3, 4, 5);
        this.checkPorts(validPorts, new Posn(0, this.getRandomEdgeNum()), true);

        List<Integer> invalidPorts = Arrays.asList(6, 7);
        this.checkPorts(invalidPorts, new Posn(0, this.getRandomEdgeNum()), false);
    }

    @Test
    public void isValidMoveBottom() {
        List<Integer> validPorts = Arrays.asList(0, 1, 2, 3, 6, 7);
        this.checkPorts(validPorts, new Posn(this.getRandomEdgeNum(), boardSize - 1), true);

        List<Integer> invalidPorts = Arrays.asList(4, 5);
        this.checkPorts(invalidPorts, new Posn(this.getRandomEdgeNum(), boardSize - 1), false);
    }

    @Test
    public void isValidMoveRight() {
        List<Integer> validPorts = Arrays.asList(0, 1, 4, 5, 6, 7);
        this.checkPorts(validPorts, new Posn(boardSize - 1, this.getRandomEdgeNum()), true);

        List<Integer> invalidPorts = Arrays.asList(2, 3);
        this.checkPorts(invalidPorts, new Posn(boardSize - 1, this.getRandomEdgeNum()), false);
    }
}