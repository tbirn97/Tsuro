package src.Remote;

import src.Admin.CompetitionResult;
import src.Admin.administrator;

import java.io.IOException;
import java.util.List;

/**
 * A Tsuro server that can accept players and play Tsuro tournaments.
 */
public class Server {

  /**
   * The number of seconds the server will wait for clients to register themselves.
   */
  private static final int SECONDS_FOR_REGISTRATION = 60;

  /**
   * Start a server with the given port, listen to all players, play a tournament,
   * and return the tournament result.
   *
   * @param port The port the server will listen on.
   * @param playerNames An ordered list of the names of all players that will sign up for
   *                    the game.
   * @return The result of the tournament.
   */
  public static CompetitionResult startServer(int port, List<String> playerNames) throws IOException {
    ServerRegisterer registerer = new ServerRegisterer();
    List<RemoteUser> registeredPlayers = registerer.registerRemoteUsers(port, SECONDS_FOR_REGISTRATION);

    administrator admin = createTournamentAdministrator(playerNames, registeredPlayers);

    return admin.runTournament();
  }

  /**
   * With an ordered list of player names and an ordered list of RemoteUsers, set each player's
   * name to the corresponding name at the same index and register players
   * one at a time with an administrator and return the admin.
   *
   * @throws IllegalArgumentException If the size of playerNames is not equal to the size of
   * remoteUsers.
   */
  private static administrator createTournamentAdministrator(List<String> playerNames, List<RemoteUser> remoteUsers) {
    if (remoteUsers.size() != playerNames.size()) {
      throw new IllegalArgumentException(String.format("Expected %d players to sign up and got: %d",
          playerNames.size(),
          remoteUsers.size()));
    }

    administrator tournamentAdministrator = new administrator();
    for (int i = 0; i < remoteUsers.size(); i++) {
      RemoteUser remoteUser = remoteUsers.get(i);
      remoteUser.setName(playerNames.get(i));
      tournamentAdministrator.registerPlayer(remoteUser);
    }

    return tournamentAdministrator;
  }
}
