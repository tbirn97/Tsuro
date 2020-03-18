# Tsuro

## Problems

### Critical (Not following specification)
- Players that give invalid moves will be asked for a new turn 8 times
    - **FIXED**: `ecfd0309e0c4fd42c5ae4d0acf14350895e26ce0`
- There is no Player directory, all player classes are in Common
    - **FIXED**: `722391e7509825f5ca0c30365da3fb9b3638166e`
- The Player class is hard coded to always use FirstS, no way of creating a player with a dynamic strategy
    - **FIXED**: `02c8c8acf41591d1c133bfa52833100a643504d3`
- If a tournament is started with 8 player, two games of 4 & 4 will be played rather than the 5 & 3 that is specified
    - **FIXED**: `19c7204b45b9f38108573df588c1077af815f91e`
- A player that continually gives invalid moves will be treated the same as players that lose normally, won't be deemed a cheater
    - **FIXED**: `02db56926c097e2b19f02579e43b4751800e84c6`
- The only way for a player to be considered cheating is to say their avatar is a different color
    - **FIXED**: `02db56926c097e2b19f02579e43b4751800e84c6`
- Cheaters are included in the rankings; they're added to cheaters in getInterface and added again in removePlayerInterface()
    - **FIXED**: `02db56926c097e2b19f02579e43b4751800e84c6`
- Referee is not checking for players throwing exceptions when calling for moves
    - **FIXED**: `a763184d0a139b6688564e8d3a340fbc24168bf2`
- Player's playing tiles that cause infinite loops still places the tile and then removes the player
    - **FIXED**: `43144ef995e88b0599c8061849b9f2d07b266d88`
- No way of getting a full tournament ranking or cheaters, runTournament() only returns a list of winners
    - **FIXED**: `679956b68808c09674e759efb259b9875ad1c63d`
- Players that died in the middle of the round will still be prompted for a move (even though the referee will never play it)
    - **FIXED**: `c0fcd6807b97d45255666353141a7e5fe8cd1ddf`
- A game can end in the middle of the round. According to spec the game can only end at the end of a round
    - **FIXED**: `7566c1d1702ee5e1ca10fa6f190a202b8496ab7a`
- All player calls are not wrapped in exception checking
    - **FIXED**: `a763184d0a139b6688564e8d3a340fbc24168bf2`
- A COLLISION board state will override the GAME_OVER board state so that a game that has a collision in it will never end
    - **FIXED**: `8f3a16ac4c34e276512afab4359008a46cdc9c6d`
- Rules does not check that the tile placement is trivially valid, i.e. that a tile can't be placed at the same spot as another or off the board.
    - **FIXED**: `01c03e7f337749541a68cfdcde1277d79d3532f0`
- welcome() method in player violates the Tsuro protocol and should be split into two methods
    - **FIXED**: `fe67c1e2c083e63e61ccaff2c6ae96153235d241`
- TilesFactory throws a null pointer exception when given a tile index of 35 rather than an IllegalArgumentException. This is somehow also expected behavior in unit tests
    - **FIXED**: `807bde1977ecdf9ab1e5e7f2e18aa402cdca0524`
- In Referee, NUMBER_OF_TILES is defined as 34. Based on the current implementation this will never allow the tile of index 34 to be used.
    - **FIXED**: `d7a05e0058948a79f4600f6b5b5a6bda2010db9d`
- In BoardMutator inside the moveAvatars function, players were not being removed from the board prior to returning a finished game boardstate. This led to the finished game considering some ties as one player winning and the other losing, versus a move that cased both of their deaths.
    - **FIXED**: `d7a05e0058948a79f4600f6b5b5a6bda2010db9d`

### Time Complexity
- TilesFactory re initializes the same map every time someone needs to use it
    - **FIXED**: `807bde1977ecdf9ab1e5e7f2e18aa402cdca0524`
- playRound() method in administrator is O(N^2) because of sublist calls
    - **FIXED**: `f13cabfaef584f6cb3a05d6845dbe58055c29920`
- Board copies every time it does anything, can happen 15+ times per player turn
- Deep copying maps of immutable objects in Board

### Bad Design
- Game ranking is 1-indexed, stores the round that players lost in but this is only accessed like an index
    - **FIXED**: `3395eb3718ae7aa2f955ba72871da6360f8bcbfa`
