package xadmin;

import org.json.JSONArray;
import org.json.JSONObject;
import src.Admin.CompetitionResult;
import src.Common.PlayerInterface;

import java.util.List;
import java.util.stream.Collectors;

public class RankingHandler {

  /**
   * Converts a competition result into a JSONObject as described as the output
   * of the testing task for <a href="http://www.ccs.neu.edu/~matthias/4500-f19/7.html">Assignment 7</a>
   *
   */
  public static JSONObject competitionResultToJSON(CompetitionResult result) {
    JSONObject resultObject = new JSONObject();

    if (result.getCheaters().size() > 0) {
      JSONArray cheaters = playerListToJSON(result.getCheaters());
      resultObject.put("cheaters", cheaters);
    }
    if (result.getRankings().size() > 0) {
      JSONArray winners = rankingToJSON(result.getRankings());
      resultObject.put("winners", winners);
    }

    return resultObject;
  }

  /**
   * Convert a list of list of PlayerInterface objects into a JSON array of sorted-string-arrays.
   */
  private static JSONArray rankingToJSON(List<List<PlayerInterface>> playerRanking) {
    JSONArray winners = new JSONArray();
    for (int i = 0; i <= playerRanking.size() - 1; ++i) {
      JSONArray sortedPlayerNames = playerListToJSON(playerRanking.get(i));
      winners.put(sortedPlayerNames);
    }
    return winners;
  }

  /**
   * Convert a list of PlayerInterface objects into a sorted JSON array of the player's names.
   */
  private static JSONArray playerListToJSON(List<PlayerInterface> players) {
    JSONArray sortedNames = new JSONArray();
    for (PlayerInterface p : players) {
      sortedNames.put(p.getName());
    }
    return sortedNames;
  }
}
