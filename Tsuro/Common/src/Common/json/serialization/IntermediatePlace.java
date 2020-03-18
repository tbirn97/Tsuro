package src.Common.json.serialization;

/**
 * This class is used to create an intermediate-place object to be used in JSON serialization and
 * Deserialization
 */
public class IntermediatePlace {

    private TilePat tilePat;
    private int x;
    private int y;

    public IntermediatePlace(TilePat tilePat, int x, int y) {
        this.tilePat = tilePat;
        this.x = x;
        this.y = y;
    }

    public TilePat getTilePat() {
        return tilePat;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
