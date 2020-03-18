package src.Admin;

import src.Common.IObserver;
import src.Common.PlayerInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An administrator can run an entire Tsuro tournament from start to finish.
 */
public class administrator {

    /**
     * The list of players that are still in the game.
     */
    private List<PlayerInterface> playersIn = new ArrayList<>();

    /**
     * A list of players that have lost the game.
     */
    private List<PlayerInterface> playersOut = new ArrayList<>();

    /**
     * The list of players that cheated during the tournament.
     */
    private List<PlayerInterface> cheaters = new ArrayList<>();

    /**
     * A subject for a tournament that holds data that can be used by an observer.
     */
    private AdministratorSubject adminSubject = new AdministratorSubject();

    /**
     * A map from player to the order that they were registered in.
     */
    private Map<PlayerInterface, Integer> playerAge = new HashMap<>();

    /**
     * Add an observer to this administrator's observable subject.
     */
    public void addObserver(IObserver observer) {
        adminSubject.addObserver(observer);
    }

    /**
     * Remove an observer to this administrator's observable subject.
     */
    public void removeObserver(IObserver observer) {
        adminSubject.removeObserver(observer);
    }

    /**
     * Add a player to the Tsuro tournament.
     */
    public void registerPlayer(PlayerInterface newPlayer) {
        this.playersIn.add(newPlayer);
        this.playerAge.put(newPlayer, playersIn.size());
    }

    /**
     * Add a list of players to the Tsuro tournament.
     */
    public void registerPlayers(List<PlayerInterface> newPlayers) {
        for (int i = 0; i < newPlayers.size(); i++) {
            this.playersIn.add(newPlayers.get(i));
            this.playerAge.put(newPlayers.get(i), this.playersIn.size());
        }
    }

    /**
     * Used to unit test tournaments. Not to be called in production.
     */
    List<PlayerInterface> getPlayersLeft() { return this.playersIn; }

    /**
     * Starts the tournament using the players held in this Administrator and returns
     * the resulting tournament result.
     */
    public CompetitionResult runTournament() {

        //case where no rounds/games have to be run
        if (this.playersIn.size() < 3) {
            return playNoTournament(this.playersIn);
        }

        //play Rounds
        while(this.playersIn.size() > 5) {
            this.playRound(this.playersIn);
        }

        //play last game
        this.notifyObserversAndUpdateGameGroups(Collections.singletonList(this.playersIn));
        CompetitionResult finalGameResult = playGame(this.playersIn);
        CompetitionResult tournamentResult = updateRankingFromFinalGame(finalGameResult);

        //notify winners and losers
        notifyPlayersOfTournamentResult(tournamentResult.getRankings());

        notifyObserversAndUpdateTournamentResult(tournamentResult);
        return tournamentResult;
    }

    /**
     * Instead of running a tournament declare the list of given players as winners and
     * return the resulting competition result.
     */
    private CompetitionResult playNoTournament(List<PlayerInterface> playersStarting) {
        for (PlayerInterface player : playersStarting) {
            player.endOfTournament(true);
        }
        CompetitionResult tournamentResult = new CompetitionResult(
            Collections.singletonList(playersStarting), new ArrayList<>());
        notifyObserversAndUpdateTournamentResult(tournamentResult);
        return tournamentResult;
    }

    /**
     * Plays a single round of the tournament, and updates the list of players in admin
     * @param currentPlayers List of players going into this round
     */
    private void playRound(List<PlayerInterface> currentPlayers) {
        List<CompetitionResult> gameResults = new ArrayList<>();
        List<List<PlayerInterface>> gameGroups = splitPlayersIntoGameGroups(currentPlayers);
        this.notifyObserversAndUpdateGameGroups(gameGroups);

        for (List<PlayerInterface> gameGroup : gameGroups) {
            CompetitionResult gameResult = playGame(gameGroup);
            gameResults.add(gameResult);
        }

        updateCheaters(gameResults);
        this.notifyObserversAndUpdateGameResults(gameResults);

        //reassign continuing players
        this.playersIn = getAdvancingPlayers(gameResults, 2);
        this.playersIn.sort(Comparator.comparing(curr -> playerAge.get(curr)));
    }

