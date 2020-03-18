package src.Remote;

import src.Common.LostConnectionException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A registerer which will begin a remote Tsuro admin server and accept incoming
 * player TCP connections for a set period of time and return all connected players.
 */
public class ServerRegisterer {
  private AtomicBoolean registrationOpen = new AtomicBoolean(true);

  /**
   * Begin a server at the given port and accept incoming connections for the given
   * number of seconds. Once the time period is elapsed or more then 20 players have
   * registered, this will return the list of registered users.
   */
  public List<RemoteUser> registerRemoteUsers(int port, int secondsRegistrationOpen) throws IOException {
    List<RemoteUser> playersList = Collections.synchronizedList(new ArrayList<>());
    ServerSocket serverSocket = new ServerSocket(port);

    long lastTime = System.currentTimeMillis();
    long left = 1000 * secondsRegistrationOpen;
    while (registrationOpen.get() && left > 0) {
      CompletableFuture<Socket> socketFuture = acceptAConnection(serverSocket);

      // This will block until a TCP connection is made
      getConnectionAndRegister(socketFuture, left, playersList);

      left = left - (System.currentTimeMillis() - lastTime);
      lastTime = System.currentTimeMillis();
    }

    return playersList;
  }

  /**
   * Wait for a single TCP connection for the given number of milliseconds and
   * register the first accepted connection to the playersList.
   */
  private void getConnectionAndRegister(CompletableFuture<Socket> socketFuture, long timeout, List<RemoteUser> playersList) {
    try {
      Socket connectionSocket = socketFuture.get(timeout, TimeUnit.MILLISECONDS);
      if (playersList.size() < 20) {
        RemoteUser newUser = new RemoteUser(connectionSocket);
        playersList.add(newUser);
      } else {
        registrationOpen.set(false);
        connectionSocket.close();
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace(); // This should not happen
    } catch (TimeoutException e) {
      // We're out of time, this is normal behavior
    } catch (IOException e) {
      // The connection close threw an error but we don't care
    }
  }

  /**
   * Return a future containing a socket for a created connection.
   */
  private CompletableFuture<Socket> acceptAConnection(ServerSocket serverSocket) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return serverSocket.accept();
      } catch (IOException e) {
        throw new LostConnectionException(e);
      }
    });
  }
}
