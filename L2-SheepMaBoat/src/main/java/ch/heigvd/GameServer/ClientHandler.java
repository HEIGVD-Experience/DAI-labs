package ch.heigvd.GameServer;

import ch.heigvd.Game.Game;
import ch.heigvd.Game.PlayBoard;
import ch.heigvd.Game.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    // Pour gérer les échanges au travers du socket
    private final Socket clientSocket;
    private PrintWriter out;
    private Scanner in;

    // Attributs static lié au jeu
    private static boolean gameIsRunning = false;
    private static boolean oneOrTwo = false; // Si vrai joueur 1 joue sinon joueur 2

    // Attributs lié au jeu
    private final String EOT = "END";
    private final List<ClientHandler> activeClients;
    private Game currentGame;
    private Player player = null;
    private boolean isReady = false;
    private String state = "";

    /**
     * Constructeur de la classe client handler permettant de gérer les connexions de chaques clients.
     * @param socket = socket attribuée par le serveur
     * @param game = partie en cours crée dans le serveur
     * @param activeClients = liste contenant les ClientHandler des deux joueurs connectés
     */
    public ClientHandler(Socket socket, Game game, List<ClientHandler> activeClients) {
        this.clientSocket = socket;
        this.currentGame = game;
        this.activeClients = activeClients;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Methode runnable lancée à la création d'un ClientHandler.
     */
    @Override
    public void run(){
        try {
            sendMessage("CMD", "Welcome to Battlesheep! Please choose a username. Use the command: username <username>");

            while (true) {
                String[] clientMessage = in.nextLine().split(" ");
                handleMessage(clientMessage);
                System.out.println("Client " + clientSocket + " says: " + Arrays.toString(clientMessage));
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * Getter pour la variable isReady.
     * @return true si prêt, false sinon
     */
    public boolean getIsReady() {
        return isReady;
    }

    /**
     * Getter du joueur en cours
     * @return
     */
    public Player getPlayer() { return this.player; }

    /**
     * Méthode permettant d'envoyer un message en respectant les séparateurs ainsi que le
     * EOT (End Of Tranmissions)
     * @param type = le type de message [CMD, TXT, EGE, ERR]
     * @param message = le contenu du message ou le numéro d'erreur
     */
    public void sendMessage(String type, String message) {
        out.println(type + "_");
        out.println(message + "_");
        out.println(EOT);
    }

    /**
     * Méthode permettant de gérer les messages envoyé par le client
     * @param message = contient le message du client
     */
    public synchronized void handleMessage(String[] message) {

        // Si la partie n'as pas encore commencé
        if(!gameIsRunning) {

            // Si le joueuer n'est pas encore set alors il doit ajouter un username
            if(player == null) {
                if (message[0].equals("username")) {
                    addUsername(message.length < 2 ? "" : message[1]);
                } else if (message[0].equals("start")) {
                    sendMessage("ERR", "204");
                } else{
                    sendMessage("ERR", "200");
                }
            // Si le joueur est déjà enregistré alors il doit écrire start pour commencer la partie
            } else {
                if (message[0].equals("start")) {
                    startGame();
                }
                else{
                    sendMessage("ERR", "300");
                }
            }
        // Si la partie est en cours
        } else {

            // Si la liste ne contient plus 2 joueurs pendant la partie
            if(activeClients.size() < 2) {
                closeConnection();
            }

            // Si le joueur a bien envoyé la commande shoot
            if (message[0].equals("shoot")) {

                // Si la position ne fait pas 2 char
                if(message.length == 2) {
                    if(message[1].charAt(0) >= PlayBoard.minColumn && message[1].charAt(0) <= PlayBoard.maxColumn &&
                            message[1].charAt(1) >= PlayBoard.minRow && message[1].charAt(1) <= PlayBoard.maxRow) {
                        shootSheep(message[1]);
                    } else {
                        sendMessage("ERR", "402");
                    }
                } else {
                    sendMessage("ERR", "401");
                }
            } else{
                sendMessage("ERR", "400");
            }
        }
    }

    /**
     * Essaye d'ajouter un username
     * @param message = contient le prétendu nom d'utilisateur
     */
    private synchronized void addUsername(String message) {
        if (message.length() <= 15 && message.length() >= 3) {
            this.player = new Player(message);
             sendMessage("CMD", "Welcome, " + message + "! Please write start when you are ready! Use the command: start");
        } else if(message.length() < 3) {
            sendMessage("ERR", "201");
        } else if(message.length() > 15) {
            sendMessage("ERR", "202");
        } else {
            sendMessage("ERR", "203");
        }
    }

    /**
     * Permet de démarrer la partie
     */
    private void startGame() {
        this.isReady = true;

        // Si deux clients sont connectés et que les deux sont prets
        if(activeClients.size() == 2 && activeClients.get(0).getIsReady() && activeClients.get(1).getIsReady()) {

            // Ajoute les joueurs dans la partie
            currentGame.setPlayer(activeClients.get(0).getPlayer());
            currentGame.setPlayer(activeClients.get(1).getPlayer());

            // Démarre la partie
            gameIsRunning = true;

            // Le premier connecté joue en premier
            activeClients.get(0).sendMessage("TXT", currentGame.printGame(activeClients.get(0).getPlayer()) + "_"
                    + "Last    shoot in : _"
                    + "Oponent shoot in : _"
                    + "Use the command: shoot <column [A-E]> <row [1-5]>");
        }
    }

    /**
     * Permet de tirer un mouton sur la position désignée
     * @param message = contient la position choisie
     */
    private void shootSheep(String message) {

        char column = message.charAt(0);
        int row = Integer.parseInt(message.substring(1));

        // Si la position fait bien 2 de longueur
        if(message.length() == 2) {

            // On change le joueur qui joue
            oneOrTwo = !oneOrTwo;

            // On définit qui a tiré et qui a reçu le tir
            int currentPlayer = oneOrTwo ? 0 : 1;
            int waitingPlayer = oneOrTwo ? 1 : 0;

            // Joue un round du jeu
            activeClients.get(currentPlayer).state =
                    currentGame.playRound(activeClients.get(currentPlayer).getPlayer(),
                            activeClients.get(waitingPlayer).getPlayer(), column, row);

            // Si ce tir apporte la victoire, alors on arrête la partie et deconnecte les personnes
            if(currentGame.isHasWinner()) {
                activeClients.get(currentPlayer).sendMessage("EGE", "W");
                activeClients.get(waitingPlayer).sendMessage("EGE", "L");
                closeConnection();
            } else {
                activeClients.get(currentPlayer).state = column + "" + row + " -> "  + activeClients.get(currentPlayer).state;
                activeClients.get(waitingPlayer).sendMessage("TXT", currentGame.printGame(activeClients.get(waitingPlayer).getPlayer()) + "_"
                        + "Your last shoot in : " + activeClients.get(waitingPlayer).state + "_"
                        + "Oponent   shoot in : " + activeClients.get(currentPlayer).state + "_"
                        + "Use the command : shoot <column [A-E]> <row [1-5]>");}
        }
    }

    /**
     * Permet de déconnecter complètement la partie et de se préparer pour une nouvelle
     */
    private void closeConnection() {
        for(ClientHandler ch : activeClients) {
            ch.player = null;
            ch.isReady = false;
            ch.oneOrTwo = false;
            ch.state = "";
            ch.gameIsRunning = false;
        }
        activeClients.clear();
    }
}

