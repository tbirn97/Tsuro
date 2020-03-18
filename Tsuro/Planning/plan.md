## Identifiable Components of Our Software System
An automated player needs to know 
- where all the avatars are
- how old they are
- what game they are participating in (ie what does the board look like? Where is their avatar?)
- if it is their turn (and if so, what tiles are in their hand).

The game software has to know 
- what players are playing
- what the turn order is
- where the avatars are
- what tiles are on the board
- what the tiles look like
- how to win and lose

The player knows which avatar is theirs because the game software assigns them one when they enter the game. 
The player knows how old they are intrinsically (or they just make it up). 
The player knows information about the game (ie their hand, the board layout, if it is their turn) because the game software tells them.
The game software knows everything about the game except what moves the player is going to make. 

The player and the game software communicate through a UI of some sort. That might be through the command line or through a GUI. 
The user sends data to the game software and the game software responds.

Common knowledge needed
- what the board looks like
- where the avatars are 
- the rules of the game
- how to win/lose

##Implementation 
these pieces. Keep in mind that you wish to have "demo" software soon so that a potential client can admire fully working prototypes.
From the top down:
```
+----------------------------------+        +------------------------------------------+
|             Tsuro                |        |                  Board                   |
+----------------------------------+        +------------------------------------------+
| Board board                      |        | ArrayList<ArrayList<Tile>> board         |
| ArrayList<src.Player> players        |        | +void placeTile(Tile t, Posn loc)        |
| ArrayList<Tile> bag              |        | +String display()                        |
| src.Player currentPlayer             |        | +boolean hasAdj(Tile t, Node n)          |
| -void sortPlayers()              |        | +Pair<Tile, Node> getAdj(Tile t, Node n) |
| -void turn(int numTiles)         |        | +boolean isOnEdge(Tile t, Node n)        |
| -void updatePlayers()            |        | +isValidPlayerLoc(Tile t, Node n)        |
| -void startUp()                  |        +------------------------------------------+
| -void gameOver()                 |
| +String viewBoard()              |        +------------------------------+
| +void placeTile(Tile t, Posn loc)|        |            Token             |
| +String viewBoard()              |        +------------------------------+
+----------------------------------+        | TokenColor color             |
                                            | Tile tile                    |
+----------------------------------+        | Node nodeName                |
|              src.Player              |        | +TokenColor getColor()       |
+----------------------------------+        | +Tile getTile()              |
| Token token                      |        | +Node getNode()              |
| int age                          |        | +void setLoc(Tile t, Node n) |
| Tsuro game                       |        +------------------------------+
| ArrayList<Tile> hand             |
| +Token getToken()                |        +----------------------------------------+
| +void deal(ArrayList<Tile> tiles)|        |                 Tile                   |
| +String viewHand()               |        +----------------------------------------+
| +void play()                     |        | Posn loc                               |
| +boolean equals(Object other)    |        | ArrayList<Pair<Node, Node>> connections|
| +void move(Tile t, Node n)       |        | +Tile rotate(int degrees)              |
+----------------------------------+        | +Posn getLoc()                         |
                                            | +Node getNext(Node n)                  |
+------------------------------+    +-------+-------------+--------------------------+
|   Posn                       |    | <<enum>> TokenColor |   
+------------------------------+    +---------------------+   +---------------+
| int x                        |    | white               |   | <<enum>> Node |
| int y                        |    | black               |   +---------------+
| +int getX()                  |    | red                 |   | N1            |
| +int getY()                  |    | green               |   | N2            |
| +boolean equals(Object other)|    | blue                |   | E1            |
+------------------------------+    +---------------------+   | E2            |
                                                              | S1            |
                                                              | S2            |
                                                              | W1            |
                                                              | W2            |
                                                              +---------------+
```

## Method Explainations
###Tsuro
- `void sortPlayers()` sorts the players ArrayList from oldest to youngest and stores it in Tsuro
- `void turn(int numTiles)` executes a turn for the current player by getting random tiles from the bag of tiles and 
calling `deal(ArrayList<Tile> tiles)` on the player to give them the tiles. It then calls `play()` on the player to let
 the player know it is their turn. After the player does their turn, turn calls `updatePlayers()`
