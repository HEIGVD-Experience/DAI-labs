package ch.heigvd.GameServer;

import ch.heigvd.Game.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private final int MAX_CLIENTS = 2;
    private final List<ClientHandler> activeClients = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);
    private boolean isRunning = true;
    private Game currentGame;

    /**
     * Méthode permettant de démarrer un serveur de jeu.
     * @param serverIP = IP du serveur
     * @param serverPort = port du serveur
     */
    public void start(String serverIP, int serverPort) {
        try {

            // Transformation de serverIP en adresse IPv4
            InetAddress serverAddress = InetAddress.getByName(serverIP);

            try (ServerSocket serverSocket = new ServerSocket(serverPort, 1, serverAddress)) {
                System.out.println("Server waiting connection on " + serverAddress + ":" + serverPort);

                // Créer un thread pour gérer la commande d'arrêt
                new Thread(() -> handleShutdownCommand(serverSocket)).start();

                while (getIsRunning()) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New connection : " + clientSocket);

                    // Commence une partie si c'est la première connexion
                    if(activeClients.isEmpty()) {
                        currentGame = new Game();
                        System.out.println("Preparation of a new game.");
                    }

                    if (activeClients.size() < MAX_CLIENTS) {
                        // Utiliser le thread pool pour gérer la communication avec le client
                        ClientHandler clientHandler = new ClientHandler(clientSocket, currentGame, activeClients);
                        activeClients.add(clientHandler);
                        executor.submit(clientHandler);
                    } else {
                        System.out.println("Connection refused for : " + clientSocket);

                        // Refuser la connexion si le serveur est plein
                        PrintWriter refusedOut = new PrintWriter(clientSocket.getOutputStream(), true);
                        refusedOut.println("ERR_1");

                        // Fermer la socket du client refusé
                        clientSocket.close();
                    }
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        } catch (IOException e) {
            System.err.println("Impossible to parse your IP address.");
        } finally {
            // Arrêter le pool après que toutes les tâches ont été soumises
            executor.shutdown();
        }
    }

    /**
     * Méthode permettant de lire si nous recevons une commande shutdown sur le serveur
     * @param serverSocket = thread ouvert sur le serveur pour lire la commande shutdown
     */
    private void handleShutdownCommand(ServerSocket serverSocket) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter 'shutdown' to stop the server.");
            while (getIsRunning()) {
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("shutdown")) {
                    System.out.println("Server shutting down.");
                    stopServer(serverSocket);
                } else {
                    System.out.println("Unknown command. Enter 'shutdown' to stop the server.");
                }
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
        }
    }

    /**
     * Getter de la valeur isRunning
     * @return boolean
     */
    private synchronized boolean getIsRunning() {
        return isRunning;
    }

    /**
     * Méthode permettant l'arrêt du server
     * @param serverSocket = socket sur le serveur permettant son arret
     */
    private synchronized void stopServer(ServerSocket serverSocket) {

        if(activeClients.size() > 0) {
            for (ClientHandler ch : activeClients)
                ch.sendMessage("ERR", "500");
        }

        try {
            isRunning = false;
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
