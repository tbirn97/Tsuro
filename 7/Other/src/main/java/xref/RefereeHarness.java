package xref;

import harness.TsuroTestHarness;
import src.Admin.CompetitionResult;
import src.Admin.Referee;
import src.Player.FirstS;
import src.Player.Player;
import src.Common.PlayerInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class RefereeHarness extends TsuroTestHarness {


    public static void main(String[] argv) {
        // PLAN:
        // 1. read input array
        // 2. create players for each player name
        // 3. give them to the ref, start game
        // 4. from GameResults, get 'winners' (guessing just first place) and cheaters
        // 5. sort each list based on name string
        // 6. build json obj and return

        JSONArray input = parseInput();

        List<PlayerInterface> players = createPlayers(input);

        Referee referee = new Referee();

        CompetitionResult results = referee.startGame(players);

        printOutput(results);
    }

    private static List<PlayerInterface> createPlayers(JSONArray playerArray) {
        List<PlayerInterface> players = new ArrayList<>();
        for (Object obj : playerArray) {
            String playerName = parseObjToString(obj);
            players.add(new Player(playerName, new FirstS()));
        }
        return players;
    }

    private static void printOutput(CompetitionResult gameResults) {
        List<List<PlayerInterface>> rankings = gameResults.getRankings();
        List<PlayerInterface> cheaters = gameResults.getCheaters();
        // sort cheaters
        cheaters.sort(Comparator.comparing(PlayerInterface::getName));

        JSONArray winners = new JSONArray();
        // rankings are in order by keys (ie 1 is first place, 2 is second place, etc)
        for (List<PlayerInterface> rankedPlayers : rankings) {
            // Sort players by name
            rankedPlayers.sort(Comparator.comparing(PlayerInterface::getName));
            JSONArray thisRank = new JSONArray();
            for (PlayerInterface p : rankedPlayers) {
                thisRank.put(p.getName());
            }
            winners.put(thisRank);
        }

        JSONArray cheaterArray = new JSONArray();
        for (PlayerInterface cheater : cheaters) {
            cheaterArray.put(cheater.getName());
        }

        JSONObject output = new JSONObject();
        output.put("winners", winners);
        output.put("cheaters", cheaterArray);

        System.out.print(output.toString());
    }
}
