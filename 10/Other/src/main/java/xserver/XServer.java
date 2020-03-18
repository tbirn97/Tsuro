package xserver;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import src.Admin.CompetitionResult;
import src.Common.PlayerInterface;
import src.Remote.Server;

import xadmin.RankingHandler;
import xadmin.TokenerHandler;
import xclients.XClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs a local server either with the port given in the command line arguments
 * or, if not specified, with the default port of 45678.
 */
public class XServer {

    private static final int DEFAULT_PORT = 45678;

    public static void main(String[] args) {
        int port = getPortArg(args);
        JSONArray input = TokenerHandler.getNextJSONArray(new JSONTokener(System.in));
        List<PlayerInterface> players = XClient.parsePlayerInputArray(input);
        List<String> playerNames = getTrimmedPlayerNames(players);
        CompetitionResult tournamentResult = startServer(port, playerNames).getLexicographicallySortedCompetitionResult();
        JSONObject resultJSON = RankingHandler.competitionResultToJSON(tournamentResult);
        System.out.print(resultJSON);
    }

    /**
     * Get the port number specified in the arguments array or return a default value.
     */
    private static int getPortArg(String[] args) {
        if(args.length > 1) {
            throw new RuntimeException("Too many arguments specified");
        } else if(args.length == 1) {
            String portStr = args[0];
            return Integer.parseInt(portStr);
        } else {
            return DEFAULT_PORT;
        }
    }

    /**
     * Call the server startup method with the given port.
     */
    private static CompetitionResult startServer(int port, List<String> playerNames) {
        try {
            return Server.startServer(port, playerNames);
        } catch (IOException e) {
            throw new IllegalStateException("Server could not start up with an IO exception");
        }
    }

    /**
     * Return a list with maximum size 20 of strings that are the names of the players at
     * the corresponding index in the given list of PlayerInterface.
     */
    private static List<String> getTrimmedPlayerNames(List<PlayerInterface> players) {
        List<String> playerNames = new ArrayList<>();
        for (int i = 0; i < players.size() && i < 20; i++) {
            playerNames.add(players.get(i).getName());
        }
        return playerNames;
    }
}
