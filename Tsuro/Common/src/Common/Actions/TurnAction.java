package src.Common.Actions;

import src.Common.Action;
import src.Common.AvatarColor;
import src.Common.Rotation;
import src.Common.Tiles;

/**
 * Data class for player turn data.
 */
public class TurnAction extends Action {

    public TurnAction(AvatarColor playerAvatar, Tiles tile, Rotation rotation) {
        super(playerAvatar, tile, rotation);
    }
}
