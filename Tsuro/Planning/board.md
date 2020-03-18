##Data Representation

The board coordinates start at (0,0) in the upper left corner with x being 
horizontal and y being vertical.

```
+-------------------------------------------------------------------+
|                            Board                                  |
+-------------------------------------------------------------------+
| int boardSize                                                     |
| Map<String, List<Integer>> tokens                                 |
| Map<Posn, Tiles> board                                            |
|                                                                   |
| +int getBoardSize()                                               |
| +Map<Posn, Tiles> getBoard()                                      |  
| +Map<String, List<Integer>> getTokens()                           |
| +void removePlayer()                                              |
| +void placeStartingTile(Tiles, Posn, int)                         |
| +void placeToken(String, Posn, int)                               |
| +boolean placeTile(String, Tiles, int)                            |
|                                                                   |
| #static Map<Posn, Tiles> copyBoard(Map<Posn, Tiles> board)        |
| #static Map<String, int[]> copyTokens(Map<String, int[]> tokens)  |
| #void placeTileHelper(Tiles, Posn, int)                           |
| #boolean moveTokens()                                             |
| #boolean isOnEdge(int, int)                                       |
| #boolean hasAdj(Posn, int)                                        |
| #Posn getAdj(Posn, int)                                           |
|                                                                   |
| -void checkValidPosition(Posn)                                    |
+-------------------------------------------------------------------+
```
###Data
- `boardSize` stores the size of the board (with the implication that the board is square)
- `tokens` is a map of the string names of the tokens (one of white, black, red, green, blue) to a list of integers. 
The list of integers has a length of three and is structured as [x, y, port]. These indicate the location of the token.
- `board` is a map of Posn to Tile objects. This represents the tiles on the board. We chose a map here instead of a 2D 
arraylist to reduce memory load for an empty board

###Public methods
- `getBoardSize()`, `getBoard()`, and `getTokens()` are getters for the data described above
- `removePlayer(string:tokenColor)` removes a player with the given token color from the game
- `placeStartingTile(Tiles:tile, Posn:location, int:rotation )` tells the board how to place the starting tile
- `placeToken(String:tokenColor, Posn:location, int:port)` tells the board where to place the given token
- `placeTile(String:tokenColor, Tiles:tile, int:rotation)` tells the board what tile to place on a normal turn.
Returns a boolean that indicates the game is still running (false = game over)

###Protected Methods
- `copyBoard(Map<Posn, Tiles>:board)` copies the board mapping for use in the constructor and 
`getBoard()`
- `copyTokens(Map<String, int[]>:tokens)` copies the tokens mapping for use in the constructor
and `getTokens()`
- `placeTileHelper(Tiles:tile, Posn:posn, int:rotation)` Helper for placing a tile at a location. Used by 
`placeStartingTile` and `placeTile`
- `moveTokens()` Moves tokens if they have an adjacent tile to move to.
- `isOnEdge(int:x, int:y)` Determines if a given x and y are on the edge of the board.
- `hasAdj(Posn:posn, int:port)` Determines if a given position and port (representing where a token is) has an 
adjacent tile to move to
- `getAdj(Posn:posn, int:port)` Gets the adjacent port assuming there is one.

###Private Methods
- `checkValidPosition(Posn:posn)` Determines if the given Posn is a valid position on the board. Depends on the 
board size only. Does not check if a position already has a tile.

The board also has the following public constructors:
- `Board(int:size)` builds an empty board of the given size
- `Board(int:size, Map<String, List<Integer>>:tokens, Map<Posn, Tile>:board)` builds a board from an intermediate state

The game ends when `placeTile` returns false. 

###Information from the Board

Here is the tile diagram showing where the ports are on a tile for player and referee reference:
```
        0   1
   +----*---*----+
   |             |
 7 *             * 2
 6 *             * 3
   |             |
   +----*---*----+
        5   4
```

###src.Player Methods
####Methods called on the Referee
- `doFirstMove(String:playerColor, Tiles:tile, int:rotation, int x, int y, int port)` is the method for placing the 
starting tile and the player token
- `placeTile(String:playerColor, Tiles:tile, int rotation)` is the method for placing a tile during other turns
- `playerError(String:errorMsg)` method for letting the referee know the player ran into an error

####Methods called on the src.Player by the Referee
- `playingAs(String:tokenColor)` - lets the player know what color token they are
- `doFirstMove(Board:board, [Tile, Tile]:hand, [Token, Token, ...]:avatars)` - tells the player it's their first turn 
and gives required information to play their turn
- `turn(Board, [ Tile, Tile ], [Token, Token, ...])` - tells the player it's their turn (not first)
- `invalidMove(Tile:tile, int rotation, String:message)` - tells the player their move was invalid
- `endPlayer(boolean:hasWon)` - tells the player they are out of the game and if they won or lost


###Referee Methods
Referees are inbetween players and the actual game. Referees review moves that the player sends and only sends them to 
the board if the referee thinks it might be valid (it checks with the rule checker for this). 
All of the commands the players make go to the referee.

####Methods the src.Player calls on the Referee
- `doFirstMove(String:playerColor, Tiles:tile, int:rotation, int x, int y, int port)` is the method for placing the 
starting tile and the player token
- `placeTile(String:playerColor, Tiles:tile, int rotation)` is the method for placing a tile during other turns
- `playerError(String:errorMsg)` method for letting the referee know the player ran into an error

####Methods the Referee calls on the src.Player
- `playingAs(String:tokenColor)` - lets the player know what color token they are
- `doFirstMove(Board:board, [Tile, Tile]:hand, [Token, Token, ...]:avatars)` - tells the player it's their first turn 
and gives required information to play their turn
- `turn(Board, [ Tile, Tile ], [Token, Token, ...])` - tells the player it's their turn (not first)
- `invalidMove(Tile:tile, int rotation, String:message)` - tells the player their move was invalid
- `endPlayer(boolean:hasWon)` - tells the player they are out of the game and if they won or lost

####Methods the Referee calls on the Board
- `Board(int:size)` - build empty board
- `Board(int:size, Map<String, [Posn, int:port]>:tokens, Map<Posn, Tile>:board)` - build board from intermediate state
- `getBoard()` - requests the board state
- `getTokens()` - gets the tokens and their locations on the board
- `removePlayer(string:tokenColor)` removes a player with the given token color from the game
- `placeStartingTile(Tile:tile, Posn:location, int:rotation )` tells the board how to place the starting tile
- `placeToken(Posn:location, int:port)` tells the board where to place the given token
- `placeTile(Tile:tile, int:rotation)` tells the board what tile to place on a normal turn