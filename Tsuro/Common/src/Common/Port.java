package src.Common;

/**
 * Represents a port on a tile where A is the left port on the top side of the tile and
 * ports continue alphabetically clockwise around the tile.
 */
public enum Port {
  A(0),
  B(1),
  C(2),
  D(3),
  E(4),
  F(5),
  G(6),
  H(7);

  private final int value;

  Port(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static Port getPortFromInteger(int value) {
    if (value < 0 || value > 7) {
      throw new IllegalArgumentException("Given a value that is outside of the valid range from" +
          "[0, 7]: " + value);
    }
    return Port.values()[value];
  }
}
