#Referee Interface

Our `Referee` interface extends the `Observer` interface (see [rules.md](./rules.md)) to allow it to receive messages 
from the `Board` while it is executing tasks. 

##Public Methods

A `Referee` is required to have two public methods. 

- `void receive(String data)` is required by the `Observer` interface. See [rules.md](./rules.md) for a description
- `List<src.Player> startGame(List<src.Player> players)`
  - This method is called by the tournament manager to start a game with a given set of players. The returned list of 
  players is to inform the tournament manager who won the game (could be one or more players).
  - This method will likely have many internal methods to handle all the actions in the game. 
  
  
## Example Internal Structure of a Referee
```
+---------------------------------------------------+
| RefereeImpl                                       |
+---------------------------------------------------+
| - Board board                                     |
| - Rules ruleChecker                               |
| - LinkedHashMap<String, src.Player> players           |
|                                                   |
| + void receive(String data)                       |
| + List<src.Player> startGame(List<src.Player> players)    |
+---------------------------------------------------+
```

The `LinkedHashMap` is a structure that maps a key to a value but preserves the order they were added to the Map. This 
can be used to keep track of the player order as well as which player is which avatar. 