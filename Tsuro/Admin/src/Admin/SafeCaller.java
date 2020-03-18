package src.Admin;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Calls methods in a safe manner.
 */
public class SafeCaller {

  /**
   * The number of seconds to wait for a callable to return.
   */
  private final static int SECONDS_TO_TIMEOUT = 3;

  /**
   * Safe call a method and get an Optional with the response or an
   * empty Optional if the method call throws an exception, closes the thread, or times out.
   */
  public static <T> Optional<T> safeCall(Callable<T> call) {
    ExecutorService executor = Executors.newCachedThreadPool();

    // Submit the method call.
    Future<T> result = executor.submit(call);

    Optional<T> callResult = Optional.empty();
    try {
      // Get the result; time out if it takes more than 3 second
      T returnedValue = result.get(SECONDS_TO_TIMEOUT, TimeUnit.SECONDS);
      callResult = Optional.of(returnedValue);
    } catch (ExecutionException | InterruptedException | TimeoutException e) {
      // Method call either threw an exception, ended the thread, or timed out during execution.
    }

    // Shutdown the thread
    executor.shutdown();

    return callResult;
  }
}