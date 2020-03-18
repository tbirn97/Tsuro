package src.Admin;

import src.Common.Action;
import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.Board;
import src.Common.BoardMutator;
import src.Common.BoardState;
import src.Common.IObserver;
import src.Common.PlayerInterface;
import src.Common.Rule.StandardRules;
import src.Common.Rules;
import src.Common.Tiles;
import src.Common.TilesFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


/**
 * Referee implementation who runs a game given a list of player objects.
 * <br>
 * Has the following internal logic:
 * <ul>
 *     <li>Give each player tiles in order of index, looping when at the max</li>
 *     <li>Kick the player from the game if:
 *      <ul>
 *          <li>They returned a tile that was not given to them)</li>
 *          <li>They claimed they were a different color than assigned</li>
 *          <li>They attempted 8 moves in one turn where none of them were legal via the rule checker</li>
 *      </ul>
 *     </li>
 *     <li>Assume given players are in the order that they will take turns</li>
 *     <li>Use the {@link Rules} object as their rule checker with the following rules:
 *     <ul>
 *         <li>{@link src.Common.Rule.InitialRuleNoAdjacent}</li>
 *         <li>{@link src.Common.Rule.InitialRulePortFacesInward}</li>
 *         <li>{@link src.Common.Rule.TurnRuleNoInfiniteLoops}</li>
 *         <li>{@link src.Common.Rule.TurnRuleNoSuicide}</li>
 *     </ul>
 *     </li>
 * </ul>
 */
public class Referee {

    private static int NUMBER_OF_TILES = 35;

    private Board board;
    private Rules rules;
    private LinkedHashMap<AvatarColor, PlayerInterface> players;
    private int nextTileIndex;
    private int currentRound;
    private SortedMap<Integer, List<PlayerInterface>> roundLosers;
    private List<PlayerInterface> cheaters;
    private RefereeSubject subject;


    public Referee() {
        this.rules = new StandardRules();
        subject = new RefereeSubject();
    }

    /**
     * This constructor is used for testing purposes only and should not be used in production code!
     */
    Referee(Board board, Rules rules, LinkedHashMap<AvatarColor, PlayerInterface> players,
                   int nextTileIndex, int currentRound, SortedMap<Integer, List<PlayerInterface>> roundLosers,
                   List<PlayerInterface> cheaters) {
        this.board = board;
        this.rules = rules;
        this.players = players;
        this.nextTileIndex = nextTileIndex;
        this.currentRound = currentRound;
        this.roundLosers = roundLosers;
        this.cheaters = cheaters;
    }

    private void initGame() {
        this.board = new Board.Builder().build();
        this.players = new LinkedHashMap<>();
        this.nextTileIndex = 0;
        this.currentRound = 0;
        this.roundLosers = new TreeMap<>();
        this.cheaters = new ArrayList<>();
    }

    /**
     * Add an observer to this referee's observable subject.
     */
    public void addObserver(IObserver observer) {
        this.subject.addObserver(observer);
    }

    /**
     * Remove an observer to this referee's observable subject.
     */
    public void removeObserver(IObserver observer) {
        this.subject.removeObserver(observer);
    }

    /**
     * Converts all the players in the current {@code this.roundLosers} list into a competition
     * result.
     */
    private CompetitionResult convertRoundLosersToGameResults() {
        List<List<PlayerInterface>> rankings = new ArrayList<>();

        // Reverse the order of the mapping so the latest round is first
        TreeMap<Integer, List<PlayerInterface>> reversed = new TreeMap<>(
                (Integer i1, Integer i2)->i2-i1);
        reversed.putAll(this.roundLosers);

        if(!this.players.values().isEmpty()) {
            // Start with first place rank
            rankings.add(new ArrayList<>(this.players.values()));
        }

        rankings.addAll(reversed.values());

        return new CompetitionResult(rankings, this.cheaters);
    }

    /**
     * Main method for running a game.
     * @param playersIn list of players who will be playing. Assumed to be in turn order
     * @return list of the winning players
     */
    public CompetitionResult startGame(List<PlayerInterface> playersIn) {
        this.initGame();

        this.setPlayerInterfaces(playersIn);

        // welcome each player to the game
        this.welcomePlayerInterfaces();

        // initial
        this.playInitialRound();

        // reg turns
        while (this.board.getState() != BoardState.GAME_OVER) {
            this.playTurnRound();
        }

        CompetitionResult gameResult = convertRoundLosersToGameResults();
        notifyObserversAndUpdateState(this.board, gameResult);

        subject.setGameResult(gameResult);
        subject.notifyObservers();

        // return game results
        return gameResult;
    }