- getGameWinnersAndUpdateLosers() in administrator requires that the client specifies one higher than the actual requested number of advancing ranks.
    - **FIXED**: `222a5e1efa3fad9e975fb8a81f6a5e16d4d191a5`
- There is no default rule checker that has always wrong rules, components have to know which rules to add and must add exception checking rules first.
    - **FIXED**: `01c03e7f337749541a68cfdcde1277d79d3532f0`
- Facing is a parallel data structure, we are already keeping the port an avatar is on. Facing is also overloaded, sometimes it can mean where an avatar is while other times it indicates a tile rotation.
    - **FIXED**: `f24bbd5fdfde7c2c5734e2afb446e001c49919f2`
- No enum for Ports, using a integer instead.
    - **FIXED**: `8bab585edbfc36f892aeef1b204945bd8ebb60cb`
- HAS_ADJ is not documented, unsure of use case
    - **FIXED**: `5ae3527a869b6ab2cd85b30fb094d52e1612288a`
- Board has a single BoardState, the presence of HAS_ADJ and NO_ADJ implies that there are multiple states a board can be in
- Players receive a direct copy of the rules from the referee. Any player can add or change any rules during the game.
- Using assert statements for typechecking, assert statements are disabled by default so this does not help in normal execution.
- The tile object is called Tiles instead of Tile
- Players are expected to hold their own state, namely their avatar color and their name
- The board object holds its own state.
- The observer pull method is currently not scalable, new methods have to be added to the interface every time an observer is added with new data.

## Remote

The remote directory handles all the communication logic needed to run distributed Tsuro games.

The RemoteAdmin and RemoteUser objects follow the Remote Proxy pattern and hide TCP message logic from the business logic of a player or administrator.

## Administrator
An `Administrator` runs a Tsuro tournament. A tournament consists of multiple 'rounds' until there are less than three 
players remaining. The remaining players are declared winners.

An `Administrator` statically depends on nothing.

An `Administrator` dynamically depends on:
- `PlayerInterface` : the players entered in the tournament
- `Referee` : the referee implementation they use to run Tsuro games
- `GameResults` : the data class returned by the `Referee` to indicate 'ranking' and 'cheaters'

##Referee
In a tournament, the referee is created by the administrator to run a single game. It is assigned with a list of 
PlayerInterfaces and prompts/receives the commands for each of them.

A `Referee` statically depends on the following: 
- `rules` When a referee is initialized, it generates a Rules object to rulecheck all its players' submitted actions. 
This Rules object always holds the same set of rules (as this is how we designed it), and can never be changed by the 
Referee or its players.
 
