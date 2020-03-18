package src.Admin;

import src.Common.AvatarColor;
import src.Common.Board;
import src.Common.Rules;
import src.Player.FirstS;
import src.Player.Player;
import src.Common.PlayerInterface;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;

public class RefereeTest {

    private Board board;
    private Rules rules;
    private LinkedHashMap<AvatarColor, PlayerInterface> players;
    private int nextTileIndex;
    private int currentRound;
    private SortedMap<Integer, List<PlayerInterface>> roundLosers;
    private List<PlayerInterface> cheaters;
    private Referee referee;

    private void setup() {
        board = mock(Board.class);
        rules = mock(Rules.class);
        players = new LinkedHashMap<>();
        nextTileIndex = 0;
        currentRound = 0;
        roundLosers = new TreeMap<>();
        cheaters = new ArrayList<>();
        referee = new Referee(board, rules, players, nextTileIndex, currentRound, roundLosers, cheaters);
    }

    @Test
    public void testRemovePlayerInterfaceCheater() {
        setup();
        LinkedHashMap<AvatarColor, PlayerInterface> players = new LinkedHashMap<>();
        PlayerInterface removingPlayer = mock(PlayerInterface.class);
        players.put(AvatarColor.red, removingPlayer);

        referee = new Referee(board, rules, players, nextTileIndex, currentRound, roundLosers, cheaters);

        referee.removePlayerInterface(AvatarColor.red, true);

        assertEquals(1, cheaters.size());
        assertEquals(removingPlayer, cheaters.get(0));

        assertEquals(0, players.size());
    }

    @Test
    public void testRemovePlayerInterfaceNotCheater() {
        setup();
        LinkedHashMap<AvatarColor, PlayerInterface> players = new LinkedHashMap<>();
        PlayerInterface removingPlayer = mock(PlayerInterface.class);
        players.put(AvatarColor.red, removingPlayer);

        referee = new Referee(board, rules, players, nextTileIndex, currentRound, roundLosers, cheaters);

        referee.removePlayerInterface(AvatarColor.red, false);

        assertEquals(0, cheaters.size());
        assertEquals(0, players.size());

        List<PlayerInterface> losers = roundLosers.get(currentRound);
        assertEquals(1, losers.size());
        assertEquals(removingPlayer, losers.get(0));
    }




    @Test
    public void playTest1() {
        Referee r = new Referee();
        Player p1 = new Player("Ann", new FirstS());
        Player p2 = new Player("Bob", new FirstS());
        Player p3 = new Player("Carla", new FirstS());
        List<PlayerInterface> players = Arrays.asList(p1, p2, p3);
        CompetitionResult winners = r.startGame(players);
        assertEquals(1, winners.getRankings().size());
        assertEquals(2, winners.getCheaters().size());
        assertEquals(1, winners.getRankings().get(0).size());

        assertEquals(Collections.singletonList(p1), winners.getRankings().get(0));
        assertEquals(Arrays.asList(p3, p2), winners.getCheaters());
    }

    @Test
    public void playTest2() {
        Referee r = new Referee();
        Player p1 = new Player("Ann", new FirstS());
        Player p2 = new Player("Bob", new FirstS());
        Player p3 = new Player("Carla", new FirstS());
        Player p4 = new Player("Deedee", new FirstS());
        Player p5 = new Player("Eddy", new FirstS());
        List<PlayerInterface> players = Arrays.asList(p1, p2, p3, p4, p5);
        CompetitionResult winners = r.startGame(players);
        assertEquals(1, winners.getRankings().size());
        assertEquals(4, winners.getCheaters().size());
        assertEquals(1, winners.getRankings().get(0).size());

        assertEquals(Collections.singletonList(p1), winners.getRankings().get(0));
        assertEquals(Arrays.asList(p5, p2, p3, p4), winners.getCheaters());
    }

}