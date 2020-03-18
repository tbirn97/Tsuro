package src.Common;

import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;

import java.util.List;

public interface PlayerInterface {

    String getName();

    AvatarColor getAvatar();

    void playingAs(AvatarColor avatar, Rules rules);

    void otherPlayers(List<AvatarColor> otherPlayers);

    /**
     * This method is called by the Referee when this player should perform its initial turn.
     * @param board current board state
     * @param hand given tiles that can be placed
     * @return InitialAction object containing the player's desired action
     */
    InitialAction playInitialTurn(Board board, List<Tiles> hand);

    /**
     * This method is called by the Referee when this player should perform a non-initial turn.
     * @param board current board state
     * @param hand given tiles that can be placed
     * @return TurnAction object containing the player's desired action
     */
    TurnAction playTurn(Board board, List<Tiles> hand);

    /**
     * The Tournament manager calls this method on the player when the tournament is over.
     * @param hasWonTournament true if the player is a winner of the tournament
     */
    void endOfTournament(boolean hasWonTournament);
}