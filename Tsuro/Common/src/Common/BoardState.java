package src.Common;

public enum BoardState {
    /**
     * Indicates that a token is on a port that is connected to another port.
     * Used for avatar movement.
     */
    HAS_ADJ,
    /**
     * Indicates that there is no token on a port that is between two tiles.
     * Used for avatar movement.
     */
    NO_ADJ,
    /**
     * Indicates that an avatar is in a loop on the board. Used for checking against moves.
     */
    LOOP,
    /**
     * Indicates when there has been a collision on the board.
     * Used for checking against moves.
     */
    COLLISION,
    /**
     * The game has been finished. Used when checking if a player should be able to make a move.
     */
    GAME_OVER
}
