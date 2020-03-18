# Code base evaluation

## (1) How we found the corresponding components starting at the README.md file 

When we originally were given our README for this project, it was incomplete and only had documentation on the referee component. After reaching out to Michael we were able to get an updated version of the README. With the updated README, none of the component's locations within the project were documented so we had to search the Common and Admin packages to find the files containing the board, referee and rules components. 

## (2) How easily the dynamic description helped you relate these three pieces

The README never specifies how they are defining static for dynamic dependencies and the notation was confusing in some cases. At some points the README specifies the components are dependent on their fields while other times they specify actual classes that are imported by components. We operated under the assumption that when a field was deemed static then it would not change during execution while if it is dynamic then it may change throughout play.

The README also had some discrepancies with the code. For example, the README states that the referee receives a list of PlayerInterface while in the code, the referee has an empty constructor and receives players through a startGame method.

When examining how the Board and Referee interact, the README states that the Board is constantly changing throughout the game. However, their Board class is immutable and it is the Referee's board field that is changing. The README does not make this easy to understand because they refer to the Board class and the referee's board field interchangeably.

When determining how Rules and Referees interact, The interaction between Rules and Referees is fairly straightforward but the README states that the Rules class is immutable which is not reflected in the code. The Rules class has addXRule methods that changes the Rule's interior Rule lists. While these methods aren't called outside of tests, they violate the README's assertions and, because players are given a direct reference to the Referee's Rules, allow players to change the rules for all players in a game.

The Board component is deemed static in the README but the Board is still given dynamic dependencies which seemed contradictory. The README also states that a board can only be "modified" by a BoardMutator. The BoardMutator creates a new board instance based on a previous board instance but this is not clear from the language in the README. The README also neglects to document anything about the BoardMutator component other than the fact that it "modifies" boards.


## (3) Changes so that if a player places a tile that causes a collision, the tile is placed and the acting player is eliminated.

Currently the Board class stores a BoardState object that "indicates both the state of the Game and the state of a Turn". One of the BoardStates already tracks if collisions occur. We would then just have to add a check in referee after each turn to see if a collision occurred as the result of a player's turn. If this is the case, we can then remove the player from the game. This would be a fairly trivial change of about 5 lines in the referee but would be dependent on the implementation of the Board correctly updating BoardStates. If the Collision state is overridden by current or future BoardStates that could be implemented, then this approach would break and we would have to do a larger refactor to make BoardState more robust and capable of representing multiple possible states.



