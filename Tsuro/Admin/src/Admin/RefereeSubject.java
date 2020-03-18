package src.Admin;

import src.Common.Action;
import src.Common.Board;
import src.Common.IObserver;
import src.Common.ISubject;
import src.Common.Tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Holds all the information that an observer may need to observe a game.
 */
public class RefereeSubject implements ISubject {

    private List<IObserver> observers;
    private Board board;
    private Action mostRecentActionTaken;
    private List<Tiles> mostRecentHandForPlayer;
    private CompetitionResult gameResult;

    public RefereeSubject() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void addObserver(IObserver o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(IObserver o) {
        this.observers.remove(o);
    }

    @Override
    public Optional<Board> getBoard() throws UnsupportedOperationException {
        if (board == null) {
            return Optional.empty();
        }
        return Optional.of(board);
    }

    @Override
    public Optional<List<Tiles>> getHand() throws UnsupportedOperationException {
        if (mostRecentHandForPlayer == null) {
            return Optional.empty();
        }
        return Optional.of(mostRecentHandForPlayer);
    }

    @Override
    public Optional<Action> getAction() {
        if (mostRecentActionTaken == null) {
            return Optional.empty();
        }
        return Optional.of(mostRecentActionTaken);
    }

    @Override
    public Optional<CompetitionResult> getResult() {
        if (gameResult == null) {
            return Optional.empty();
        }
        return Optional.of(gameResult);
    }

    @Override
    public Optional<List<List<String>>> getGameGroups() {
        return Optional.empty();
    }

    @Override
    public Optional<List<CompetitionResult>> getGameResults() {
        return Optional.empty();
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setActionAndHand(Action mostRecentActionTaken, List<Tiles> mostRecentHandForPlayer) {
        this.mostRecentActionTaken = mostRecentActionTaken;
        this.mostRecentHandForPlayer = mostRecentHandForPlayer;
    }

    public void setGameResult(CompetitionResult gameResult) {
        this.gameResult = gameResult;
    }

    void notifyObservers() {
        for (IObserver observer : observers) {
            observer.notify(this);
        }
    }
}
