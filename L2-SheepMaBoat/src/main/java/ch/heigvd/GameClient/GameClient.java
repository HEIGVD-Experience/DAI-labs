package ch.heigvd.GameClient;

import ch.heigvd.Game.PlayBoard;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    Socket socket;
    PrintWriter out;
    Scanner in;
    Scanner consoleInput;
    BufferedReader bin;
    final static String EOT = "END";

    /**
     * Méthode permettant de se connecter et ecouter le serveur
     * @param serverIP = IP de serveur
     * @param serverPort = port sur lequel communiquer avec le serveur
     */
    public void start(String serverIP, int serverPort) {

        try {

            socket = new Socket(serverIP, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
            bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            consoleInput = new Scanner(System.in);

            // Boucle pour permettre à l'utilisateur d'envoyer des messages au serveur
            while (true) {

                // Lire la réponse du serveur
                String line;
                StringBuilder textOut = new StringBuilder();
                while ((line = bin.readLine()) != null && !line.equals(EOT)) {
                    textOut.append(line);
                }

                handleMessage(textOut.toString().split("_"));

                // Ecris le message à envoyer au serveur
                String userMessage = consoleInput.nextLine();

                // Envoyer le message au serveur
                out.println(userMessage);

            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Méthode permettant de filter les réponses du serveur
     * @param messages = message recu du serveur
     */
    private void handleMessage(String[] messages) {
        if(messages[0].equals("EGE")){
            // Fermer la connexion
            closeSocketConnexion(messages[1]);
        }
        else if(messages[0].equals("ERR")){
            handleError(messages[1]);
        }
        else if (messages[0].equals("CMD")) {
            for(int i = 1 ; i < messages.length ; ++i)
                System.out.print(messages[i] + " ");
            System.out.println();
        }
        else if(messages[0].equals("TXT")) {
            for(int i = 1 ; i < messages.length ; ++i)
                System.out.println(messages[i]);
        }
    }

    /**
     * Méthode permettant de d'afficher au client les erreurs en fonction du code d'erreur
     * @param errorCode = code d'erreur
     */
    private void handleError(String errorCode) {
        switch(errorCode) {
            case "100":
                System.err.println("The server is full. Please try later.");
                break;
            case "200":
                System.err.println("Use command username <username [3 - 15]>.");
                break;
            case "201":
                System.err.println("The username is too short. Use command username <username [3 - 15]>.");
                break;
            case "202":
                System.err.println("The username is too long. Use command username <username [3 - 15]>.");
                break;
            case "203":
                System.err.println("The username doesn't fit our policies. Use command username <username [3 - 15]>.");
                break;
            case "204":
                System.err.println("Before using start command you need to choose a username. Use command username <username [3 - 15]>.");
                break;
            case "300":
                System.err.println("You have to use the command start.");
                break;
            case "400":
            case "401":
                System.err.println("You need to use the command shoot <position -> B1>");
                break;
            case "402":
                System.err.println("Your shoot is out of the play board. Please shoot between : " + PlayBoard.minColumn + PlayBoard.minRow + " - " + PlayBoard.maxColumn + PlayBoard.maxRow);
                break;
            case "500":
                System.err.println("The server closed the connection.");
                break;
            default:
                System.err.println("Error, something went wrong.");
                break;
        }
    }

    /**
     * Méthode permettant de deconnecter le client
     * @param result = code de fin (W = won, L = lose)
     */
    private void closeSocketConnexion(String result) {

        if(result.equals("W")){
            System.out.println("You won ! \nThe game is over, thanks for playing !");
        }
        else if(result.equals("L")){
            System.out.println("You lose ! \nThe game is over, thanks for playing !");
        }

        out.println("GoodBye");

        try {
            out.close();
            consoleInput.close();
            bin.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}