import Common.*;
import Common.Player.*;
import Common.Actions.TurnAction;

import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Player's Methods:
 * + InitialAction playInitialTurn(Board board, List<Tiles> hand, boolean isLastInvalid)
 * + TurnAction playTurn(Board board, List<Tiles> hand, boolean isLastInvalid)
 */

/**
 * MockPlayerTestHarness
 *
 * Develop an external experimentation harness for the observer; think of it as a mock player.
 * The harness takes a server IP address and a port from the command line.
 * It sends the server a string (your names) and receives some JSON input.
 * The harness renders the state and the player’s decision specified via this JSON input.
 */
public class MockPlayerTestHarness extends TsuroTestHarness implements ISubject {

    private Board currBoard;
    private ArrayList<Tiles> currHand;
    private TurnAction currAction;
    private IObserver currObserver;
    private Socket clientSocket;
    private BufferedReader clientIn;
    private PrintStream clientOut;

    /**
     * For testing purposes
     */
    public MockPlayerTestHarness() {

    }

    public MockPlayerTestHarness(String host, int port) {
        // Setup the connection with given host and port
        try {
            clientSocket = new Socket(host, port);
            clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientOut = new PrintStream(clientSocket.getOutputStream());
        } catch (UnknownHostException ex) {
            System.out.println("Bad server or port number given.");
            System.out.println("Cannot connect to server: " + ex.getMessage());
        } catch (IOException e) {
            System.out.println("Bad server or port number given.");
            System.out.println("IO Exception error: " + e.getMessage());
        }
    }

    public static void main(String[] arg) throws IOException {
        Scanner scan;

        scan = new Scanner(System.in);
        String address;
        int port;

        //TODO: Connect to the server
        System.out.println("Please enter the server's IP address:");
        address = scan.nextLine();
        System.out.println("Please enter your port number:");
        port = scan.nextInt();

        MockPlayerTestHarness harness = new MockPlayerTestHarness(address, port);
        harness.addObserver(new PlayerObserver());

        System.out.println("Please enter your names");
        String name1 = scan.next();
        String name2 = scan.next();
        String names = name1 + ", " + name2;
        harness.clientOut.println(names);

        String pats = harness.clientIn.readLine();

        //Read and store state of game with JSON
        JSONArray in = parseObjToArray(pats);
        harness.makeBoard(in);

        //play the move on the board stored in this
        harness.playMove();

        //Testing Code -
        //        Scanner scan = new Scanner(System.in);
        //        StringBuilder strBuilder = new StringBuilder();
        //        while(scan.hasNextLine()) {
        //            String tempString = scan.nextLine();
        //            strBuilder.append(tempString);
        //        }
        //
        //        System.out.println(strBuilder);
        //
        //        //Read and store state of game with JSON
        //        JSONArray in = parseObjToArray(strBuilder);
        //
        //        harness.makeBoard(in);
        //
        //        //play the move on the board stored in this
        //        harness.playMove();
    }

    private void makeBoard(JSONArray pats) {

        HashMap<AvatarColor, AvatarLocation> avatars = new HashMap<>();
        HashMap<Posn, Tiles> board = new HashMap<>();

        //extract action & hand
        JSONArray turnPat = parseObjToArray(pats.get(pats.size()-1));
        this.assignAction(turnPat);
        pats.remove(pats.size()-1);

        JSONArray pats2 = parseObjToArray(pats.get(0));

        for (Object obj : pats2) {
            JSONArray statePat = parseObjToArray(obj);

            switch (statePat.size()) {
                case 5:
                    // initial place
                    JSONArray tilePat = parseObjToArray(statePat.get(0));
                    Tiles tile = getTileFromTilePat(tilePat);
                    tile = tile.rotate(getFacingFromInt(parseObjToInt(tilePat.get(1))));
                    Posn loc = new Posn(parseObjToInt(statePat.get(3)), parseObjToInt(statePat.get(4)));
                    AvatarLocation aLoc = new AvatarLocation(loc, parseObjToInt(statePat.get(2)));

                    avatars.put(getAvatarColor(statePat.get(1).toString()), aLoc);
                    board.put(loc, tile);
                    break;
                case 3:
                    // intermediate place OR
                    tilePat = parseObjToArray(statePat.get(0));
                    tile = getTileFromTilePat(tilePat);
                    tile = tile.rotate(getFacingFromInt(parseObjToInt(tilePat.get(1))));
                    loc = new Posn(
                            parseObjToInt(statePat.get(1)),
                            parseObjToInt(statePat.get(2)));
                    board.put(loc, tile);
                    break;
                default:
                    System.out.println("Bad state-pat.");
                    break;
            }
        }

        currBoard = new Board(10, avatars, board);

        //tell observer init board with Notify()
        currObserver.notify(this, TurnState.START);
    }

    private void assignAction(JSONArray turnPat) {
        //assign action from turn-pat
        //-> [[player-who-just-played, [player's chosen tile, rotation]], first-tile-of-hand, second-tile-of-hand]
        //-> [[color, [tile-index, degrees]], tile-index, tile-index]
        JSONArray actionPat = parseObjToArray(turnPat.get(0));
        AvatarColor color = getAvatarColor(actionPat.get(0).toString());
        JSONArray tilePat = parseObjToArray(actionPat.get(1));
        Tiles tileChosen = getTileFromTilePat(tilePat);
        Facing dir = getFacingFromInt(parseObjToInt(tilePat.get(1)));

        ArrayList<Tiles> newHand = new ArrayList<>();
        newHand.add(getTileFromIndex(parseObjToInt(turnPat.get(1))));
        newHand.add(getTileFromIndex(parseObjToInt(turnPat.get(2))));

        this.currAction = new TurnAction(color, tileChosen, dir);
        this.currHand = newHand;
    }

    private void playMove() {
        currBoard.placeTile(this.currAction.getPlayerAvatar(), this.currAction.getTileToPlace(),
                this.currAction.getFacingDirection());
        //tell observer post-move board with Notify()
        currObserver.notify(this, TurnState.PLAY);
    }

    @Override
    public void addObserver(IObserver o) {
        this.currObserver = o;
    }

    @Override
    public void removeObserver(IObserver o) {

    }

    public Board getBoard() {
        return this.currBoard;
    }

    public ArrayList<Tiles> getHand() {
        return this.currHand;
    }

    public TurnAction getAction() {
        return this.currAction;
    }

    private Tiles getTileFromTilePat(JSONArray tilePat) {
        int tileIndex = parseObjToInt(tilePat.get(0));
        return getTileFromIndex(tileIndex);
    }


}