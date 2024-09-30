package ch.heigvd.NewsServer;

import ch.heigvd.Shared.*;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NewsServer {

    // Structure permettant d'insérer efficacement en début de containeur
    private final List<News> newsList = Collections.synchronizedList(new LinkedList<>());

    /**
     * Méthode permettant de démarrer un NewsServer qui gère deux thread.
     * Le premier thread: gère la réception des messages envoyés par les différents émetteurs
     * Le deuxième thread: gère la connexion des clients sur le serveur
     */
    public void start(int portNews, int portClient) {
        // The number of threads in the pool must be the same as the number of tasks you want to run in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        try {
            executorService.submit(new NewsReceiver(portNews)); // Start the first task
            executorService.submit(new ClientServer(portClient)); // Start the second task

            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Wait for termination
        } catch (Exception e) {
            e.getMessage();
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * Classe implémentant l'interface Runnable et permet de démarrer le serveur récupèrant les news.
     */
    public class NewsReceiver implements Runnable{

        int serverPort;

        public NewsReceiver(int listeningPort) {
            serverPort = listeningPort;
        }

        @Override
        public void run() {
            try(MulticastSocket socket = new MulticastSocket(serverPort);) {

                System.out.println("The server has started to listening to news!");

                // Joindre chaque groupe multicast
                for (String type : TypeNews.mapTypeIP.keySet()) {
                    InetAddress group = InetAddress.getByName(TypeNews.mapTypeIP.get(type));
                    socket.joinGroup(group);
                }

                byte[] buffer = new byte[1024];

                // Récupère les données venant des émetteurs
                while (true) {

                    // Récéption de la news
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    // Traitement des données reçues
                    String received = new String(packet.getData(), 0, packet.getLength());
                    String[] splitted = received.split(" ");

                    // Ajoute une news dans notre structure de données
                    News newsToAdd = new News(splitted[1], splitted, splitted[0].contains("BK"));
                    newsList.add(0, newsToAdd);

                    // Print de la news reçue sur le serveur
                    System.out.println("Received: " + newsToAdd);
                }

            } catch (IOException e) {
                e.getMessage();
            }
        }
    }

    /**
     * Classe implémentant l'interface Runnable et permet de démarrer les échanges avec les clients
     */
    public class ClientServer implements Runnable{

        int serverPort;

        public ClientServer(int listeningPort) {
            serverPort = listeningPort;
        }

        private final static String TXTCODE = "TXT ", ERRCODE = "ERR ";

        @Override
        public void run() {

            try (DatagramSocket socket = new DatagramSocket(serverPort);) {
                while (true) {

                    // Attente du message du client
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);

                    String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Client " + receivePacket.getAddress() + " says : " + clientMessage);

                    // Réponse au client
                    String responseMessage = handleMessage(clientMessage.split(" "));
                    byte[] sendData = responseMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    socket.send(sendPacket);
                }

            } catch (Exception e) {
                e.getMessage();
            }
        }

        /**
         * Méthode permettant de gérer les messages reçus par les utilisateurs
         * @param clientMessages = tableau de messages splité sur le char " "
         * @return le message à envoyer au client
         */
        private String handleMessage(String[] clientMessages) {
            return switch (clientMessages[0]) {
                case "welcome" -> printCommandMenu();
                case "help" -> TXTCODE + printHelpMenu();
                case "menu" -> printNewsMenu();
                case "list" -> clientMessages.length == 2 ? listNews(clientMessages[1]) : ERRCODE + "201";
                case "count" -> clientMessages.length == 2 ? countNews(clientMessages[1]) : ERRCODE + "201";
                default -> ERRCODE + "100";
            };
        }

        /**
         * Prépare le message d'accueil
         * @return le message à envoyer au client
         */
        private String printCommandMenu() {
            return TXTCODE + "TELETEXT - The only way to inform you!\n" +
                    "Here you will have different canals in which you will find the latest news.\n" +
                    "----------------------------------------------- \n" +
                    printHelpMenu();
        }

        /**
         * Prépare le menu des commandes
         * @return le message à envoyer au client
         */
        private String printHelpMenu() {
            return "A summary of available commands : \n" +
                    "- help : print a menu with all available commands\n" +
                    "- menu : list all existing categories\n" +
                    "- list <category> : list the news for the selected category\n" +
                    "- count <category> : print the number of the news for the selected category\n" +
                    "- exit : quit the application\n ";
        }

        /**
         * Prépare la liste de type de news disponibles
         * @return le message à envoyer au client
         */
        private String printNewsMenu() {
            StringBuilder output = new StringBuilder();
            output.append(TXTCODE + "Please choose a news category to list with command list <category> or to count with count <category> \n");
            for (String key : TypeNews.newsListUserChoice) {
                output.append(key).append("\n");
            }
            return output.toString();
        }

        /**
         * Prépare la liste de news à envoyer au client en fonction de la catégorie choisie
         * @param category = catégorie de news a prendre en compte
         * @return le message à envoyer au client
         */
        private String listNews(String category) {
            StringBuilder output = new StringBuilder();
            if(checkCategory(category).contains(ERRCODE))
                return checkCategory(category);
            for (News news : newsList) {
                if(Objects.equals(news.getType(), category.toUpperCase()) ||
                        (Objects.equals(category.toUpperCase(), "BREAKING") && news.isBreakingNews()) ||
                        (Objects.equals(category.toUpperCase(), "LATEST"))) {
                    output.append(news).append("\n");
                }
            }
            if(output.isEmpty())
                output.append(ERRCODE + "200");
            else
                output.insert(0, TXTCODE);

            return output.toString();
        }

        /**
         * Prépare le comptage de news dans la cotegorie choisie
         * @param category = catégorie de news a prendre en compte
         * @return le message à envoyer au client
         */
        private String countNews(String category) {
            int count = 0;
            if(checkCategory(category).contains(ERRCODE))
                return checkCategory(category);
            for (News news : newsList) {
                if(Objects.equals(news.getType(), category.toUpperCase()) ||
                        (Objects.equals(category.toUpperCase(), "BREAKING") && news.isBreakingNews()) ||
                        (Objects.equals(category.toUpperCase(), "LATEST"))) {
                    count++;
                }
            }
            return TXTCODE + count + " news \n";
        }

        /**
         * Check si la catégorie choisie est existante
         * @param category = catégorie de news a prendre en compte
         * @return le message à envoyer au client
         */
        private String checkCategory(String category) {
            if(!TypeNews.newsListUserChoice.contains(category.toUpperCase()))
                return ERRCODE + "201";
            return "";
        }
    }
}