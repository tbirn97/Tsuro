# Distributed Tsuro Protocol

At this point in the design, we do not consider observer components to be distributed. Game and tournament observers exist locally along with Administrator and Referee and player observer exist locally with Players.

For the distributed phase of Tsuro.com we will use a Remote Proxy Pattern. Our distributed protocol identifies two remote proxy objects, RemotePlayer and RemoteAdministrator.

## RemotePlayer

Represents a player that can be communicated with through TCP. A RemotePlayer will implement `PlayerInterface` and be used dynamically by a `Referee` and statically by an `Administrator` whenever calls to a player have to be made.

A RemotePlayer will be constructed with a `Socket connection` that is the TCP connection that it will send player method calls to and expect responses from.

### Methods

- All methods in `PlayerInterface`

These calls will all create TCP messages to the `connection` and parse the response into an object to return.

- `void close()`

This method will gracefully close the TCP socket. It should be called by the administrator anytime a player is removed from the tournament because of cheating or otherwise. The RemotePlayer must not throw an exception when this is called for a player that cheated.


## RemoteAdministrator

Represents a `Referee` and `Administrator` object that is local to players and can call player methods and return responses as a TCP message to a socket. A `RemoteAdministrator` will wait for TCP messages, translate these messages into calls that can be made on a `PlayerInterface`, and return a response to these calls back to the TCP socket.

A `RemoteAdministrator` is constructed as `RemoteAdministrator(Socket connection, PlayerInterface player)`. The `connection` is the TCP socket that the `RemoteAdministrator` will receive and send TCP messages from and to, and the `player` is the `PlayerInterface` that the administrator will call to decide how to respond to TCP queries.

### Methods

- `void listen()`

Will wait for TCP messages from its `socket`, call a corresponding `PlayerInterface` method, and searialize the method response into a new TCP message that will be sent back to the `socket`.

This method will initiate the loop described above and continue its wait and respond cycle until the socket is closed.

## Message Formats

**To conform with assignment specs we are currently using what is specified in this [document](http://www.ccs.neu.edu/home/matthias/4500-f19/10.html)**

The messages in this specification are directly analagous to the calls defined in the [local-protocol](http://www.ccs.neu.edu/~matthias/4500-f19/localp.html)

```
Referee - - - - - - - > Player
         playing-as
```

The referee telling the player the color avatar they are playing as.

```json
{
  "method": "playing-as",
  "avatar-color": COLOR
}
```

- A COLOR is a string defined [here](http://www.ccs.neu.edu/~matthias/4500-f19/tiles.html)

No response is expected.

```
Referee - - - - - - - > Player
         other-players
```

The referee telling the player the other avatar colors in the game.

```json
{
  "method": "other-players",
  "avatar-colors": LIST-OF-COLOR
}
```

- A LIST-OF-COLOR is an array of size 2 - 4 COLOR elements

No response is expected.

```
Referee - - - - - - - > Player
         initial
```

The referee asking the player for their first turn.

```json
{
  "method": "initial",
  "board": STATE-PATS
  "hand": INITIAL-CHOICES
}
```
- A STATE-PATS is a JSON array of state-pat JSON arrays as defined [here](http://www.ccs.neu.edu/~matthias/4500-f19/4.html#%28tech._state._pat%29)
- A INITIAL-CHOICES is an array of size 3 tile-index integers as defined [here](http://www.ccs.neu.edu/~matthias/4500-f19/tiles.html#%28tech._tile._pat%29)

This call expects an initial-response message:

```
Referee < - - - - - - - Player
         initial-response
```

The player responding to an 'initial' TCP message.

```json
{
  "placement": {
    "tile": {
      "index": TILE-INDEX, 
      "rotation": DEGREES
    },
    "port": PORT,
    "position": {
      "x": INTEGER,
      "y": INTEGER
    }
  }
}
```

- A DEGREES is an integer that is either 0, 90, 180, or 270.
- A PORT is a string that is one of: "A", "B", "C", "D", "E", "F", "G", or "H".
- A TILE-INDEX is an integer tile-index as defined [here](http://www.ccs.neu.edu/~matthias/4500-f19/tiles.html#%28tech._tile._pat%29)

```
Referee - - - - - - - > Player
         intermediate
```

The referee asking the player for an intermediate turn.

```json
{
  "method": "intermediate",
  "board": STATE-PATS
  "hand": INTERMEDIATE-CHOICES
}
```

- A INTERMEDIATE-CHOICES is an array of size 2 TILE-INDEX integers.

```
Referee < - - - - - - - Player
         intermediate-response
```

The player responding to an 'intermediate' TCP message.

```json
{
  "placement": {
    "tile": {
      "index": TILE-INDEX, 
      "rotation": DEGREES
    }
  }
}
```


```
Administrator - - - - - - - > Player
         end-of-tournament
```

The administrator telling the player if they won the tournament or not.

```json
{
  "method": "end-of-tournament",
  "player-won": BOOLEAN
}
```

No response is expected.