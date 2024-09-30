# Protocol d'application TeleText

## 1 - Overview
- The Teletext protocol is designed to enable the transmission, reception, and display of information like a teletext
- The server listens to news emitters and stores the news to allow clients to access and view them.
- The client connects to the teletext server and requests information about different categories (UDP unicast).
- The emitters are independent of the server or clients (UDP multicast).


## 2 - Transport protocol
- The Teletetx protocol uses the `UDP protocol`.
- The server runs on the ports `5000` and `5001`. (`5000` -> listening to the news emitters and `5001` -> listening to the clients)
- The client establishes the connection with the server using the `client` command **(unicast, request response)**.
- The emitters send news across the network and the server store them, they use  **(multicast, fire and forget)**.
- The emitters mulicast addresses are form `239.0.0.1` to `239.0.0.4`.
- The connection between the client and the server stops either when the client enters the command to disconnect or if an error occurs.
- There is no connection between the server and the emitters because that's UDP multicast.


## 3 - Messages

### 3.1 - Servers
#### Start the server 
Command : `server -n <port to receive news (def => 5000)> -c <port to make news available (def => 5001)>`
|Response|Detail|
| ---- | ---- |
|_no response_|The teletext server will wait for new client connections and retrieve the news emitted by the emitters|


### 3.2 - Emitters
#### Start the news emitter 
Command : `emitter <type> [weather, heig, politic, sport] -n <port to send news (def => 5000)>`
|Response|Detail|
| ---- | ---- |
|_no response_|The news emitter will start multicasting news on the network|

#### Send a news 
Command : `<importance> [NW = news, BK = breaking news]  <type> [WEATHER, HEIG, POLITIC, SPORT] <newsText>`
|Response|Detail|
| ---- | ---- |
|_no response_|The news is sent to the multicast address|


### 3.3 - Clients
#### Connect the client to the teletext server
Command: `client -c <port to get news (def => 5001)>`
|Response|Detail|
| ---- | ---- |
|_no response_|Try to connect to the teletext server|

#### At the connection with the server (auto send)
Command: `welcome`
|Response|Detail|
| ---- | ---- |
|`TXT <message>`|Display a welcome message|

#### Get help to know what you can do
Command: `help`
|Response|Detail|
| ---- | ---- |
|`TXT <message>`|Print a menu with all available commands|

#### Display news categories
Command: `menu`
|Response|Detail|
| ---- | ---- |
|`TXT <message>`|List all existing categories|

#### Display the news about a selected category
Command: `list <category> [weather, heig, politic, sport, breaking, latest]`
|Response|Detail|
| ---- | ---- |
|`TXT <message>`|List the news for the selected category|
|`ERR 200`|Currently, there is no news in this category|
|`ERR 201`|Currently, this category does not exist|

#### Display number of news for a selected category
Command: `count <category> [weather, heig, politic, sport, breaking, latest]`
|Response|Detail|
| ---- | ---- |
|`TXT <message>`|Print the number of the news for the selected category|
|`ERR 201`|Currently, this category does not exist|


#### Disconnect the client from the teletext server
Command: `exit`
|Response|Detail|
| ---- | ---- |
|_no response_|Disconnect the client from the teletext server|

#### If you try an unkown command
Command: `???`
|Response|Detail|
| ---- | ---- |
|`ERR 100`|Unknown command|



## 4 - Examples

![protocol](/docs/protocol.drawio.png)
