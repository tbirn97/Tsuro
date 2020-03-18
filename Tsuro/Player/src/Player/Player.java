package src.Player;

import src.Admin.CompetitionResult;
import src.Common.Action;
import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.Board;
import src.Common.IObserver;
import src.Common.ISubject;
import src.Common.PlayerInterface;
import src.Common.Rules;
import src.Common.Tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player implements PlayerInterface, ISubject {

    private final String name;
    private AvatarColor currentAvatar;
    private Strategy strategy;
    private Rules rules;

    private List<IObserver> observers = new ArrayList<>();
    private Board currentBoard;
    private List<Tiles> currentTiles;
    private Action lastAction;

    /**
     * Main constructor for a src.Admin.Player.
     */
    public Player(String name, Strategy strategy) {
        this.name = name;
        this.strategy = strategy;
        this.reset();
    }

    private void checkFields() {
        if (this.strategy == null || this.currentAvatar == null) {
            throw new IllegalArgumentException("Referee forgot to welcome me :'(");
        }
    }

    private void notifyObservers() {
        for (IObserver o : this.observers) {
            o.notify(this);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public AvatarColor getAvatar() {
        return this.currentAvatar;
    }

    @Override
    public void playingAs(AvatarColor avatar, Rules rules) {
        this.reset();
        this.currentAvatar = avatar;
        this.rules = rules;
    }

    @Override
    public void otherPlayers(List<AvatarColor> otherPlayers) {
    }

    @Override
    public InitialAction playInitialTurn(Board board, List<Tiles> hand) {
        checkFields();
        currentBoard = board;
        currentTiles = hand;
        InitialAction action = this.strategy.getInitialTurn(this.currentAvatar, board, hand, rules);
        lastAction = action;
        this.notifyObservers();
        return action;
    }

    @Override
    public TurnAction playTurn(Board board, List<Tiles> hand) {
        checkFields();
        currentBoard = board;
        currentTiles = hand;
        TurnAction action = this.strategy.getTurn(this.currentAvatar, board, hand, rules);
        lastAction = action;
        this.notifyObservers();
        return action;
    }

    @Override
    public void endOfTournament(boolean hasWonTournament) {
        // TODO: implement later maybe?
    }

    private void reset() {
        this.currentAvatar = null;
        this.rules = null;
        this.currentTiles = null;
        this.currentBoard = null;
        this.lastAction = null;
    }

    @Override
    public void addObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver o) {
        observers.remove(o);
    }

    @Override
    public Optional<Board> getBoard() {
        if (currentBoard == null) {
            return Optional.empty();
        }
        return Optional.of(currentBoard);
    }

    @Override
    public Optional<List<Tiles>> getHand() {
        if (currentTiles == null) {
            return Optional.empty();
        }
        return Optional.of(new ArrayList<>(this.currentTiles));
    }

    @Override
    public Optional<Action> getAction() {
        if (lastAction == null) {
            return Optional.empty();
        }
        return Optional.of(lastAction);
    }

    @Override
    public Optional<CompetitionResult> getResult() {
        return Optional.empty();
    }

    @Override
    public Optional<List<List<String>>> getGameGroups() {
        return Optional.empty();
    }

    @Override
    public Optional<List<CompetitionResult>> getGameResults() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return this.currentAvatar != null ? this.name + ": " + this.currentAvatar.toString() : this.name;
    }
}