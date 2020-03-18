package src.Common;

/**
 * Observer interface. Currently expected to be used to observe players, but can be expanded later.
 */
public interface IObserver {

    /**
     * Method that observed objects call to let this IObserver know that something of interest happened.
     * @param caller the object that called this method
     */
    void notify(ISubject caller);
}