    /**
     * Assign a list of players colors for their avatars to use during this game.
     */
    private void setPlayerInterfaces(List<PlayerInterface> playersIn) {

        //Make list of AvatarColors
        AvatarColor[] colors = AvatarColor.values();

        //Place sorted players in this.players
        // ASSUME: playersIn.size() is <= colors.size()
        for (int index = 0; index < playersIn.size(); index++) {
            this.players.put(colors[index], playersIn.get(index));
        }
    }

    /**
     * Call the "playingAs" and "otherPlayers" methods on all of the players playing.
     */
    private void welcomePlayerInterfaces() {
        for (AvatarColor color : this.players.keySet()) {
            PlayerInterface player = this.players.get(color);
            callPlayerMethod(() -> {
                player.playingAs(color, this.rules);
                return true;
            });
        }
        for (AvatarColor color : this.players.keySet()) {
            PlayerInterface player = this.players.get(color);
            ArrayList<AvatarColor> otherPlayerInterfaces = new ArrayList<>(this.players.keySet());
            otherPlayerInterfaces.remove(color);
            callPlayerMethod(() -> {
                player.otherPlayers(otherPlayerInterfaces);
                return true;
            });
        }
    }

    /**
     * Play the initial round in the Tsuro game.
     */
    private void playInitialRound() {
        //initial-turns
        LinkedHashMap<AvatarColor, PlayerInterface> currentRoundPlayerInterfaces = new LinkedHashMap<>(this.players);

        for (AvatarColor col : currentRoundPlayerInterfaces.keySet()) {
            if (!this.players.containsKey(col)) {
                continue;
            }

            List<Tiles> playerHand = this.getHandOfSize(3);
            Optional<Action> playerInitialAction = getPlayerInterfaceAction(col, playerHand, true);
            if (playerInitialAction.isPresent()) {
                Action currAction = playerInitialAction.get();
                assert(currAction instanceof InitialAction);
                InitialAction initialAction = (InitialAction) currAction;
                handleInitialTurn(initialAction);
                this.notifyObserversAndUpdateState(this.board, initialAction, playerHand);
            }
            else {
                removePlayerInterface(col, true);
            }
        }
        this.currentRound++;
    }

    /**
     * Get an optional action from a player for an initial or intermediate turn. Will return
     * an empty optional if the player does not return a valid move for the board.
     */
    private Optional<Action> getPlayerInterfaceAction(AvatarColor color, List<Tiles> playerHand, boolean isInitialTurn) {
        PlayerInterface p = this.players.get(color);

        Optional<Action> action = callPlayerMethod(() -> isInitialTurn ?
            p.playInitialTurn(this.board, playerHand) :
            p.playTurn(this.board, playerHand));

        if (action.isPresent()) {
            Action currAction = action.get();
            this.notifyObserversAndUpdateState(this.board, currAction, playerHand);
            if (isCheating(currAction, color)) {
                this.removePlayerInterface(color, true);
            } else if (isValidMove(this.board, playerHand, currAction)) {
                return Optional.of(currAction);
            }
        }

        return Optional.empty();
    }

    /**
     * Play an intermediate round of Tsuro.
     */
    private void playTurnRound() {
        //regular-turns
        LinkedHashMap<AvatarColor, PlayerInterface> currentRoundPlayerInterfaces = new LinkedHashMap<>(this.players);
        for (AvatarColor color : currentRoundPlayerInterfaces.keySet()) {
            if (!this.players.containsKey(color)) {
                continue;
            }

            List<Tiles> playerHand = this.getHandOfSize(2);
            Optional<Action> optAction = getPlayerInterfaceAction(color, playerHand, false);
            // If this player color was removed during this round, do not give them a turn
            if (optAction.isPresent()) {
                Action action = optAction.get();
                assert(action instanceof TurnAction);
                TurnAction currAction = (TurnAction) action;
                handleIntermediateTurn(currAction, color, playerHand);
                this.notifyObserversAndUpdateState(this.board, action, playerHand);
            }
            else {
                this.removePlayerInterface(color, true);
            }
        }
        if(players.size() <= 1) {
            this.board = new Board.Builder().basedOn(this.board).setBoardState(BoardState.GAME_OVER).build();
        }
        this.currentRound++;
    }

