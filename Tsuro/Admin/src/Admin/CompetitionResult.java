package src.Admin;

import src.Common.PlayerInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the result of a single Tsuro game or a Tsuro tournament.
 */
public class CompetitionResult {
    /**
     * An ordered list of players that represents their finishing place in a competition.
     * Players within the same inner list are ranked at the same position. Elements at the beginning
     * of the outer list have players that finished better than players that are in later elements.
     */
    private final List<List<PlayerInterface>> rankings;

    /**
     * A list of players that cheated in a competition.
     */
    private final List<PlayerInterface> cheaters;

    public CompetitionResult(List<List<PlayerInterface>> rankings, List<PlayerInterface> cheaters) {
        // TODO: make this a deep copy
        this.rankings = rankings;
        this.cheaters = new ArrayList<>(cheaters);
    }

    /**
     * Returns an identical competition result where all list of player interfaces are sorted
     * lexicographically by name.
     */
    public CompetitionResult getLexicographicallySortedCompetitionResult() {
        List<List<PlayerInterface>> sortedRankings = new ArrayList<>();
        for(List<PlayerInterface> rank : this.rankings) {
            List<PlayerInterface> sortedPlayers = rank.stream()
                    .sorted(Comparator.comparing(PlayerInterface::getName))
                    .collect(Collectors.toList());
            sortedRankings.add(sortedPlayers);
        }
        List<PlayerInterface> sortedCheaters = this.cheaters.stream()
                    .sorted(Comparator.comparing(PlayerInterface::getName))
                    .collect(Collectors.toList());

        return new CompetitionResult(sortedRankings, sortedCheaters);
    }

    /**
     * Gets the list of player interfaces that have cheated during the competition.
     */
    public List<PlayerInterface> getCheaters() {
        return new ArrayList<>(cheaters);
    }

    /**
     * Gets the player rankings for the competition.
     */
    public List<List<PlayerInterface>> getRankings() {
        // TODO: make this a deep copy
        return rankings;
    }
}
