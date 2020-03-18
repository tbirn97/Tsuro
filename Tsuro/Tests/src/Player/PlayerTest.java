package src.Player;

import src.Common.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;
    private Board board;
    private List<Tiles> hand;

    class TestPlayerObserver implements IObserver {

        Board exBoard;
        List<Tiles> exHand;
        Action exAction;

        TestPlayerObserver(Board board, List<Tiles> hand, Action action) {
            this.exBoard = board;
            this.exHand = hand;
            this.exAction = action;
        }

        @Override
        public void notify(ISubject caller) {
            assertTrue("Caller should be a player", caller instanceof PlayerInterface);
            assertNotNull("Board should not be null", caller.getBoard());
            assertNotNull("Hand should not be null", caller.getHand());
            assertNotNull("Action should not be null", caller.getAction());

            assertEquals(exHand, caller.getHand());
            assertEquals(exAction, caller.getAction());
        }
    }

    @Before
    public void init() {
        TilesFactory factory = new TilesFactory();
        player = new Player("Fred", new FirstS());
        board = Board.getInstance();
        hand = Arrays.asList(factory.makeTile(0), factory.makeTile(1), factory.makeTile(2));
    }
}