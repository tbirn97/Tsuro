package xclients;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import src.Common.LostConnectionException;
import src.Common.PlayerInterface;
import src.Player.Player;
import src.Player.Strategy;
import src.Remote.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import src.Remote.RemoteAdmin;
import xadmin.StrategyLoader;
import xadmin.TokenerHandler;

public class XClient {

    /**
     * The maximum number of minutes a client will be kept open. If a tsuro tournament goes
     * longer than this time the client will close.
     */
    private static final int MINUTES_KEEP_OPEN = 5;

    public static void main(String[] args) {
        HostPortType soc = parseArgs(args);
        JSONArray input = TokenerHandler.getNextJSONArray(new JSONTokener(System.in));
        List<PlayerInterface> players = parsePlayerInputArray(input);
        loadPlayers(players, soc.getHost(), soc.getPort());
    }

    /**
     * Parse arguments into a type that can store a host and a port
     * @param args passed from main
     * @return
     */
    private static HostPortType parseArgs(String[] args) {
        int port;
        String host;
        if(args.length > 2) {
            throw new RuntimeException("Too many arguments specified");
        } else if(args.length == 2) {
            host = args[0];
            String portStr = args[1];
            port = Integer.parseInt(portStr);
        } else if(args.length == 1) {
            throw new RuntimeException("You must specify two or no arguments");
        } else {
            host = "127.0.0.1";
            port = 45678;
        }
        return new HostPortType(host, port);
    }


    /**
     * Return a List of PlayerInterface objects from a JSON array of player JSON representations.
     */
    public static List<PlayerInterface> parsePlayerInputArray(JSONArray array) {
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
        return new Player(name, strategy);
    }

    /**
     * For each player in players, start a client with the given ip and port
     */
    private static void loadPlayers(List<PlayerInterface> players, String host, int port) {
        ExecutorService clientExecutor = Executors.newFixedThreadPool(players.size());
        for (PlayerInterface player : players) {
            try {
                RemoteAdmin playerAdmin = Client.getClient(host, port, player);
                clientExecutor.submit(playerAdmin::listen);
            } catch (IOException e) {
                throw new LostConnectionException(e);
            }
        }
        clientExecutor.shutdown();

        try {
            clientExecutor.awaitTermination(MINUTES_KEEP_OPEN, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
