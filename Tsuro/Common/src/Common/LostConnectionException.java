package src.Common;

/**
 * An exception indicating that a TCP connection has been lost or otherwise failed
 * while a client and a server are attempting to communicating.
 */
public class LostConnectionException extends IllegalStateException {
  private static final String exceptionMessage = "The connection was lost and a message " +
      "could not be sent or received";

  public LostConnectionException() {
    super(exceptionMessage);
  }

  public LostConnectionException(Throwable cause) {
    super(exceptionMessage, cause);
  }
}
