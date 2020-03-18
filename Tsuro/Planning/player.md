#src.Player interface

Players use `Action` objects to communicate the actions they want to take to both the `Rules` object and the referee.

## Public Methods
A `src.Player` is required to have four public methods that the referee can call on them:
- `int welcome(String avatar, Rules rules)`
  - This method welcomes the player to the game. It provides their avatar as well as the `Rules` that they can use to 
  check their moves 
  - The player returns their age from this method to the referee so they can determine play order
- `InitialAction playInitialTurn(Board board, List<Tiles> hand, boolean isLastInvalid)`
  - This method tells the player to play their initial turn. 
  - The player is expected to return an `InitialAction` object (see [rules.md](./rules.md)) which is the same object 
  used by the rule checker. We use this object again here because ideally the player might have already created it to 
  check against the `Rules` object.
  - the last field, `boolean isLastInvalid` is used to inform the player if they have already tried to play a move on 
  this board state and it was invalid. We purposely chose a boolean for this because it forces the player to keep track 
  of the moves they send, so they do not send invalid moves over and over. This might also prevent multiple invalid 
  moves being played in a loop (if they player is implemented in a way that avoids that).
- `TurnAction playTurn(Board board, List<Tiles> hand, boolean isLastInvalid)`
  - This method is very similar to the `playInitialTurn` method above, except it expects a `TurnAction` object back 
  (again see [rules.md](./rules.md)) because less information is required to do a normal turn.
- `void gameOver(boolean hasWon)`
  - This method is used by the referee to tell the player that they are out of the game. 
  - Called when the game is over for normal reasons (no other players are left, there was a tie, or the player died). 
  - Might be called if the player exhibited illegal behavior or ran into an exception. This illegal behavior is defined 
  by the referee, but could include if the player lied about who they were or gave a tile that they did not receive.

## Example Internal Structure of a src.Player
 
```
+---------------------------------------------------+
| PlayerImpl                                        |
+---------------------------------------------------+
| - String avatar                                   | 
| - Rules ruleChecker                               |
| - ArrayList<InitialAction> invalidInitialActions  |
| - ArrayList<TurnAction> invalidTurnActions        |
| - int age                                         |
|                                                   |
| + int welcome(String avatar, Rules rules)         |
| + InitialAction playInitialTurn(                  |
|                    Board board,                   |
|                    List<Tiles> hand,              |
|                    boolean isLastInvalid)         |
| + TurnAction playTurn(                            |
|                    Board board,                   |
|                    List<Tiles> hand,              |
|                    boolean isLastInvalid)         |
| + void gameOver(boolean hasWon)                   |
+---------------------------------------------------+
```

Each ArrayList is intended to store the last action sent until they receive a play call where `isLastInvalid` is false. 
They can also be used for storing actions that the player checked were invalid, if they would find that useful.