    /**
     * Break a list of players into a as many groups of 5 as possible with all groups being at least size 3.
     */
    private List<List<PlayerInterface>> splitPlayersIntoGameGroups(List<PlayerInterface> allPlayers) {
        List<List<PlayerInterface>> allGames = new ArrayList<>();
        int index = 0;
        while (index < allPlayers.size() - 10) {
            List<PlayerInterface> aGame = createAGameGroup(allPlayers, index, index + 5);
            allGames.add(aGame);
            index += 5;
        }

        //organize how last 2 games of the round are split
        int splitLocation = Math.min(index + 5, allPlayers.size() - 3);

        List<PlayerInterface> secondToLastGame = createAGameGroup(allPlayers, index, splitLocation);
        List<PlayerInterface> lastGame = createAGameGroup(allPlayers, splitLocation, allPlayers.size());
        allGames.add(secondToLastGame);
        allGames.add(lastGame);
        return allGames;
    }

    /**
     * Get a list of players to play a single game from a given list of all players a starting
     * index of players in the game (inclusive) and an ending index of players in the game
     * (exclusive).
     */
    private List<PlayerInterface> createAGameGroup(List<PlayerInterface> allPlayers, int start, int end) {
        List<PlayerInterface> aGame = new ArrayList<>();
        for (int i = start; i < end; i++) {
            aGame.add(allPlayers.get(i));
        }
        return aGame;
    }

    /**
     * Get the list of players that finished in the top ranksAdvancing sets from every game result.
     */
    private List<PlayerInterface> getAdvancingPlayers(List<CompetitionResult> gameResults, int ranksAdvancing) {
        List<PlayerInterface> roundWinners = new ArrayList<>();
        for (CompetitionResult gameResult : gameResults) {
            for (int i = 0; i < ranksAdvancing && i < gameResult.getRankings().size(); i++) {
                roundWinners.addAll(gameResult.getRankings().get(i));
            }
        }
        return roundWinners;
    }

    /**
     * Update the administrator's cheaters with the cheaters from the list of game results.
     */
    private void updateCheaters(List<CompetitionResult> gameResults) {
        for (CompetitionResult gameResult : gameResults) {
            this.cheaters.addAll(gameResult.getCheaters());
        }
    }

    /**
     * Returns a competition result for the tournament based on the result of the
     * final game.
     */
    private CompetitionResult updateRankingFromFinalGame(CompetitionResult gameResult) {
        this.cheaters.addAll(gameResult.getCheaters());
        List<List<PlayerInterface>> ranking = new ArrayList<>(gameResult.getRankings());
        if (this.playersOut.size() > 0) {
            ranking.add(this.playersOut);
        }
        return new CompetitionResult(ranking, this.cheaters);
    }

    /**
     * Play a tsuro game with the given list of players and return the game's result.
     */
    private CompetitionResult playGame(List<PlayerInterface> gamePlayers) {
        Referee referee = new Referee();
        return referee.startGame(gamePlayers);
    }

    /**
     * Given a ranked list of finishing spots inform every player if they won the
     * tournament. Any player in the first list in the ranking has won and every
     * other player has lost.
     */
    private void notifyPlayersOfTournamentResult(List<List<PlayerInterface>> ranking) {
        for (int i = 0; i < ranking.size(); i++) {
            boolean wonTournament = i == 0;
            for (PlayerInterface player : ranking.get(i)) {
                SafeCaller.safeCall(() -> {
                    player.endOfTournament(wonTournament);
                    return true;
                });
            }
        }
    }

    /**
     * Update the observer's state for the beginning of a tournament round with all of
     * the games that are about to start.
     */
    private void notifyObserversAndUpdateGameGroups(List<List<PlayerInterface>> gameGroups) {
        List<List<String>> gameGroupNames = new ArrayList<>(gameGroups.size());
        for (List<PlayerInterface> game : gameGroups) {
            gameGroupNames.add(game.stream().map(PlayerInterface::getName).collect(Collectors.toList()));
        }
        adminSubject.setGameGroups(gameGroupNames);
        adminSubject.setGameResults(null);
        adminSubject.notifyAllObservers();
    }

    /**
     * Update the observer's state for the end of a tournament round with all of the
     * round's game results.
     */
    private void notifyObserversAndUpdateGameResults(List<CompetitionResult> gameResult) {
        adminSubject.setGameResults(gameResult);
        adminSubject.notifyAllObservers();
    }

    /**
     * Update the observer's state for the end of a tournament's result.
     */
    private void notifyObserversAndUpdateTournamentResult(CompetitionResult tournamentResult) {
        adminSubject.setTournamentResult(tournamentResult);
        adminSubject.notifyAllObservers();
    }
}