- `void updatePlayers()` loops through the players checking if there is an adjacent tile using the `Board`'s 
`hasAdj(Node n)` method. If there is an adjacent node to a player, the player method `move(Tile t, Node n)` is called. 
Once the player is moved, the game checks if that player is now at the edge of the board (the lose condition) by calling
 `Board's` `isOnEdge(Tile t, Node n)` method. If the player has lost, they are added to a temporary `ArrayList<src.Player>` 
 to keep track of how many players reached the edge this turn. After looping through all the players, the game checks 
 if the temporary list has the same length of the current `players` list. If so, there has been a tie and we call 
 `gameOver()`. Otherwise, we remove the players from the current `players` list that are in the temporary list.
 - `void placeTile(Tile t, Posn p)` checks if the position is next to the `currentPlayer`'s position (by getting the 
 current node and tile the player is on and comparing that posn to the given one). If it is valid it calls the `Board` 
 method of the same name and sees if the board throws an `IllegalArgumentException`. If it does not, the tile was placed
  successfully. If it does, placeTile prints to the console to inform the player that it's move was invalid.
 - `String viewBoard()` returns a string representation of the board setup. The return value of this may change based on
 if we are buidling a GUI or not for the games.
 - `void startUp()` runs the setup round of the game where the players receive 3 tiles instead of 2 and also place their
  tokens such that they are not on the edge of the board. 
 - `void gameOver()` checks the list of players to determine if the gameOver was a tie or not. Prints to the console the 
 `TokenColor`s of the players who won.
 
###Board
 - `void placeTile(Tile t, Posn p)` determines if the tile can be placed on the board at the given position. Throws 
 `IllegalArgumentException` if the tile cannot be placed in the given position.
 - `String display()` returns a string representation of the board. The return value of this may change based on if we 
 are building a GUI or not.
 - `boolean hasAdj(Tile t, Node n)` determines if the tile with the given node has an adjacent tile on the board
 - `Pair<Tile, Node> getAdj(Tile t, Node n)` returns the Tile and equivalent node for the adjacent tile. For example, 
 node A is to the left of node B. Node A's East-Top node is the same as Node B's West-Top node when we are moving the 
 player tokens.
- `boolean isOnEdge(Tile t, Node n)` determines if the tile and node given are on the edge of the board (used mainly 
for checking the lose condition)
- `boolean isValidPlayerPlacement(Tile t, Node n)` determines if the given tile and node are valid for the placement of
the player token at the start of the game.

###src.Player
- `Token getToken()` gets the player's token
- `void deal(ArrayList<Tile> tiles)` refreshes the player's hand with tiles on their turn. Called by Tsuro
- `String viewHand()` returns a string representation of the player's hand. The return value of this may change
based on if we are building a GUI or not.
- `void play()` plays the player's turn. The turn would start with the player viewing the board and their hand with 
`game.viewBoard()` and `this.viewHand()`. Then the player would decide what tile to place where and how much to rotate
it. They would get the tile from their hand and get a replaced tile if they call `rotate(int degrees)` on it. Then they
would call `game.placeTile(tile, posn)` with their chosen tile and position. If this throws an `IllegalArgumentException`
then the player needs to try again.
- `boolean equals(Object other)` compares this player with another object. 
- `void move(Tile t, Node n)` move calls `getNext(Node n)` on the given tile to determine the end of the path on the 
tile. Then it sets the token location, `setLoc(Tile t, Node n)`, to the end of the path on that tile.

###Token
- `TokenColor getColor` gets the token color
- `Tile getTile()` gets the tile the token is on
- `Node getNode()` gets the node the token is on
- `void setLoc(Tile t, Node n)` sets the tile and node of the token

###Tile
- `Tile rotate(int degrees)` rotates the tile and returns a new tile object. (NOTE: Tiles are immutable, so this 
method returns a new tile)
- `Posn getLoc()` gets the location of this tile
- `Node getNext(Node)` gets the next node on the tile following the path from the given node.

###Posn
- `int getX()` gets the x value of the position
- `int getY()` gets the y value of the position
- `boolean equals(Object other)` compares this posn to another object