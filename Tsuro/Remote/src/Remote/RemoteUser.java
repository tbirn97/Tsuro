package src.Remote;

import org.json.JSONArray;
import org.json.JSONTokener;
import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.Board;
import src.Common.LostConnectionException;
import src.Common.PlayerInterface;
import src.Common.Rules;
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
import java.util.Collections;
import java.util.List;

/**
 * See the Planning/Remote.md file for the specification of RemoteUser
 */
public class RemoteUser implements PlayerInterface {
  private String name;
  private AvatarColor avatar;

  private InputStream inputStream;
  private DataOutputStream outputStream;

  /**
   * Construct a RemoteUser that has the given name and communicates with a RemoteAdmin
   * over the given TCP socket.
   */
  public RemoteUser(Socket connection) {
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

  @Override
  public String getName() {
    return name;
  }

  /**
   * Sets the name this RemoteUser should have.
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public AvatarColor getAvatar() {
    return avatar;
  }

  @Override
  public void playingAs(AvatarColor avatar, Rules rules) {
    this.avatar = avatar; //NOTE: This is overreaching the communication layer responsibilities
    JSONArray jsonColors = ColorHandler.listToJson(Collections.singletonList(avatar));
    JSONArray message = createMessage("playing-as", jsonColors);
    sendJSONArray(message);
    readVoidMessage();
  }

  @Override
  public void otherPlayers(List<AvatarColor> otherPlayers) {
    JSONArray jsonColors = ColorHandler.listToJson(Collections.singletonList(avatar));
    JSONArray message = createMessage("others", jsonColors);
    sendJSONArray(message);
    readVoidMessage();
  }

  @Override
  public InitialAction playInitialTurn(Board board, List<Tiles> hand) {
    JSONArray messageArgument = constructTurnJSON(board, hand);
    JSONArray message = createMessage("initial", messageArgument);
    sendJSONArray(message);
    JSONArray response = readJSONArray();
    return ActionHandler.jsonToInitialAction(response, avatar);
  }

  @Override
  public TurnAction playTurn(Board board, List<Tiles> hand) {
    JSONArray messageArgument = constructTurnJSON(board, hand);
    JSONArray message = createMessage("take-turn", messageArgument);
    sendJSONArray(message);
    JSONArray response = readJSONArray();
    return ActionHandler.jsonToTurnAction(response, avatar);
  }

  /**
   * Will additionally shutdown the TCP connection between the RemoteUser and the
   * RemoteAdmin, any calls made after this call will throw a LostConnectionException.
   */
  @Override
  public void endOfTournament(boolean hasWonTournament) {
    JSONArray wonTournamentJSON = booleanToJSON(hasWonTournament);
    JSONArray message = createMessage("end-of-tournament", wonTournamentJSON);
    sendJSONArray(message);
    readVoidMessage();
    gracefullyShutdown();
  }

  /**
   * Send a JSONArray as a message out of this RemoteUser's output stream.
   */
  private void sendJSONArray(JSONArray message) {
    try {
      this.outputStream.writeBytes(message.toString());
      this.outputStream.flush();
    } catch (IOException e) {
      throw new LostConnectionException(e);
    }
  }

  /**
   * Wait for a JSONArray from this RemoteUser's input stream and return it.
   */
  private JSONArray readJSONArray() {
    JSONTokener tokener = new JSONTokener(this.inputStream);
    if (tokener.more()) {
      return TokenerHandler.getNextJSONArrayNonContinuous(tokener);
    } else {
      throw new LostConnectionException();
    }
  }

  /**
   * Wait for a "void" message from this RemoteUser's input stream and return it.
   */
  private String readVoidMessage() {
    JSONTokener tokener = new JSONTokener(this.inputStream);
    if(tokener.more()) {
      return TokenerHandler.getNextJSONStringNonContinuous(tokener);
    } else {
      throw new LostConnectionException();
    }
  }

  /**
   * Wraps a singular boolean value into a JSON array with a single element.
   */
  private JSONArray booleanToJSON(boolean value) {
    JSONArray booleanJSON = new JSONArray();
    booleanJSON.put(0, value);
    return booleanJSON;
  }

  /**
   * Creates a JSONArray for a turn that has the board JSON as the first element and
   * every subsequent element as a tile-index that is a tile given to a player.
   */
  private JSONArray constructTurnJSON(Board board, List<Tiles> hand) {
    JSONArray turnJSON = new JSONArray();
    JSONArray boardJSON = StatePatHandler.boardToJSON(board);
    turnJSON.put(0, boardJSON);
    TilesFactory tilesFactory = new TilesFactory();
    for (int i = 0; i < hand.size(); i++) {
      Integer tileIndex = tilesFactory.getTilePatFromTile(hand.get(i)).getIndex();
      turnJSON.put(i + 1, tileIndex);
    }
    return turnJSON;
  }


  /**
   * Creates a JSONArray that has messageName as its first element and messageArguments as
   * its second element.
   */
  private JSONArray createMessage(String messageName, JSONArray messageArguments) {
    JSONArray message = new JSONArray();
    message.put(0, messageName);
    message.put(1, messageArguments);
    return message;
  }

  /**
   * Gracefully close the input and output streams.
   */
  private void gracefullyShutdown() {
    try {
      this.inputStream.close();
      this.outputStream.close();
    } catch (IOException e) {
      // If there is an exception then there's nothing to be done.
    }
  }
}
