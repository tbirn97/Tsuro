package src.Admin;

import src.Player.FirstS;
import src.Player.Player;
import src.Common.PlayerInterface;
import src.Player.SecondS;
import src.Player.ThirdS;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class administratorTest {

    @Test
    public void registerPlayer() {
        Player p1 = new Player("George", new FirstS());
        Player p2 = new Player("Margot", new FirstS());

        administrator admin = new administrator();

        admin.registerPlayer(p1);
        admin.registerPlayer(p2);

        List<PlayerInterface> playerList1 = new ArrayList<>();
        playerList1.add(p1);
        playerList1.add(p2);

        assertEquals(admin.getPlayersLeft(), playerList1);

        administrator admin2 = new administrator();
        admin2.registerPlayers(playerList1);

        assertEquals(admin2.getPlayersLeft(), playerList1);
    }

    @Test
    public void testThreePlayersSecondS() {
        Player p1 = new Player("a", new SecondS());
        Player p2 = new Player("b", new SecondS());
        Player p3 = new Player("c", new SecondS());

        administrator admin = new administrator();

        admin.registerPlayer(p1);
        admin.registerPlayer(p2);
        admin.registerPlayer(p3);
        List<List<PlayerInterface>> results = new ArrayList<>();
        List<PlayerInterface> cheaters = new ArrayList<>();
        List<PlayerInterface> first = new ArrayList<>();
        first.add(p2);
        results.add(first);
        List<PlayerInterface> second = new ArrayList<>();
        second.add(p1);
        results.add(second);
        List<PlayerInterface> third = new ArrayList<>();
        third.add(p3);
        results.add(third);

        CompetitionResult expected = new CompetitionResult(results, cheaters);
        CompetitionResult actual = admin.runTournament();
        assertEquals(expected.getCheaters(), actual.getLexicographicallySortedCompetitionResult().getCheaters());
        assertEquals(expected.getRankings(), actual.getLexicographicallySortedCompetitionResult().getRankings());
    }

    @Test
    public void testThreePlayersThirdS() {
        Player a = new Player("a", new ThirdS());
        Player b = new Player("b", new ThirdS());
        Player c = new Player("c", new ThirdS());

        administrator admin = new administrator();

        admin.registerPlayer(a);
        admin.registerPlayer(b);
        admin.registerPlayer(c);
        List<List<PlayerInterface>> results = new ArrayList<>();
        List<PlayerInterface> cheaters = new ArrayList<>();
        List<PlayerInterface> first = new ArrayList<>();
        first.add(c);
        results.add(first);
        List<PlayerInterface> second = new ArrayList<>();
        second.add(a);
        results.add(second);
        List<PlayerInterface> third = new ArrayList<>();
        third.add(b);
        results.add(third);

        CompetitionResult expected = new CompetitionResult(results, cheaters);
        CompetitionResult actual = admin.runTournament();
        assertEquals(expected.getCheaters(), actual.getLexicographicallySortedCompetitionResult().getCheaters());
        assertEquals(expected.getRankings(), actual.getLexicographicallySortedCompetitionResult().getRankings());
    }

    @Test
    public void testFourPlayersThirdS() {
        Player a = new Player("a", new ThirdS());
        Player b = new Player("b", new ThirdS());
        Player c = new Player("c", new ThirdS());
        Player d = new Player("d", new ThirdS());

        administrator admin = new administrator();

        admin.registerPlayer(a);
        admin.registerPlayer(b);
        admin.registerPlayer(c);
        admin.registerPlayer(d);
        List<List<PlayerInterface>> results = new ArrayList<>();
        List<PlayerInterface> cheaters = new ArrayList<>();
        List<PlayerInterface> first = new ArrayList<>();
        first.add(a);
        first.add(d);
        results.add(first);
        List<PlayerInterface> second = new ArrayList<>();
        second.add(b);
        second.add(c);
        results.add(second);

        CompetitionResult expected = new CompetitionResult(results, cheaters);
        CompetitionResult actual = admin.runTournament();
        assertEquals(expected.getCheaters(), actual.getLexicographicallySortedCompetitionResult().getCheaters());
        assertEquals(expected.getRankings(), actual.getLexicographicallySortedCompetitionResult().getRankings());
    }

    @Test
    public void testFivePlayersThirdS() {
        Player a = new Player("a", new ThirdS());
        Player b = new Player("b", new ThirdS());
        Player c = new Player("c", new ThirdS());
        Player d = new Player("d", new ThirdS());
        Player e = new Player("e", new ThirdS());


        administrator admin = new administrator();

        admin.registerPlayer(a);
        admin.registerPlayer(b);
        admin.registerPlayer(c);
        admin.registerPlayer(d);
        admin.registerPlayer(e);
        List<List<PlayerInterface>> results = new ArrayList<>();
        List<PlayerInterface> cheaters = new ArrayList<>();
        List<PlayerInterface> first = new ArrayList<>();
        first.add(a);
        first.add(b);
        results.add(first);
        List<PlayerInterface> second = new ArrayList<>();
        second.add(d);
        results.add(second);
        List<PlayerInterface> third = new ArrayList<>();
        third.add(c);
        results.add(third);
        cheaters.add(e);

        CompetitionResult expected = new CompetitionResult(results, cheaters);
        CompetitionResult actual = admin.runTournament();
        assertEquals(expected.getCheaters(), actual.getLexicographicallySortedCompetitionResult().getCheaters());
        assertEquals(expected.getRankings(), actual.getLexicographicallySortedCompetitionResult().getRankings());
    }

    @Test
    public void testThreePlayersDifferentStrategies() {
        Player a = new Player("a", new FirstS());
        Player b = new Player("b", new SecondS());
        Player c = new Player("c", new ThirdS());

        administrator admin = new administrator();

        admin.registerPlayer(a);
        admin.registerPlayer(b);
        admin.registerPlayer(c);
        List<List<PlayerInterface>> results = new ArrayList<>();
        List<PlayerInterface> cheaters = new ArrayList<>();
        List<PlayerInterface> first = new ArrayList<>();
        first.add(c);
        results.add(first);
        List<PlayerInterface> second = new ArrayList<>();
        second.add(b);
        results.add(second);
        cheaters.add(a);


        CompetitionResult expected = new CompetitionResult(results, cheaters);
        CompetitionResult actual = admin.runTournament();
        assertEquals(expected.getCheaters(), actual.getLexicographicallySortedCompetitionResult().getCheaters());
        assertEquals(expected.getRankings(), actual.getLexicographicallySortedCompetitionResult().getRankings());
    }


    @Test
    public void motherOfAllTests() {
        /**
         * '((#hasheq((name . "a") (strategy . "../Tsuro/Player/src/Player/FirstS.java"))
         *    #hasheq((name . "g") (strategy . "../Tsuro/Player/src/Player/SecondS.java"))
         *    #hasheq((name . "m") (strategy . "../Tsuro/Player/src/Player/ThirdS.java"))
         *    #hasheq((name . "b") (strategy . "../Tsuro/Player/src/Player/FirstS.java"))
         *    #hasheq((name . "h") (strategy . "../Tsuro/Player/src/Player/SecondS.java"))
         *    #hasheq((name . "n") (strategy . "../Tsuro/Player/src/Player/ThirdS.java"))
         *    #hasheq((name . "c") (strategy . "../Tsuro/Player/src/Player/FirstS.java"))
         *    #hasheq((name . "i") (strategy . "../Tsuro/Player/src/Player/SecondS.java"))
         *    #hasheq((name . "o") (strategy . "../Tsuro/Player/src/Player/ThirdS.java"))
         *    #hasheq((name . "d") (strategy . "../Tsuro/Player/src/Player/FirstS.java"))
         *    #hasheq((name . "j") (strategy . "../Tsuro/Player/src/Player/SecondS.java"))
         *    #hasheq((name . "p") (strategy . "../Tsuro/Player/src/Player/ThirdS.java"))
         *    #hasheq((name . "e") (strategy . "../Tsuro/Player/src/Player/FirstS.java"))
         *    #hasheq((name . "k") (strategy . "../Tsuro/Player/src/Player/SecondS.java"))
         *    #hasheq((name . "q") (strategy . "../Tsuro/Player/src/Player/ThirdS.java"))
         *    #hasheq((name . "f") (strategy . "../Tsuro/Player/src/Player/FirstS.java"))
         *    #hasheq((name . "l") (strategy . "../Tsuro/Player/src/Player/SecondS.java"))
         *    #hasheq((name . "r")
         *            (strategy . "../Tsuro/Player/src/Player/ThirdS.java"))))
         */
        Player a = new Player("a", new FirstS());
        Player g = new Player("g", new SecondS());
        Player m = new Player("m", new ThirdS());
        Player b = new Player("b", new FirstS());
        Player h = new Player("h", new SecondS());
        Player n = new Player("n", new ThirdS());
        Player c = new Player("c", new FirstS());
        Player i = new Player("i", new SecondS());
        Player o = new Player("o", new ThirdS());
        Player d = new Player("d", new FirstS());
        Player j = new Player("j", new SecondS());
        Player p = new Player("p", new ThirdS());
        Player e = new Player("e", new FirstS());
        Player k = new Player("k", new SecondS());
        Player q = new Player("q", new ThirdS());
        Player f = new Player("f", new FirstS());
        Player l = new Player("l", new SecondS());
        Player r = new Player("r", new ThirdS());

        administrator admin = new administrator();
        admin.registerPlayer(a);
        admin.registerPlayer(g);
        admin.registerPlayer(m);
        admin.registerPlayer(b);
        admin.registerPlayer(h);
        admin.registerPlayer(n);
        admin.registerPlayer(c);
        admin.registerPlayer(i);
        admin.registerPlayer(o);
        admin.registerPlayer(d);
        admin.registerPlayer(j);
        admin.registerPlayer(p);
        admin.registerPlayer(e);
        admin.registerPlayer(k);
        admin.registerPlayer(q);
        admin.registerPlayer(f);
        admin.registerPlayer(l);
        admin.registerPlayer(r);


        List<List<PlayerInterface>> results = new ArrayList<>();
        List<PlayerInterface> cheaters = new ArrayList<>();
        List<PlayerInterface> first = new ArrayList<>();
        first.add(o);
        results.add(first);
        List<PlayerInterface> second = new ArrayList<>();
        second.add(r);
        results.add(second);
        List<PlayerInterface> third = new ArrayList<>();
        third.add(l);
        third.add(n);
        results.add(third);
        cheaters.add(a);
        cheaters.add(b);
        cheaters.add(c);
        cheaters.add(d);
        cheaters.add(f);


        CompetitionResult expected = new CompetitionResult(results, cheaters);
        CompetitionResult actual = admin.runTournament();
        assertEquals(expected.getCheaters(), actual.getLexicographicallySortedCompetitionResult().getCheaters());
        assertEquals(expected.getRankings(), actual.getLexicographicallySortedCompetitionResult().getRankings());

    }


    @Test
    public void test12SecondS() {

        Player player1 = new Player("player1", new SecondS());
        Player player2 = new Player("player2", new SecondS());
        Player player3 = new Player("player3", new SecondS());
        Player player4 = new Player("player4", new SecondS());
        Player player5 = new Player("player5", new SecondS());
        Player player6 = new Player("player6", new SecondS());
        Player player7 = new Player("player7", new SecondS());
        Player player8 = new Player("player8", new SecondS());
        Player player9 = new Player("player9", new SecondS());
        Player player10 = new Player("player10", new SecondS());
        Player player11 = new Player("player11", new SecondS());
        Player player12 = new Player("player12", new SecondS());

        administrator admin = new administrator();
        admin.registerPlayer(player1);
        admin.registerPlayer(player2);
        admin.registerPlayer(player3);
        admin.registerPlayer(player4);
        admin.registerPlayer(player5);
        admin.registerPlayer(player6);
        admin.registerPlayer(player7);
        admin.registerPlayer(player8);
        admin.registerPlayer(player9);
        admin.registerPlayer(player10);
        admin.registerPlayer(player11);
        admin.registerPlayer(player12);

        List<List<PlayerInterface>> results = new ArrayList<>();
        List<PlayerInterface> cheaters = new ArrayList<>();
        List<PlayerInterface> first = new ArrayList<>();
        first.add(player1);
        first.add(player3);
        results.add(first);
        List<PlayerInterface> second = new ArrayList<>();
        second.add(player2);
        results.add(second);
        List<PlayerInterface> third = new ArrayList<>();
        third.add(player9);
        results.add(third);
        List<PlayerInterface> fourth = new ArrayList<>();
        fourth.add(player10);
        results.add(fourth);

        CompetitionResult expected = new CompetitionResult(results, cheaters);
        CompetitionResult actual = admin.runTournament();
        assertEquals(expected.getCheaters(), actual.getLexicographicallySortedCompetitionResult().getCheaters());
        assertEquals(expected.getRankings(), actual.getLexicographicallySortedCompetitionResult().getRankings());

    }




    @Test
    public void firstTestMichael() {
        Player p1 = new Player("a", new SecondS());
        Player p2 = new Player("b", new ThirdS());
        Player p3 = new Player("c", new FirstS());
        Player p4 = new Player("d", new FirstS());
        administrator admin = new administrator();
        admin.registerPlayer(p1);
        admin.registerPlayer(p2);
        admin.registerPlayer(p3);
        admin.registerPlayer(p4);

        CompetitionResult result = admin.runTournament();

    }

    /*
     * This Test was removed because, based on the commented out prints and the general unintuitiveness of its design,
     * it was created by running the admin, seeing what was returned, and then changing the asserts to match what
     * the code actually does. This completely defeats the purpose of unit testing and breaks when the code
     * is changed to fix logic bugs and will have to be reimplemented with a test that doesn't rely on the code
     * implementation.
     */
//    @Test
//    public void runTournament() {
//        Player p1 = new Player("George", new FirstS());
//        Player p2 = new Player("Margot", new FirstS());
//        Player p3 = new Player("Frank", new FirstS());
//        Player p4 = new Player("Sam", new FirstS());
//        Player p5 = new Player("Hank", new FirstS());
//        Player p6 = new Player("Ben", new FirstS());
//        Player p7 = new Player("Sarah", new FirstS());
//        Player p8 = new Player("Tim", new FirstS());
//        Player p9 = new Player("Ashley", new FirstS());
//        Player p10 = new Player("Alexa", new FirstS());
//        Player p11 = new Player("Thomas", new FirstS());
//        Player p12 = new Player("Daniel", new FirstS());
//        Player p13 = new Player("Mary", new FirstS());
//        Player p14 = new Player("Katie", new FirstS());
//
//        administrator admin1 = new administrator();
//        admin1.registerPlayer(p1);
//        admin1.registerPlayer(p2);
//        admin1.registerPlayer(p3);
//        admin1.registerPlayer(p4);
//        admin1.registerPlayer(p5);
//        admin1.registerPlayer(p6);
//        admin1.registerPlayer(p7);
//        admin1.registerPlayer(p8);
//        admin1.registerPlayer(p9);
//        admin1.registerPlayer(p10);
//        admin1.registerPlayer(p11);
//        admin1.registerPlayer(p12);
//        admin1.registerPlayer(p13);
//        admin1.registerPlayer(p14);
////        System.out.println("[test with 14]: " + admin1.runTournament());
//        ArrayList<PlayerInterface> test14List = new ArrayList<>();
//        Player sam = new Player("Sam", new FirstS());
//        sam.welcome(new ArrayList<>(), AvatarColor.BLACK, new Rules());
//        test14List.add(sam);
//        List<PlayerInterface> t1 = admin1.runTournament();
//        assertEquals(t1.get(0).toString(), test14List.get(0).toString());
//        assertEquals(t1.size(), test14List.size());
//
//        administrator admin2 = new administrator();
//        admin2.registerPlayer(p1);
//        admin2.registerPlayer(p2);
//        admin2.registerPlayer(p3);
//        admin2.registerPlayer(p4);
//        admin2.registerPlayer(p5);
//        admin2.registerPlayer(p6);
//        admin2.registerPlayer(p7);
//        admin2.registerPlayer(p8);
//        admin2.registerPlayer(p9);
//        admin2.registerPlayer(p10);
//        admin2.registerPlayer(p11);
//        System.out.println("[test with 11]: " + admin2.runTournament());
//        List<PlayerInterface> t2 = admin2.runTournament();
//        assertEquals(t2.get(0).toString(), test14List.get(0).toString());
//        assertEquals(t2.size(), test14List.size());
//
//        administrator admin3 = new administrator();
//        admin3.registerPlayer(p1);
//        admin3.registerPlayer(p2);
//        admin3.registerPlayer(p3);
//        admin3.registerPlayer(p4);
//        admin3.registerPlayer(p5);
////        System.out.println("[test with 5]: " + admin3.runTournament());
//        ArrayList<PlayerInterface> test5List = new ArrayList<>();
//        Player george = new Player("George", new FirstS());
//        george.welcome(new ArrayList<>(), AvatarColor.WHITE, new Rules());
//        test5List.add(george);
//        List<PlayerInterface> t3 = admin3.runTournament();
//        assertEquals(t3.get(0).toString(), test5List.get(0).toString());
//        assertEquals(t3.size(), test5List.size());
//
//        administrator admin4 = new administrator();
//        admin4.registerPlayer(p1);
//        admin4.registerPlayer(p2);
////        System.out.println("[test with 2]: " + admin4.runTournament());
//        ArrayList<PlayerInterface> test2List = new ArrayList<>();
//        Player margot = new Player("Margot", new FirstS());
//        margot.welcome(new ArrayList<>(), AvatarColor.BLACK, new Rules());
//        test2List.add(george);
//        test2List.add(margot);
//        List<PlayerInterface> t4 = admin4.runTournament();
//        assertEquals(t4.get(0).toString(), test2List.get(0).toString());
//        assertEquals(t4.size(), test2List.size());
//
//        administrator admin5 = new administrator();
////        System.out.println("[test with 0]: " + admin5.runTournament());
//        ArrayList<PlayerInterface> test0List = new ArrayList<>();
//        List<PlayerInterface> t5 = admin5.runTournament();
//        assertEquals(t5.size(), test0List.size());
//    }

}