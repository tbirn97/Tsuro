package src.Common.json.serialization;

/**
 * This class is used to construct a tile-pat object to be used in JSON serialization and
 * Deserialization
 */
public class TilePat {
    private int index;
    private int degree;

    public TilePat(int index, int degree) {
        this.index = index;
        this.degree = degree;
    }

    public int getIndex() {
        return index;
    }

    public int getDegree() {
        return degree;
    }
}
