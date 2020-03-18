package src.Common;

public class Posn implements Comparable<Posn> {
    private int x;
    private int y;

    public Posn(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAdjacent(Posn other) {
        // TODO: check if posn is adjacent to another
        return true;
    }

    @Override
    public Posn clone() {
        return new Posn(this.x, this.y);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Posn) {
            Posn otherPosn = (Posn) other;
            return otherPosn.getX() == this.getX() && otherPosn.getY() == this.getY();
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.getX(), this.getY());
    }

    @Override
    public int hashCode() {
        return this.x * 10 + this.y;
    }

    @Override
    public int compareTo(Posn given) {

        if (this.getY() != given.getY()) {
            return this.getY() < given.getY() ? -1 : 1;
        }
        else if(this.getX() != given.getX()) {
            return this.getX() < given.getX() ? -1 : 1;
        }
        else {
            return 0;
        }

    }
}
