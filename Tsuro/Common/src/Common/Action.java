package src.Common;

/**
 * Interface for data representations that a player may
 * request. All actions should implement the methods here.
 */
public abstract class Action {
    private final AvatarColor player;
    private final Tiles tile;
    private final Rotation rotation;

    protected Action(AvatarColor player, Tiles tile, Rotation rotation) {
        this.player = player;
        this.tile = tile;
        this.rotation = rotation;
    }

    /**
     * Gets the player avatar of the player who is performing the action.
     * @return playerAvatar (referred to as tokenColor elsewhere)
     */
    public AvatarColor getPlayerAvatar() {
        return this.player;
    }

    /**
     * Gets the player's selected tile that they want to place.
     * @return copy of the Tiles object they want to place
     */
    public Tiles getTileToPlace() {
        return this.tile.clone();
    }

    /**
     * Gets the players's selected rotation for the tile they chose.
     */
    public Rotation getRotation() {
        return this.rotation;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Action) {
            Action otherAction = (Action) other;
            return this.getPlayerAvatar().equals(otherAction.getPlayerAvatar())
                    && this.getTileToPlace().equals(otherAction.getTileToPlace())
                    && this.getRotation().equals(otherAction.getRotation());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("src.Admin.Player: ");
        builder.append(this.player.toString());
        builder.append(" Facing: ");
        builder.append(this.rotation.toString());
        builder.append(" Tile: ");
        builder.append(this.tile.toString());
        return builder.toString();
    }
}