A `Referee` dynamically depends on the following:
- `board` An empty Board object is created upon Referee initialization, which is the Board that which the Referee's game
is played on. As soon as the Referee starts a game, every confirmed action from one of its players is applied to the 
Board using our BoardMutator class, so the Board is constantly changing. The Board is also updated whenever a player
needs to be removed from the game.  
- `players` The Referee recieves a list of PlayerInterfaces through it's constructor, and converts it into a 
LinkedHashMap of AvatarColor to PlayerInterface that is used throughout the entire game. This map is updated ever time
a player completes a turn, whether to update it's placement on the board, or to remove it from the game.
- `roundLosers` The map of Integer and PlayerInterface that is roundLosers is a Referee-specific object that helps it 
keep track of when each player stop playing the game. The Integer indicates which round the corresponding 
PlayerInterface either won or lost the game. This map is also used to calculate the GameResults at the end of the game,
by creating a Map of Integer (player's rank) to List of PlayerInterfaces, based on which round each PlayerInterface 
stopped playing at.
    - `GameResults` The GameResults class holds a Map of Integer(game-completion rank) to List of PlayerInterfaces, and
    a List of PlayerInterface: cheaters.
- `cheaters` Is another Referee-specific object, that is defined as a List of PlayerInterfaces. This list is updated
whenever a PlayerInterface is found to be cheating, and is accounted for in the GameResults of a game.

##Rules
A Rules object acts a set of rules that a PlayerInterface or Referee can refer to, to check the validity of either an
Action, an Action being either an InitialAction or TurnAction. Rules is made to be able to hold a list of 
initialPlacementRules, as well as a list of turnRules. 

A `Rules` statically depends on the following:
- `initialPlacementRules` This List of InitialRule is used whenever isValidInitialMove is called on Rules. The given 
move is compared to every InitalRule in this list. Although there is a method in Rules that allow more InitialRules to 
be added to initalPlacementRules, it is generally only used during the creation of Rules.
- `turnRules` This List of TurnRule is used whenever isValidTurnMove is called on Rules. The given move is compared to
every TurnRule in this list. Although there is a method in Rules that allow more TurnRules to be added to turnRules, it
is generally only used during the creation of Rules.

Once created, a Rules is generally treated as a static object. It does not depend on any dynamic objects.

##IObservers/ISubject/IDraw
IObservers and ISubject work together, as an implementation of IObservers is made to observe a corresponding
implementation of an ISubject. Every implementation of ISubject also uses an implementation of IDraw to render itself.

An `IObserver` statically depends on the following:
- `drawer' An IObserver uses an implementation of an IDraw to render itself.

An `IObserver` dynamically depends on the following:
- `caller` An IObserver's caller is the ISubject it is observing. Whenever a state-change occurs in a game of Tsuro that
affects the observed ISubject, the IObserver is notified of this state-change and reacts accordingly. 

An `ISubject` statically depends on the following:
- `observer` As described, an ISubject would be able to add and remove static Observers, and each would be notified upon
any event that affects the ISubject.

An `ISubject` dynamically depends on the following:
- `board` We assume that an ISubject is an element of Tsuro that would "play" the game, so it holds a Board that it
plays on. This Board would change constantly throughout a game. 
- `hand` We assume that an ISubject is an element of Tsuro that would "play" the game, so it holds a List of Tiles that
it uses to complete a turn. Although a hand can never change, the specific hand that a ISubject holds is updated every
time an ISubject is prompted to make a turn. 

An `IDraw` statically depends on the following:
- `*General Board Properties*` In order to properly scale and place elements of a Tsuro game, an IDraw holds a set of
static board properties (i.e. board-size, tile-size, etc.).

##PlayerInterface
The `PlayerInterface` interface is used to indicate what methods (e.g. functionality) are required for a player to play 
our Tsuro game. This interface extends our `ISubject` interface (described above) to allow IObservers to attach and 
observe the concrete player.

We include a concrete implementation of the `PlayerInterface` in `src.Player`

A `src.Player` statically depends on:
- their name, represented as a string

A `src.Player` dynamically depends on:
- `AvatarColor` : to indicate what avatar they are in the game of Tsuro they are playing (if they are playing one)
- `Strategy` : to indicate what strategies the player uses during games
- `Rules` : (described above) to indicate what rules the current game of Tsuro abides by

The other fields in our `src.Player` class are used when `IObserver`s call get methods on the player to determine their
status in the game. 

```
+---------------------------------------------------+
| PlayerInterface <<extends ISubject>>              |
+---------------------------------------------------+
| + String getName()                                |
| + void welcome(List<AvatarColor> otherPlayers,    |
|                AvatarColor avatar, Rules rules)   |
| + InitialAction playInitialTurn(                  |
|       Board board,                                | 
|       List<Tiles> hand,                           |
|       boolean isLastInvalid)                      |
| + TurnAction playTurn(Board board,                |
|       List<Tiles> hand, boolean isLastInvalid)    |
| + void gameOver(boolean hasWon)                   |
| + void results(boolean hasWonTournament)          |
+---------------------------------------------------+
                        ^
                        |
           +-----------------------------+
           | src.Player                      | 
           +-----------------------------+
           | - String name               |
           | - AvatarColor avatar        |
           | - Strategy strategy         |
           | - Rules currentRules        |
           | - List<IObserver> observers |
           | - Board currentBoard        |
           | - List<Tiles> currentTiles  |
           | - Action lastAction         |
           |                             |
           | [implemented methods]       |
           +-----------------------------+
```

### Strategy
The `Strategy` interface is used to represent a `src.Player`'s strategy during a game. This interface is used by a `src.Player` 
to get their moves for a game. 

We have two concrete implementations of a `Strategy` in `FirstS` and `SecondS`. Our `SecondS` strategy extends `FirstS`
because they have the same initial turn behavior.

```
+------------------------------------------------------------------------------------------------+
| Strategy <<interface>>                                                                         |
+------------------------------------------------------------------------------------------------+
| + InitialAction getInitialTurn(AvatarColor avatar, Board board, List<Tiles> hand, Rules rules) |
| + TurnAction getTurn(AvatarColor avatar, Board board, List<Tiles> hand, Rules rules)           |
+------------------------------------------------------------------------------------------------+
                                            ^
                                            |
                               +--------------------------+
                               | FirstS                   |
                               +--------------------------+
                                            ^
                                            |
                               +--------------------------+
                               | SecondS                  |
                               +--------------------------+
```

## Action
The `Action` abstract class and subclasses are used for passing a src.Player's 'action' data to other classes. 

The two concrete implementations of these classes, `InitialAction` and `TurnAction` are used for data storage and 
comparison. All their fields are final and immutable due to getters copying internal data when necessary. A `TurnAction`
 adds no extra fields or functionality to an `Action`, but an `InitialAction` also contains an `AvatarLocation`.

An `InitialAction` and a `TurnAction` statically depend on:
- `AvatarColor` : an enum used throughout the code to represent the avatar color of a player. One of WHITE, BLACK, RED, 
GREEN, BLUE.
- `Tiles` : (described below) used here to store the Tile the player would like to play. 
- `Facing` : (described below) used here to represent the direction to rotate the given tile

An `InitialAction` also statically depends on:
- `AvatarLocation`: a data class that stores the x, y, and port number of an avatar on the board. It stores the x and y 
as a `Posn` but they can also be received as integers

Neither of these objects have dynamic dependencies because they are used to store data and are immutable.


```
                    +-------------------------------+
                    | Action <Abstract Class>       |
                    +-------------------------------+
                    | - AvatarColor player          |
                    | - Tiles tile                  |
                    | - Facing facing               |
                    |                               |
                    | + String getPlayerAvatar()    |
                    | + Tiles getTileToPlace()      |
                    | + Facing getFacingDirection() |
                    +-------------------------------+
                                    ^
                                    |
                    +---------------+-------------------+
                    |                                   |
+-----------------------------------+           +-----------------+
| InitialAction                     |           | TurnAction      |
+-----------------------------------+           +-----------------+           
| - AvatarLocation location         |           
|                                   |
| + Posn getAvatarPlacement()       |
+-----------------------------------+
```

##Board
The Board is the representation of a Tsuro board, and holds game elements such as avatars and possible tiles. Our
representation of a Board is technically static, and can only be "modified" by other classes using our `BoardMutator`.

A `Board` statically depends on the following:
- `boardSize` This determines the overall size of a Tsuro Board.

A `Board` dynamically depends on the following:
- `AvatarLocation` A Board holds a map of AvatarColor to AvatarLocation named avatars. These avatarLocations are updated
whenever an avatar completes a turn.
- `tiles` A Board holds a map of Posn to Tiles that define which tiles are placed on the board. These tiles are updated
whenever an avatar places a new tile on its turn.
- `boardState` A Board relies on a BoardState that indicates both the state of the Game and the state of a Turn. (In 
retrospect, we should have split up the turn-states and board-states.)
    - `BoardState` A BoardState is an enum that can be one of the following:
        - HAS_ADJ
        - NO_ADJ
        - LOOP
        - COLLISION
        - GAME_OVER
    
## Tiles
The `Tiles` class represents a Tsuro tile. 

A `Tiles` object statically depends on the following:
- `Connection` : represents the "connection" (otherwise known as a 'path') between two ports on a Tsuro
 tile. Ports are stored as integers between 0 and 7 inclusive. A Tiles object contains an array of 4 Connection objects. 
- `Facing` : an enum used throughout the code to indicate a 'facing' direction. Facing is one of NORTH, EAST,
 SOUTH, or WEST. For tiles, this indicates the rotation direction. NORTH indicates no rotation, EAST represents a 90 
 degree rotation etc.

A `Tiles` object does not dynamically depend on anything, because it is just a complex data structure. `Tiles` are 
immutable.

Included for the `Referee` and simplicity of testing, we have a `TilesFactory` that stores all the data about the unique
 Tsuro tiles and returns them based on index. 
 
Most methods are omitted for simplicity:
 ```
 +---------------------+            +-------------------------------------+
 | Connection          |----+       | Tiles                               |
 +---------------------+    |       +-------------------------------------+
 | int a               |    +------>| - ArrayList<Connection> connections |
 | int b               |     +----->| + Tiles rotate(Facing facing)       |
 +---------------------+     |      | ...                                 |
                             |      +-------------------------------------+
 +------------------------+  |
 | Facing <<enumeration>> |--+
 +------------------------+
 | NORTH                  |
 | EAST                   |
 | SOUTH                  |
 | WEST                   |
 +------------------------+

```