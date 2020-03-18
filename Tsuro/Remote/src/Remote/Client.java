package src.Remote;

import src.Common.PlayerInterface;

import java.io.IOException;
import java.net.Socket;

/**
 * A client that can start a remote Tsuro player and connect to a server administrator.
 */
public class Client {

  /**
   * Begin and return a remote admin that will talk to a server at the given host and port and
   * communicate tcp messages to the given PlayerInterface object.
   */
  public static RemoteAdmin getClient(String host, int port, PlayerInterface player) throws IOException {
    Socket clientSocket = new Socket(host, port);
    return new RemoteAdmin(clientSocket, player);
  }
}
