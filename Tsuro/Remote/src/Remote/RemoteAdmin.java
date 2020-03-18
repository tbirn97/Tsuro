package src.Remote;

import org.json.JSONArray;
import org.json.JSONTokener;
import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.Board;
import src.Common.LostConnectionException;
import src.Common.PlayerInterface;
import src.Common.Rule.StandardRules;
import src.Common.Tiles;
import src.Common.TilesFactory;
import src.Common.json.ActionHandler;
import src.Common.json.ColorHandler;
import src.Common.json.StatePatHandler;
import src.Common.json.TokenerHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * See the Planning/Remote.md file for the specification of RemoteAdmin.
 *
 * RemoteAdmin assumes that it will be playing with the DefaultRules object for all turns.
 */
public class RemoteAdmin {
  private PlayerInterface player;

  private InputStream inputStream;
  private DataOutputStream outputStream;

  /**
   * Construct a remote admin that will talk over the given TCP socket and communicate
   * external TCP commands.
   */
  public RemoteAdmin(Socket connection, PlayerInterface player) {
    this.player = player;
    connectToSocket(connection);
  }

  /**
   * Connect to the given socket and set up the input and output streams for communication.
   */
  private void connectToSocket(Socket connection) {
    try {
      this.inputStream = connection.getInputStream();
      this.outputStream = new DataOutputStream(connection.getOutputStream());
    } catch (IOException e) {
      throw new LostConnectionException(e);
    }
  }

  /**
   * Listen for incoming prompts from this TCP socket and delegate to the RemoteAdmin's player
   * and send responses to the TCP socket when appropriate. This method will ignore commands
   * that are not specified in the protocol definition and throw an exception if given any
   * ill formed JSON.
   */
  public void listen() {
    JSONTokener tokener = new JSONTokener(inputStream);
    while (tokener.more()) {
      JSONArray nextCommand = TokenerHandler.getNextJSONArrayNonContinuous(tokener);
      String commandType = nextCommand.getString(0);
      JSONArray commandArguments = nextCommand.getJSONArray(1);
      handleCommand(commandType, commandArguments);
    }
  }

  /**
   * Handles a command of the given type with the given JSON array of arguments.
   */
  private void handleCommand(String commandType, JSONArray commandArguments) {
    switch (commandType) {
      case "playing-as":
        handlePlayingAs(commandArguments);
        break;
      case "others":
        handleOthers(commandArguments);
        break;
      case "initial":
        handleInitial(commandArguments);
        break;
      case "take-turn":
        handleTakeTurn(commandArguments);
        break;
      case "end-of-tournament":
        handleEndOfTournament(commandArguments);
        break;
      default:
        throw new IllegalArgumentException("Given a command of an undefined type!: " + commandType);
    }
  }

  /**
   * Handle the playing-as message and delegate to the internal player.
   */
  private void handlePlayingAs(JSONArray commandArguments) {
    List<AvatarColor> color = ColorHandler.jsonToList(commandArguments);
    player.playingAs(color.get(0), new StandardRules());
    sendVoidMessage();
  }

  /**
   * Handle the playing-as message and delegate to the internal player.
   */
  private void handleOthers(JSONArray commandArguments) {
    List<AvatarColor> colors = ColorHandler.jsonToList(commandArguments);
    player.otherPlayers(colors);
    sendVoidMessage();
  }

  /**
   * Handle the initial message, delegate to the internal player, and return the player's
   * requested move back to the TCP socket.
   */
  private void handleInitial(JSONArray commandArguments) {
    Board board = StatePatHandler.jsonToBoard(commandArguments.getJSONArray(0));
    List<Tiles> hand = jsonToHand(commandArguments);
    InitialAction playerTurn = player.playInitialTurn(board, hand);
    JSONArray initialTurnJSON = ActionHandler.initialActionToJSON(playerTurn);
    sendArrayMessage(initialTurnJSON);
  }

  /**
   * Handle the take-turn message, delegate to the internal player, and return the player's
   * requested move back to the TCP socket.
   */
  private void handleTakeTurn(JSONArray commandArguments) {
    Board board = StatePatHandler.jsonToBoard(commandArguments.getJSONArray(0));
    List<Tiles> hand = jsonToHand(commandArguments);
    TurnAction playerTurn = player.playTurn(board, hand);
    JSONArray turnJSON = ActionHandler.turnActionToJSON(playerTurn);
    sendArrayMessage(turnJSON);
  }

  /**
   * Handle the end-of-tournament message and delegate to the internal player.
   */
  private void handleEndOfTournament(JSONArray commandArguments) {
    boolean wonTournament = jsonToBoolean(commandArguments);
    player.endOfTournament(wonTournament);
    sendVoidMessage();
  }

  /**
   * Return a JSONArray message to the TCP socket.
   */
  private void sendArrayMessage(JSONArray message) {
    try {
      this.outputStream.writeBytes(message.toString());
      this.outputStream.flush();
    } catch (IOException e) {
      throw new LostConnectionException(e);
    }
  }

  /**
   * Return a "void" message to the TCP socket.
   */
  private void sendVoidMessage() {
    try {
      this.outputStream.writeBytes("\"void\"");
      this.outputStream.flush();
    } catch (IOException e) {
      throw new LostConnectionException(e);
    }
  }

  /**
   * Create a register message for the given player name.
   */
  private JSONArray createRegisterMessage(String name) {
    JSONArray message = new JSONArray();
    message.put(0, "register");
    JSONArray messageArguments = new JSONArray();
    messageArguments.put(0, name);
    message.put(1, messageArguments);
    return message;
  }


  /**
   * Convert a JSON array to a single boolean object from the array's first element.
   */
  private boolean jsonToBoolean(JSONArray jsonArray) {
    return jsonArray.getBoolean(0);
  }

  /**
   * Convert a JSON array to a list of tiles where all but the first element in the array are
   * tile indices.
   */
  private List<Tiles> jsonToHand(JSONArray jsonArray) {
    TilesFactory tileFactory = new TilesFactory();
    List<Tiles> hand = new ArrayList<>();
    for (int i = 1; i < jsonArray.length(); i++) {
      int tileIndex = jsonArray.getInt(i);
      Tiles tile = tileFactory.makeTile(tileIndex);
      hand.add(tile);
    }
    return hand;
  }
}
