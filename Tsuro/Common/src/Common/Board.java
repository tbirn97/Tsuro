package src.Common;

import src.Common.Util.MapDeepCopier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Data representation of the board. IMMUTABLE
 */
public class Board {
    private final int boardSize;
    private final Map<AvatarColor, AvatarLocation> avatars;
    private final Map<Posn, Tiles> tiles;
    private final BoardState boardState;


    private Board(int boardSize, Map<AvatarColor, AvatarLocation> avatars, Map<Posn, Tiles> tiles, BoardState state) {
        this.boardSize = boardSize;
        this.avatars = MapDeepCopier.copy(avatars);
        this.tiles = MapDeepCopier.copy(tiles);
        this.boardState = state;
    }

    public static Board getInstance() {
        return new Builder().build();
    }

    public int getBoardSize() {
        return this.boardSize;
    }

    public Map<AvatarColor, AvatarLocation> getAvatars() {
        return MapDeepCopier.copy(this.avatars);
    }

    public Map<Posn, Tiles> getTiles() {
        return MapDeepCopier.copy(this.tiles);
    }

    public BoardState getState() {
        return this.boardState;
    }

    public Optional<Tiles> getTile(Posn location) {
        if (this.tiles.containsKey(location)) {
            return Optional.of(this.tiles.get(location));
        }
        else {
            return Optional.empty();
        }
    }

    public boolean isInGame(AvatarColor avatar) {
        return this.avatars.containsKey(avatar);
    }

    public Optional<AvatarLocation> getAvatarLocation(AvatarColor avatar) {
        return this.isInGame(avatar) ? Optional.of(this.avatars.get(avatar)) : Optional.empty();
    }

    /**
     * Returns the upper left corner Posn and the lower right corner Posn. This allows the rule checker to not care that
     * we start at 0,0.
     * @return [ upperLeft Posn, lowerRight Posn ]
     */
    public List<Posn> getBoardBounds() {
        return Arrays.asList(new Posn(0, 0), new Posn(this.boardSize - 1, this.boardSize - 1));
    }

    public boolean isValidPosition(Posn posn) {
        return posn.getX() >= 0 && posn.getX() < this.boardSize
                && posn.getY() >= 0 && posn.getY() < this.boardSize;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Board state: ");
        strBuilder.append(this.boardState.toString());
        strBuilder.append("\n");
        ArrayList<Posn> listOfKeys = new ArrayList<>(this.tiles.keySet());
        Collections.sort(listOfKeys);
        for(Posn p : listOfKeys) {
            strBuilder.append("Position: ");
            strBuilder.append(p.toString());
            strBuilder.append(" Tile: ");
            strBuilder.append(this.tiles.get(p).toString());
            strBuilder.append('\n');
        }

        ArrayList<AvatarColor> avatarColors = new ArrayList<>(this.avatars.keySet());
        Collections.sort(avatarColors);
        for (AvatarColor avatar : avatarColors) {
            strBuilder.append("Avatar: ");
            strBuilder.append(avatar.toString());
            strBuilder.append(" @ ");
            strBuilder.append(this.avatars.get(avatar).toString());
            strBuilder.append('\n');
        }
        return strBuilder.toString();
    }

    public static class Builder {
        private int boardSize;
        private Map<AvatarColor, AvatarLocation> avatars;
        private Map<Posn, Tiles> tiles;
        private BoardState boardState;

        private void checkBoardSize(int boardSize) {
            if (boardSize < 1) {
                throw new IllegalArgumentException("BoardOld size must be a positive number.");
            }
        }

        public Builder() {
            boardSize = 10;
            avatars = new HashMap<>();
            tiles = new HashMap<>();
            boardState = BoardState.HAS_ADJ;
        }

        public Builder basedOn(Board board) {
            this.boardSize = board.getBoardSize();
            this.avatars = board.getAvatars();
            this.tiles = board.getTiles();
            this.boardState = board.getState();
            return this;
        }

        public Builder setBoardSize(int boardSize) {
            this.checkBoardSize(boardSize);
            this.boardSize = boardSize;
            return this;
        }

        public Builder setAvatars(Map<AvatarColor, AvatarLocation> givenAvatars) {
            avatars = givenAvatars;
            return this;
        }

        public Builder setTiles(Map<Posn, Tiles> givenTiles) {
            tiles = givenTiles;
            return this;
        }

        public Builder setBoardState(BoardState state) {
            boardState = state;
            return this;
        }

        public Board build() {
            return new Board(boardSize, avatars, tiles, boardState);
        }
    }
}