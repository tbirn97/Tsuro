#Game-Observer

The Game-Observer interface extends the `IObserver` interface, and closely observes a `Referee` and their role in a game
of Tsuro. This observer is notified of all transactions passed through a `Referee` that occur in a singe game.

###Methods

The only public method this interface would need is `notify(ISubject caller, TurnState state)`. This method would be
called by the observed subject, in this case by a `Referee`, whenever a noteworthy game-state change occurs (i.e. game start,
cards given to player upon turn start, player death, etc.).

The important state changes that `notify` would handle are the following:
- `Game Initialization State`, after which the observer can get the following:
    - Number of players playing
    - src.Player turn order
    - Initial board state
    - Set of rules being used
- `src.Player Start-Turn State`, after which the observer can get the following
    - Current board state (including remaining players)
    - src.Player
    - Hand given to player
- `src.Player Turn State`, after which the observer can get the following:
    - src.Player and their decided action (tile-placed, tile-rotation, tile-location)
    - Action Outcome, which could be one of the following:
        - valid-move
        - cheat-move
        - invalid-move
- `End-Game State`, after which the observer can get the following
    - Final board state
    - List of players who won
    
    
The `Referee` should only notify its observers of a `Game Initialization State` and `End-Game State` once per game, but
should notify it's observer(s) every `src.Player Start-Turn State`, `src.Player Turn State`, and `src.Player Post-Turn State`.

Game-Observer should hold private helper methods for each state-change, to render the unique components in each change.
These methods could be called names similar to `RenderBoard`, or `RenderInitialization`.

Todos:
- add the stated Game-States to our TurnState enum.
- update the ISubject interface to have the appropriate getters mentioned above for different states
- make the Referee implement ISubject and implement the getters and sending notifications to listeners