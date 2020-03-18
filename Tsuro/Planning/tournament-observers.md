#Tournament-Observer

The Tournament-Observer interface extends the `IObserver` interface, and closely observes an `Administrator` and their
role in a Tsuro Tournament. This observer is notified of all actions performed by an `Administrator` during the course
of a tournament.

###Methods

The only public method this interface would need is `notify(ISubject caller, TurnState state)`. This method would be
called upon the `Tournament-Observer` by the observed subject (`Administrator`), whenever a noteworthy game-state change
occurs (i.e. game start, game result, etc.).

The important state changes that `notify` would handle are the following:
- `Get-Players State`, after which the observer can get the following:
    - List of players currently involved in the tournament
- `Game-Start State`, after which the observer can get the following:
    - Players of the game
    - Rules of the game
- `Game-Start State`, after which the observer can get the following:
    - src.Player(s) won
    - Remaining tournament players
- `Tournament-End State`, after which the observer can get the following:
    - src.Player(s) won

The `Administrator` should only notify its observers of a `Tournament-End State` once per tournament, but should notify 
it's observer(s) every `Game-Start State`, and `Game-Start State`.

Tournament-Observer should hold private helper methods for each state-change, to render the unique components in each 
change. These methods could be called names similar to `RenderBoard`, or `RenderInitialization`.

Todo:
- add the getters mentioned above to the ISubject interface
- create an enum for the game states above

