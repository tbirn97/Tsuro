# Administrator

Develop a Tsuro tournament administrators. The task of the administrator is to manage the games for an unbounded number 
of players that have signed up for a tournament.

Describe an interface for the administrator and a method of how it might run the tournament and interact with the 
players and the referee.

## Interface for the Administrator

An administrator would need at least two public methods:
- `boolean register(src.Player p)` registers a player for the tournament, returns true if successful
- `List<src.Player> runTournament()` runs the tournament and returns a list of winners

## How the Administrator Might Run the Games

The simplest way to run a digital tournament would be to run one game at a time. An administrator could **run multiple 
games in parallel**, but here we just describe how a simple administrator might work.

In the `runTournament()` method, an administrator creates a `Referee` for the game they want to run. If they have more 
than 3 players registered, they call `startGame(List<Players> players)` with up to 5 players. The return value of 
`startGame` is the list of players who won. Once the tournament managers receives that list, it can add those players to
 the existing list of players in the tournament. The administrator then starts another game if it has at least 3 
 players. And so on. Once the administrator has less than 3 players in it's list, those are returned as the winners. 


### Parallel Games

In case an administrator implementation wants to run multiple games in parallel, we have included the pseudocode for a 
recursive function that starts a round of games. 

The following pseudocode assumes that `startGame` is a method that starts a game with the given players if there is a 
thread available to the administrator to do so. The thread would give some indication of termination so the 
administrator knows to add the results to their list of players still playing. This threading might modify the method 
below for better functionality, but the basic recursion and math is what we are showing.
```
// assumes n >= 3
void makeTournament(List<Players> players) {
   int n = players.size();
   if (n <= 5 && >= 3) {
        // base case
        startGame(players);
   } 
   else {
        if (n - 5 > 2) {
            startGame(players.sublist(0, 5));
            makeTournament(players.sublist(5, players.size()));
        }
        else if (n - 4 > 2) {
            startGame(players.sublist(0, 4));
            makeTournament(players.sublist(4, players.size()));
        }
        else {
            startGame(players.sublist(0, 3));
            makeTournament(players.sublist(3, players.size()));
        }
   }
}
```