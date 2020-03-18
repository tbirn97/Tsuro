package src.Player;

import src.Common.Actions.TurnAction;
import org.junit.Before;
import org.junit.Test;

import src.Common.AvatarColor;
import src.Common.Board;
import src.Common.Rotation;
import src.Common.Rules;
import src.Common.Tiles;
import src.Common.TilesFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

// NOTE: because SecondS inherits from FirstS, we do not repeat tests for getInitialTurn here
public class SecondSTest {

    private SecondS strat = new SecondS();
    private Rules rules = new Rules();
    private AvatarColor avatar = AvatarColor.blue;
    private TilesFactory factory = new TilesFactory();
    private Board board = new Board.Builder().build();
    private List<Tiles> hand = Arrays.asList(factory.makeTile(0), factory.makeTile(1), factory.makeTile(2));

    @Before
    public void init() {
        rules = new Rules();
    }


    private void checkTurn(Tiles exTile, Rotation expectedRotation) {
        this.init();
        rules.addTurnRule((board, hand, playerMove) ->
                playerMove.getTileToPlace().equals(exTile)
                        && playerMove.getRotation().equals(expectedRotation));

        TurnAction expected = new TurnAction(avatar, exTile, expectedRotation);

        assertEquals(expected, strat.getTurn(avatar, board, hand, rules));
    }

    @Test
    public void checkIntermediateTurns() {
        for (Tiles t : hand) {
            for (Rotation r : Rotation.values()) {
                checkTurn(t, r);
            }
        }
    }

    @Test
    public void getLastTurn() {
        rules.addTurnRule((board, hand, playerMove) -> false);

        TurnAction expected = new TurnAction(avatar, hand.get(0), Rotation.R0);

        assertEquals(expected, strat.getTurn(avatar, board, hand, rules));
    }

}