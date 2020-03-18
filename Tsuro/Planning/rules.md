# Rules Interface
Rules address the validity of a player' move, and are required to be used by Referees; players have the option to use 
them.

```
+ ------------------------------------------------------------------------------- +
|   Rules                                                                         |
+ ------------------------------------------------------------------------------- +
| + boolean isValidInitialMove(Board board, List<Tiles> hand, Action playerMove)  |
| + boolean isValidTurn(Board board, List<Tiles> hand, Action playerMove)         |
| + void addInitialRule(InitialRule rule)                                         |
| + void addTurnRule(TurnRule rule)                                               |
| + List<InitialRule> getInitialRules()                                           |
| + List<TurnRule> getTurnRules()                                                 |
+ ------------------------------------------------------------------------------- +
```
## Method Explanations
- `boolean isValidInitialMove(Board board, List<Tiles> hand, Action playerMove)` 
   checks that all of the stored `InitialRule` objects think the given `Action` is valid
- `boolean isValidTurn(Board board, List<Tiles> hand, Action playerMove)`
   checks that all of the stored `TurnRule` objects think the given `Action` is valid
- `void addInitialRule(InitialRule rule)` adds a given rule to the list of rules for the `Rules` object to check
- `void addTurnRule(TurnRule rule)` adds a given rule to the list of rules for the `Rules` object to check
- `List<InitialRule> getInitialRules()` gets the current list of `InitialRule`s that this `Rules` object checks initial 
moves against
- `List<TurnRule> getTurnRules()` gets the current list of `TurnRule`s that this `Rules` object checks normal moves 
against 

## Relevant classes

### Action (Interface) and Subclasses

The `Action` interface and its two subclasses, `InitialAction` and `TurnAction`, are used to contain actions that the 
player wants to perform in the game. Currently, there are two types of actions allowed in a game of Tsuro: first move 
and subsequent move. The first move requires more information to execute, so the `InitialAction` class has more fields 
and methods. 

```
                    +----------------------------+
                    | Action <Interface>         |
                    +----------------------------+
                    | + String getPlayerAvatar() |
                    | + Tiles getTileToPlace()   |
                    | + int getRotation()        |
                    | + boolean isInitialAction()|
                    | + boolean isTurnAction()   |
                    +----------------------------+
                                    ^
                                    |
                    +---------------+-------------------+
                    |                                   |
+-----------------------------------+           +-----------------+
| InitialAction                     |           | TurnAction      |
+-----------------------------------+           +-----------------+
| - String player                   |           | - String player |
| - Tiles tile                      |           | - Tiles tile    |
| - int rotation                    |           | - int rotation  |
| - Posn location                   |           +-----------------+
| - int port                        |
|                                   |
| + Posn getTilePlacementLocation() |
| + int getAvatarPort()             |
+-----------------------------------+
```

#### Method Explanations

- `String getPlayerAvatar()` gets the player avatar color of the player who is doing this Action
- `Tiles getTileToPlace()` gets the Tiles object that the player wants to place
- `int getRotation()` gets the player's indicated rotation value
- `boolean isInitialAction()` tells other classes if this action is of the class InitialAction. This is used in the 
`Rules` object for checking if a given `Action` is the correct one to send to a rule.
- `boolean isTurnAction()` tells other classes if this action is of the class TurnAction. This is used in the `Rules` 
object for checking if a given `Action` is the correct one to send to a rule.

- `Posn getTilePlacementLocation()` gets the player's indicated placement location for the tile
- `int getAvatarPort()` gets the player's indicated port for their avatar

###Observer (Interface)

We decided to implement an Observer design pattern for our `InitialRule` and `TurnRule` interfaces (as well as our
 referee) to make it easier for the `Board` to communicate loops and collisions while still operating normally.
 
```
+-------------------------------+
| Observer                      |
+-------------------------------+
| + void receive(String data)   |
+-------------------------------+
```
#### Method Explanations
- `void receive(String data)` receives a message from an `Observable` object about something that occurred

###InitialRule and TurnRule (Interfaces)

We decided to implement rule checking using function objects that each know how to check one rule. These rules are 
delimited by if they are checking rules for an initial player turn or any other player turn. The `Rules` object stores 
and checks all of the rules given to it for a player or referee.

```
  +-------------------------------------------------------------------------------+ 
  | InitialRule extends Observer                                                  |
  +-------------------------------------------------------------------------------+
  | +boolean isValidMove(Board board, List<Tiles> hand, InitialAction playerTurn) |
  | +void receive(String data)                                                    |
  +-------------------------------------------------------------------------------+
  
  +----------------------------------------------------------------------------+ 
  | TurnRule extends Observer                                                  |
  +----------------------------------------------------------------------------+
  | +boolean isValidMove(Board board, List<Tiles> hand, TurnAction playerTurn) |
  | +void receive(String data)                                                 |
  +----------------------------------------------------------------------------+
```

Note that these interfaces have almost the same method required of them. We chose, purposefully to not wrap these in one
 interface with each accepting an `Action` object. This was to force the `Rules` object to check that `InitialRule` and 
 `TurnRule` receive valid data. The `Rules` object is the one to tell the referee or player that the data given is bad.

#### Method Explanations

- `boolean isValidMove(Board board, List<Tiles> hand, InitialAction playerTurn` returns true if the given game 
information and turn information pass this `InitialRule`'s rule.
- `boolean isValidMove(Board board, List<Tiles> hand, TurnAction playerTurn` returns true if the given game information 
and turn information pass this `TurnRule`'s rule
- `void receive(String data)` is described above for the `Observer` interface. The `InitialRule` and `TurnRule` objects
use the Observer to check for the breaking of rules that do not have a method to directly check.