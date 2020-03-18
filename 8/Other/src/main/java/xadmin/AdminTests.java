package xadmin;

import src.Admin.CompetitionResult;
import src.Admin.administrator;
import src.Player.Player;
import src.Player.Strategy;
import src.Common.PlayerInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class AdminTests {

  public static void main(String[] args) {
    JSONArray input = TokenerHandler.getNextJSONArray(new JSONTokener(System.in));
    List<PlayerInterface> players = parsePlayerInputArray(input);
    administrator admin = new administrator();
    admin.registerPlayers(players);
    CompetitionResult tournamentResult = admin.runTournament().getLexicographicallySortedCompetitionResult();
    JSONObject resultJSON = RankingHandler.competitionResultToJSON(tournamentResult);
    System.out.print(resultJSON);
  }

  /**
   * Return a List of PlayerInterface objects from a JSON array of player JSON representations.
   */
  private static List<PlayerInterface> parsePlayerInputArray(JSONArray array) {
    List<PlayerInterface> players = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      players.add(getPlayerFromObject(array.getJSONObject(i)));
    }
    return players;
  }

  /**
   * Return a PlayerInterface object from a JSON object.
   */
  private static PlayerInterface getPlayerFromObject(JSONObject object) {
    String name = object.getString("name");
    String strategyPath = object.getString("strategy");

    Strategy strategy = StrategyLoader.getStrategyFromFilePath(strategyPath);
    PlayerInterface player = new Player(name, strategy);
    return player;
  }
}
