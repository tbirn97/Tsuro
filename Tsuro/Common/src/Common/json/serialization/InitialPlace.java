package src.Common.json.serialization;

import src.Common.AvatarColor;

/**
 * This class is used to construct an initial-place object to be used in JSON serialization and
 * Deserialization
 */
public class InitialPlace {

    private TilePat tilePat;
    private AvatarColor avatarColor;
    private String port;
    private int x;
    private int y;

    public InitialPlace(TilePat tilePat, AvatarColor avatarColor, String port, int x, int y) {
        this.tilePat = tilePat;
        this.avatarColor = avatarColor;
        this.port = port;
        this.x = x;
        this.y = y;
    }

    public TilePat getTilePat() {
        return tilePat;
    }

    public AvatarColor getAvatarColor() {
        return avatarColor;
    }

    public String getPort() {
        return port;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

