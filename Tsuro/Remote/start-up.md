# Changes Necessary For Distributed Tsuro


**fe67c1e2c083e63e61ccaff2c6ae96153235d241 Refactored player interface to match the local protocol**

We had to change the PlayerInterface so that it could be used with the local protocol. We split
the welcome() method into a method for getting the avatar's color and a method for getting the
other avatars playing in the game. To avoid a very large refactor, we kept the getName and
getAvatar methods in the PlayerInterface and mocked them in the RemoteUser.

# Client Server Start-Up Phase

### Server Startup

A Tsuro tournament is started by calling a method in `Server` that accepts a port that the server should bind to.

After binding, the server will enter a registration period. During this time, any player can connect by TCP to join the tournament. After the TCP connection has been initialized, the player must send a `register` message to the server that included the player's preferred name. If this message is not received during the registration period, the player will not be a part of the tournament. The registration period closes after 1 minute or after 20 players have been successfully registered.

After the registration period, all outstanding TCP connections that did not get registered will be ended by the server.

Finally, the registered players will begin the tournament.

````
                Server                                                                                Client N          
                   |                                                                                     |                           
bind on port       |                                                                                     |                           
                   |                                                                                     |                           
listen for         |                                                                                     |                           
connection         |                                                                                     |                           
                   |                                                                                     |                           
                   |                                                                                     |                           
TCP accept         |<------------------------------------------------------------------------------------+ TCP connect               
                   |                                                                                     |                           
TCP connect        +------------------------------------------------------------------------------------>| TCP accept                
Create Remote User +----- give socket---> Remote User                                                    |                           
                   |                           |                                               Player    |                  
                   | -------- Admin            |                                                 |<------+ create           
                   |            |              |                      Remote Admin               |       |                           
add to player list |--give RU-->|              |                            |<--- Socket/Player -^-------+                           
                   |            |              |                            |                    |       |                           
                   |--Start --->|              |                            |<--------- listen---^-------+        
                   |            |------------->+-------- playing-as ------->+------------------->|       |                         
                   |            |              |                            |                    |       |
                   |            |<-------------+<------- void response -----+<-------------------|       |                           
                  ...          ...            ...                          ...                  ...     ...                           
TCP close          | -end tournament result -> |                            |                    |       |            
                   |            |              |                            |                    |       |         
````

Client 1 is representative of a registered player and client N is representative of a player
that connected but did not send a register message.

### Register Message Format
JSON:
```json
["register", [STRING]]
```


