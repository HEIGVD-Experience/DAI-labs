package ch.heigvd.NewsEmitter;

import ch.heigvd.Shared.*;

import java.io.IOException;
import java.net.*;
import java.util.Random;

public class NewsEmitter {

    /**
     * Méthode permettant de gérer l'envoie de news sur le réseau en multicast
     * @param newsType = type de la news à envoyer
     * @param emissionPort = port sur lequel émettre la news
     */
    public void start(String newsType, int emissionPort) {

        newsType = newsType.toUpperCase();

        if(!TypeNews.mapTypeIP.containsKey(newsType)){
            System.err.println("This type of news doesn't exist!");
        } else {

            try (DatagramSocket datagramSocket = new DatagramSocket()) {

                // Boucle d'emission
                while (true) {
                    String news = switch (newsType) {
                        case "WEATHER" -> "WEATHER " + NewsCreator.getRandomWeatherNews();
                        case "HEIG" -> "HEIG " + NewsCreator.getRandomHeigNews();
                        case "POLITIC" -> "POLITIC " + NewsCreator.getRandomPoliticNews();
                        case "SPORT" -> "SPORT " + NewsCreator.getRandomSportNews();
                        default -> "";
                    };

                    // Choix des Breaking news
                    Random random = new Random();
                    int randomNumber = random.nextInt(5);
                    if (randomNumber == 4) {
                        news = "BK " + news;
                    } else {
                        news = "NW " + news;
                    }

                    // Creation du datagramm
                    byte[] data = news.getBytes();
                    InetAddress address = InetAddress.getByName(TypeNews.mapTypeIP.get(newsType));
                    DatagramPacket packet = new DatagramPacket(data, data.length, address, emissionPort);

                    // Envoie du datagramm
                    datagramSocket.send(packet);
                    System.out.println("> News sent (" + newsType + ") on " + address);


                    randomNumber = (random.nextInt(26) + 5) * 1000; // de 5 a 30 sec

                    // Attente avant d'envoyer la prochaine nouvelle (10 secondes)
                    Thread.sleep(randomNumber);
                }

            } catch (IOException | InterruptedException e) {
                e.getMessage();
            }
        }
    }
}