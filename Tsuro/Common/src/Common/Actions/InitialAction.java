package src.Common.Actions;


import src.Common.Action;
import src.Common.AvatarColor;
import src.Common.AvatarLocation;
import src.Common.Rotation;
import src.Common.Tiles;

/**
 * Data class for initial player moves.
 */
public class InitialAction extends Action {
    private final AvatarLocation location;

    public InitialAction(AvatarColor playerAvatar, Tiles tile, Rotation rotation, AvatarLocation location) {
        super(playerAvatar, tile, rotation);
        this.location = location;
    }

    public AvatarLocation getAvatarPlacement() {
        return location;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof InitialAction) {
            InitialAction otherAction = (InitialAction) other;
            return super.equals(other)
                    && this.getAvatarPlacement().equals(otherAction.getAvatarPlacement());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(" Location: ");
        builder.append(this.location.toString());
        return builder.toString();
    }
}