    /**
     * Plays a valid InitialAction for a player and updates the board.
     */
    private void handleInitialTurn(InitialAction action) {
        this.board = BoardMutator.placeStartingTile(this.board, action.getTileToPlace(),
            action.getAvatarPlacement().getPosn(), action.getRotation());
        this.board = BoardMutator.placeAvatar(this.board, action.getPlayerAvatar(),
            action.getAvatarPlacement());
    }

    /**
     * Plays a valid TurnAction for a player playing as the given avatar color and updates
     * the internal board and players accordingly.
     */
    private void handleIntermediateTurn(TurnAction action, AvatarColor avatar, List<Tiles> playerHand) {
        Board nextBoard = BoardMutator.placeTurnTile(this.board, avatar, action.getTileToPlace(),
            action.getRotation());


        //make sure gameState is ok
        if (nextBoard.getState() == BoardState.LOOP) {
            this.removePlayerInterface(avatar, false);
        }

        this.board = nextBoard;

        // PlayerInterface no longer on the board
        Set<AvatarColor> deadPlayers = this.players.keySet()
            .stream().filter(p -> !board.isInGame(p))
            .collect(Collectors.toSet());
        for (AvatarColor avatarColor : deadPlayers) {
            removePlayerInterface(avatarColor, false);
        }
    }

    /**
     * Uses the rules checker in this Referee to determine the validity of given move/action.
     * @param board The current board
     * @param hand The given hand
     * @param move The move/action returned by the player
     * @return TRUE if the given move/action is valid
     */
    private boolean isValidMove(Board board, List<Tiles> hand, Action move) {
        if (move instanceof InitialAction) {
            return this.rules.isValidInitialMove(board, hand, (InitialAction) move);
        } else if (move instanceof TurnAction) {
            return this.rules.isValidTurn(board, hand, (TurnAction) move);
        }
        else {
            throw new IllegalArgumentException("Given an unhandled action type: " + move);
        }
    }

    /**
     * Determines if the given action qualifies as cheating.
     * Currently this only accounts for players attempting to pose as other players.
     * We chose to make this a helper method so that other cheating rules can be added here in the future.
     * @param action Action being "inspected"
     * @param currColor Current player turn color
     * @return TRUE if given action qualifies as cheating
     */
    private boolean isCheating(Action action, AvatarColor currColor) {
        return action.getPlayerAvatar() != currColor;
    }


    /**
     * Removes a player from the game and informs them they lost if they weren't a cheater.
     */
    void removePlayerInterface(AvatarColor col, Boolean hasCheated) {
        PlayerInterface playerToRemove = this.players.get(col);
        this.board = BoardMutator.removePlayer(this.board, col);

        if (hasCheated) {
            this.cheaters.add(playerToRemove);
        } else {
            if (!this.roundLosers.containsKey(this.currentRound)) {
                this.roundLosers.put(currentRound, new ArrayList<>());
            }
            this.roundLosers.get(currentRound).add(playerToRemove);
        }

        this.players.remove(col);
    }

    /**
     * Get a list of tiles of the given hand size.
     */
    private ArrayList<Tiles> getHandOfSize(int handSize) {
        ArrayList<Tiles> newHand = new ArrayList<>();

        for (int tiles = 0; tiles < handSize; tiles++) {
            newHand.add(getTile());
        }
        return newHand;
    }

    /**
     * Get a singular tile in order according to {@code this.nextTileIndex}.
     */
    private Tiles getTile() {
        Tiles tile = new TilesFactory().makeTile(this.nextTileIndex);
        this.nextTileIndex = (this.nextTileIndex + 1) % NUMBER_OF_TILES;
        return tile;
    }

    /**
     * Safely calls a player interface method and returns an optional value that is empty
     * if the player threw an exception when called.
     */
    private <T> Optional<T> callPlayerMethod(Callable<T> playerCall) {
        return SafeCaller.safeCall(playerCall);
    }

    /**
     * Update the observer's state for the end of a Tsuro game.
     */
    private void notifyObserversAndUpdateState(Board board, CompetitionResult result) {
        this.subject.setBoard(board);
        this.subject.setGameResult(result);
        this.subject.notifyObservers();
    }

    /**
     * Update the observer's state for a round during a Tsuro game.
     */
    private void notifyObserversAndUpdateState(Board board,
                                               Action action,
                                               List<Tiles> hand) {
        this.subject.setBoard(board);
        this.subject.setActionAndHand(action, hand);
        this.subject.notifyObservers();
    }
}