package src.Common;

import src.Admin.CompetitionResult;

import java.util.List;
import java.util.Optional;

/**
 * Interface for objects that are observable by an IObserver.
 */
public interface ISubject {

    /**
     * Adds the given observer to the observers this subject needs to notify about events.
     * @param o observer to add
     */
    void addObserver(IObserver o);

    /**
     * Removes the given observer from the observers this subject needs to notify about events.
     * @param o observer to remove
     */
    void removeObserver(IObserver o);

    /**
     * Gets the Board from this subject, if the subject has it.
     * @return Board
     */
    Optional<Board> getBoard();

    /**
     * Gets the hand from this subject, if the subject has it.
     * @return List of Tiles which represents a "hand"
     */
    Optional<List<Tiles>> getHand();

    /**
     * Gets the action from this subject, if the subject has it.
     * @return Action the subject has
     */
    Optional<Action> getAction();

    /**
     * Gets the result of this competition, if the subject has it.
     */
    Optional<CompetitionResult> getResult();

    /**
     * Gets the groups of players in a round, if the subject has it.
     */
    Optional<List<List<String>>> getGameGroups();

    /**
     * Gets the results of a round of games, if the subject has it.
     */
    Optional<List<CompetitionResult>> getGameResults();
}
