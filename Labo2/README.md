# SheepMaBoat

```
                             v  ~.      v
                    v           /|
                               / |          v
                        v     /__|__
                            \--------/
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`~~~~~~'~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
```
## Description
SheepMaBoat is a TCP two players game application. It's a twin of the Battleship game.       
The app use the SheepMaBoat protocol, you can find the information about it [here.](/PROTOCOL.md)

## Building the app
The app use maven  so in order to build and package the app use this command.

```sh
# Download the dependencies
./mvnw dependency:resolve

# Package the application
./mvnw package
```

## Running the app
Download the .jar file into the lastest [release](https://github.com/gwendalpiemonte/SheepMaBoat/releases/tag/v1.0) and run the command explained under this text. You can also download the source code and build the code with Maven but make sure to have included all dependencies.

### Launch the game server

The port's value is by default `11111`

```sh
java -jar <path-to-jar> server -a <address> -p <port>

# Example
java -jar SheepMaBoat-v1.0.jar server --address 127.0.0.1
```

### Launch a player(client)

```sh
java -jar <path-to-jar> client -a <address> -p <port>

# Example
java -jar SheepMaBoat-v1.0.jar client --address 127.0.0.1 --port 11111
```

### Description

- `-a, --address <IP address>`: IP address to connect.
- `-p, --port <port>`: Port to connect.
