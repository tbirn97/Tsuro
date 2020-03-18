package src.Common;

/**
 * Represents a rotation values for tiles in degrees turned clockwise.
 */
public enum Rotation {
  R0(0),
  R90(90),
  R180(180),
  R270(270);

  private final int degrees;

  Rotation(int degrees) {
    this.degrees = degrees;
  }

  /**
   * Get the number of degrees this rotation corresponds to.
   */
  public int getDegrees() {
    return degrees;
  }

  /**
   * Get a rotation instance that corresponds to the given number of degrees.
   */
  public static Rotation getRotationFromDegrees(int degrees) {
    switch(degrees) {
      case 0:
        return Rotation.R0;
      case 90:
        return Rotation.R90;
      case 180:
        return Rotation.R180;
      case 270:
        return Rotation.R270;
      default:
        throw new IllegalArgumentException("Invalid rotation int");
    }
  }
}
