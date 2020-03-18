```
PlayerAIOne                 Game
     |     Join Request       |
     |----------------------->|
     |       TokenColor       |
     |<-----------------------|
     |                        |
     |                        |
  +->|    Your Turn (info)    | (info includes board, hand and token placement)
  |  |<-----------------------|
  |  |    Tile Placement      |
  |  |----------------------->|
  |  |    Success/Failure     |
  |  |<-----------------------|
  |  |                        |
  |  |                        |<--------------> Other player turns
  +--|                        |
     |     You lost/won!      |
     |<-----------------------|
     |                        |
```     
     