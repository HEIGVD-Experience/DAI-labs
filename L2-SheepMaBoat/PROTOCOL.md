# SheepMaBoat application's protocol

## 1 - Overview
- The SheepMaBoat protocol is meant to play a battleship game over the network. It's a client-server protocol.   
- The client connects to a server and request to play a new game if another player is also ready to play.    
- The server manage the game's logic between the two players.  
- When a game is over both player are logged out. They can reconnect if they want to play another game or with another player.   
- We expect that no one disconnects during the game.

## 2 - Transport protocol
- The SheepMaBoat protocol uses the `TCP protocol`. The server runs on the `port 11111`.  
- The client has to know the IP address of the server to connect to. **The client establishes the connection with the server.**   
- The connection ends when a game is over. If you want to replay or play with another player you will have to reconnect.

## 3 - Messages

### Server
Start the server : `server -i <ip (default -> 127.0.0.1)> -p <port (default -> 11111)>`
|Response|Detail|
| ---- | ---- |
|`Server waiting connection on <adrress>:<port>`|The server is waiting for a connection from a client.|

Shutdown the server : `shutdown`
|Detail|
| ---- |
|It will shut the server down.|


### Client
#### Connection
Command: `client -i <ip> -p <port (default -> 11111)>`
|Response|Detail|
| ---- | ---- |
|`CMD_<welcome message>`|A welcome message and you what you have to do.|
|`ERR_100`|There is too much people on the server.|

#### Register username
Command: `username <username>`
|Response|Detail|
| ---- | ---- |
|`CMD_<username accepted message>`|The username as been added and you what you have to do.|
|`ERR_200`|Use the username command|
|`ERR_201`|The username is too short sorry.|
|`ERR_202`|The username is too long sorry.|
|`ERR_203`|The username doesn't fit our policies.|

#### Start game
Command: `start`
|Response|Detail|
| ---- | ---- |
|`TXT_<the player's playboard>`|When both player are ready the one who send `start` first will start the game and recieve his playboards.|
|`ERR_204`|You need to have a username.|
|`ERR_300`|You have to enter start to start the game.|

#### Attack sheep
Command: `shoot <position -> B1>`
|Response|Detail|
| ---- | ---- |
|`TXT_<the player's updated playborads>`|Your updated playboards with your last shoot and the opponent shoot.|
|`ERR_400`|You need to use the command shoot.|
|`ERR_401`|You need to use the command shoot.|
|`ERR_402`|Your shoot is out of the play board.|

#### Finish game
|Response|Detail|
| ---- | ---- |
|`EGE_W`|The game is over and you won.|
|`EGE_L`|The game is over and you lose.|

#### Quit game (auto send command when the game is over)
Command: `GoodBye`
|Detail|
| ---- |
|You will leave the server.|

## 4 - Example

### Sequence diagram

![SheepMyBoatProtocl](/SheepMyBoatProtocol.drawio.png)



