package src.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BoardMutator {


    private static Board placeTile(Board board, Tiles t, Posn loc) {
        Map<Posn, Tiles> tiles = board.getTiles();
        tiles.put(loc, t);
        return new Board.Builder().basedOn(board).setTiles(tiles).build();
    }


    private static Board placeAvatarHelp(Board board, AvatarColor color, AvatarLocation loc) {
        Map<AvatarColor, AvatarLocation> avatars = board.getAvatars();
        avatars.put(color, loc);
        return new Board.Builder().basedOn(board).setAvatars(avatars).build();
    }

    private static Board moveAvatar(Board board, AvatarColor color, AvatarLocation newLoc) {
        Map<AvatarColor, AvatarLocation> avatars = board.getAvatars();
        //System.out.println(avatars.toString());
        avatars.replace(color, newLoc);
        //System.out.println(avatars.toString());
        return new Board.Builder().basedOn(board).setAvatars(avatars).build();
    }

    private static boolean isValidBoardState(Board board) {
        Map<AvatarColor, AvatarLocation> avatars = board.getAvatars();
        for (AvatarColor c : avatars.keySet()) {
            if (getAdj(avatars.get(c)) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes a given token from the board. This is used when the referee catches a player cheating.
     * @param tokenColor token color of the token to remove
     */
    public static Board removePlayer(Board board, AvatarColor tokenColor) {
        Map<AvatarColor, AvatarLocation> avatars = board.getAvatars();
        if (avatars.containsKey(tokenColor)) {
            Map<AvatarColor, AvatarLocation> newAvatars = new HashMap<>();
            for (AvatarColor color : avatars.keySet()) {
                if (!color.equals(tokenColor)) {
                    newAvatars.put(color, avatars.get(color));
                }
            }
            return new Board.Builder().basedOn(board).setAvatars(newAvatars).build();
        }
        return board;
    }

    /**
     * Places the player's first tile in the game (requires a location to do so).
     * @param tile tile to place
     * @param loc location to place at
     * @param rotation the rotation of the given tile
     */
    public static Board placeStartingTile(Board board, Tiles tile, Posn loc, Rotation rotation) {
        return placeTileHelper(board, tile, loc, rotation);
    }

    /**
     * Helper for placing a tile at a location. Used by {@link #placeStartingTile} and {@link #placeTile}.
     * @param tile tile to place
     * @param loc location to place at
     * @param rotation the rotation degrees of the tile
     */
    private static Board placeTileHelper(Board board, Tiles tile, Posn loc, Rotation rotation) {

        if (!isValidBoardState(board)) {
            //board = moveAvatars(board);
        }


        Tiles rotatedTile = tile.rotate(rotation);

        //System.out.println("[BoardMutator] in placeTileHelper");
//        System.out.println(board.toString());
//        System.out.println(loc.toString());

        if (board.getTile(loc).isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Tile already at given location: %d, %d", loc.getX(), loc.getY()));
        }

        if(!board.isValidPosition(loc)) {
            throw new IllegalArgumentException(
                    String.format("Invalid position for board of size %d: %s", board.getBoardSize(), loc.toString()));
        }

        return placeTile(board, rotatedTile, loc);
    }

    /**
     * Places the given token on the board at the given location and port.
     * @param board previous board state to add this avatar to
     * @param avatarColor token to place
     * @return new board state with avatar on it
     */
    public static Board placeAvatar(Board board, AvatarColor avatarColor, AvatarLocation aLoc) {
        if(!board.isValidPosition(aLoc.getPosn())) {
            throw new IllegalArgumentException(
                    String.format("Invalid position for board of size %d: %s", board.getBoardSize(),
                            aLoc.getPosn().toString()));
        }

        if (board.getAvatarLocation(avatarColor).isPresent()) {
            throw new IllegalArgumentException("Given token " + avatarColor.toString() + " is already on the board!");
        }


        return placeAvatarHelp(board, avatarColor, aLoc);
    }

    /**
     * Places a tile during a normal turn where the player has no choice where it goes.
     * @param avatarColor token color, used to get the adjacent tile based on where the token is
     * @param tile tile to place
     * @param rotation the rotation of this tile
     * @return boolean, false if game is over after moving the avatars
     */
    public static Board placeTurnTile(Board board, AvatarColor avatarColor, Tiles tile, Rotation rotation) {
        Optional<AvatarLocation> loc = board.getAvatarLocation(avatarColor);
        if (!loc.isPresent()) {
            throw new IllegalArgumentException("Avatar " + avatarColor.toString() + " not on board!");
        }
        Posn newTilePosn = getAdj(loc.get());

        if(!board.isValidPosition(newTilePosn)) {
            throw new IllegalArgumentException(
                    String.format("Invalid position for board of size %d: %s", board.getBoardSize(), newTilePosn.toString()));
        }


        //System.out.println("new tile posn: " + newTilePosn.toString());
        board = placeTileHelper(board, tile, newTilePosn, rotation);

        // move/update all the avatars
        return moveAvatars(board);
    }

    /**
     * Assuming the given coordinates are from 2 valid adjacent tiles, determines the new port value of the token on the
     * second tile, if it moved across the second tile.
     * @param startLocation the current avatar location
     * @param adj Posn of adjacent tile
     * @return int end-port-value relative to Adj tile
     */
    private static Port getNewPortFrom(Board board, AvatarLocation startLocation, Posn adj) {
        int portFrom = 0;
        if (Math.abs(startLocation.getX() - adj.getX()) != 0) {
            if (startLocation.getX() - adj.getX() > 0) {
                // adj tile is on left, movement is either port 6 TO port 3 or port 7 TO port 2
                portFrom = 2 - (startLocation.getPort().getValue() - 7);
            } else {
                // adj tile is on right, movement is either port 2 TO port 7 or port 3 TO port 6
                portFrom = 6 - (startLocation.getPort().getValue() - 3);
            }
        } else if (Math.abs(startLocation.getY() - adj.getY()) != 0) {
            if (startLocation.getY() - adj.getY() > 0) {
                // adj tile is on top, movement is either port 0 TO port 5 or port 1 TO port 4
                portFrom = 4 - (startLocation.getPort().getValue() - 1);
            } else {
                // adj tile is on bot, movement is either port 4 TO port 1 or port 5 TO port 0
                portFrom = 0 - (startLocation.getPort().getValue() - 5);
            }
        }
        return board.getTiles().get(adj).getEndOfConnection(Port.getPortFromInteger(portFrom));
    }

    /**
     * Determines if the token of given location data has a valid move that is not in the given list of ports crossed.
     * @param token Token being calculated for valid moves
     * @param portsCrossed List of ports token has already crossed
     * @return TRUE if token has a valid location to move to
     */
    private static BoardState hasNonLoopingMove(Board board, AvatarColor token, ArrayList<AvatarLocation> portsCrossed) {
        AvatarLocation location = board.getAvatarLocation(token).get();

        if (hasAdjacent(board, location) ) {

            Posn adj = getAdj(location);
            AvatarLocation newLocation = new AvatarLocation(adj, getNewPortFrom(board, location, adj));

            if (portsCrossed.contains(newLocation)) {
                return BoardState.LOOP;
            }


            return BoardState.HAS_ADJ;

        }
        // No more places to move to
        return BoardState.NO_ADJ;
    }

    /**
     * Moves avatars if they have an adjacent tile to move to.
     * @return boolean, false if game is over after moving the avatars
     */
    private static Board moveAvatars(Board board) {
        ArrayList<AvatarColor> deadPlayers = new ArrayList<>();
        Board curBoard = board;
        for (AvatarColor avatar : board.getAvatars().keySet()) {
            ArrayList<AvatarLocation> portsAlreadyCrossed = new ArrayList<>();
            // checks if token hasAdjacent tile + if new end path is not in list of portsAlreadyCrossed
            //TODO: abstract looping check to be part of series of modular checks in separate "hasMove" method.
            curBoard = new Board.Builder().basedOn(curBoard).setBoardState(hasNonLoopingMove(curBoard, avatar, portsAlreadyCrossed)).build();
            while (curBoard.getState().equals(BoardState.HAS_ADJ)) {
                //System.out.println("has adj");
                // Add previous tile to list of tilesAlreadyCrossed
                AvatarLocation currentLocation = curBoard.getAvatarLocation(avatar).get();

                portsAlreadyCrossed.add(currentLocation);

                // get adjacent tile's location
                Posn adj = getAdj(currentLocation);
                // establish token's new location

                AvatarLocation newLocation = new AvatarLocation(adj, getNewPortFrom(curBoard, currentLocation, adj));

                //System.out.println("moving " + avatar.toString() + " to " + newLocation.toString());

                curBoard = moveAvatar(curBoard, avatar, newLocation);

                //System.out.println("after move");
                //System.out.println(curBoard.toString());

                curBoard = new Board.Builder().basedOn(curBoard).setBoardState(hasNonLoopingMove(curBoard, avatar, portsAlreadyCrossed)).build();

            }

            if (curBoard.getState().equals(BoardState.LOOP)) {
                return new Board.Builder().basedOn(curBoard).build();
            }

            // if player dead add to list of dead guys
            if (isOnEdge(curBoard, avatar)) {
                deadPlayers.add(avatar);
            } else if (!curBoard.getAvatarLocation(avatar).isPresent()) {
                deadPlayers.add(avatar);
            }
        }

        // remove player avatars who are deceased
        for (AvatarColor token : deadPlayers) {
            curBoard = removePlayer(curBoard, token);
        }

        if (curBoard.getAvatars().keySet().size() == 0 || curBoard.getAvatars().keySet().size() == 1) {
            return new Board.Builder().basedOn(curBoard).setBoardState(BoardState.GAME_OVER).build();
        }

        Set<AvatarLocation> avatarLocations = new HashSet<>(curBoard.getAvatars().values());
        if (avatarLocations.size() != curBoard.getAvatars().keySet().size()) {
            return new Board.Builder().basedOn(curBoard).setBoardState(BoardState.COLLISION).build();
        }

        return new Board.Builder().basedOn(curBoard).build();
    }

    /**
     * Determines if the given token's location is on the edge of the board.
     * @param avatar Name of token
     * @return TRUE if the given token's location is on the edge of the board
     */
    private static boolean isOnEdge(Board board, AvatarColor avatar) {
        AvatarLocation location = board.getAvatars().get(avatar);
        Facing portSide = Connection.getPortSide(location.getPort());
        int boardSize = board.getBoardSize();
        switch(portSide) {
            case NORTH:
                // Top of tile
                return location.getY() == 0;
            case EAST:
                // Right side of tile
                return location.getX() == boardSize - 1;
            case SOUTH:
                // Bottom of tile
                return location.getY() == boardSize - 1;
            case WEST:
                // Left side of tile
                return location.getX() == 0;
            default:
                return false;
        }
    }

    /**
     * Given a posn and port, see if there is another tile placed on any side around it.
     * @param loc location of the avatar that we are checking
     * @return true if the given location has a placed tile next to it
     */
    // TODO: add tests
    public static boolean hasAdjacent(Board board, AvatarLocation loc) {
        Posn adjPosn = getAdj(loc);
        return board.getTile(adjPosn).isPresent();
    }

    /**
     * Gets the adjacent position given the current position and the port of the token.
     * (since this method needs to return 2 things, we exchange tile location data as a Posn)
     * @param location the current location of the avatar
     * @return
     */
    protected static Posn getAdj(AvatarLocation location)  {
        Facing portSide = Connection.getPortSide(location.getPort());
        int xPosn = -65;
        int yPosn = -65;
        switch(portSide) {
            case NORTH:
                // Top of tile
                xPosn = location.getX();
                yPosn = location.getY() - 1;
                break;
            case EAST:
                // Right side of tile
                xPosn = location.getX() + 1;
                yPosn = location.getY();
                break;
            case SOUTH:
                // Bottom of tile
                xPosn = location.getX();
                yPosn = location.getY() + 1;
                break;
            case WEST:
                // Left side of tile
                xPosn = location.getX() - 1;
                yPosn = location.getY();
                break;
        }

        // TODO: replace this with a Optional<T>
        if (xPosn == -65 && yPosn == -65) {
            return null;
        }
        return new Posn(xPosn, yPosn);
    }

}
