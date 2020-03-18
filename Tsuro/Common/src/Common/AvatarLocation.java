package src.Common;

public class AvatarLocation {
    private final Posn position;
    private final Port port;

    public AvatarLocation(int x, int y, Port port) {
        this.position = new Posn(x, y);
        this.port = port;
    }

    public AvatarLocation(Posn posn, Port port) {
        this.position = posn.clone();
        this.port = port;
    }

    public Posn getPosn() {
        return this.position.clone();
    }

    public Port getPort() {
        return this.port;
    }

    public int getX() {
        return this.position.getX();
    }

    public int getY() {
        return this.position.getY();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AvatarLocation) {
            AvatarLocation otherLoc = (AvatarLocation) other;
            return this.getPosn().equals(otherLoc.getPosn()) && this.getPort() == otherLoc.getPort();
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getPosn().toString());
        builder.append(", port: ");
        builder.append(this.getPort());
        return builder.toString();
    }

    @Override
    public AvatarLocation clone() {
        return new AvatarLocation(this.getPosn().clone(), this.getPort());
    }
}
