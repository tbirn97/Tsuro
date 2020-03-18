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
 * Encapsulates all the data a tournament observer would want to get from an administrator.
 */
public class AdministratorSubject implements ISubject {
  private List<IObserver> observers;
  private List<List<String>> gameGroups;
  private List<CompetitionResult> gameResults;
  private CompetitionResult tournamentResult;

  public AdministratorSubject() {
    observers = new ArrayList<>();
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
    return Optional.empty();
  }

  @Override
  public Optional<List<Tiles>> getHand() {
    return Optional.empty();
  }

  @Override
  public Optional<Action> getAction() {
    return Optional.empty();
  }

  @Override
  public Optional<CompetitionResult> getResult() {
    if (tournamentResult == null) {
      return Optional.empty();
    }
    return Optional.of(tournamentResult);
  }

  @Override
  public Optional<List<List<String>>> getGameGroups() {
    if (gameGroups == null) {
      return Optional.empty();
    }
    return Optional.of(gameGroups);
  }

  @Override
  public Optional<List<CompetitionResult>> getGameResults() {
    if (gameResults == null) {
      return Optional.empty();
    }
    return Optional.of(gameResults);
  }

  public void setGameGroups(List<List<String>> gameGroups) {
    this.gameGroups = gameGroups;
  }

  public void setGameResults(List<CompetitionResult> gameResults) {
    this.gameResults = gameResults;
  }

  public void setTournamentResult(CompetitionResult tournamentResult) {
    this.tournamentResult = tournamentResult;
  }

  public void notifyAllObservers() {
    for (IObserver observer : observers) {
      observer.notify(this);
    }
  }
}
