package ch.heigvd.PicoCLI;

import picocli.CommandLine;

@CommandLine.Command(
        name = "Teletext - A new way to be kept informed!",
        version = "1.0",
        description = "Start what you want!",
        subcommands = { PicoCLI.NewsEmitter.class, PicoCLI.NewsServer.class, PicoCLI.NewsClient.class },
        mixinStandardHelpOptions = true)
public class PicoCLI{

    @CommandLine.Command(
            name = "emitter",
            version = "1.0",
            description = "Start the emitters. [weather, heig, politic, sport]",
            mixinStandardHelpOptions = true)
    public static class NewsEmitter implements Runnable{

        @CommandLine.Parameters(description = "Type of news you want to emitt.")
        private String newsType;

        @CommandLine.Option(
                names = {"-n", "--news"},
                description = "Port to listen news.")
        private int portNews = 5000;

        @Override
        public void run() {
            ch.heigvd.NewsEmitter.NewsEmitter e = new ch.heigvd.NewsEmitter.NewsEmitter();
            e.start(newsType, portNews);
        }
    }

    @CommandLine.Command(
            name = "server",
            version = "1.0",
            description = "Start the server that retrieve news emitted and exposed them to clients.",
            mixinStandardHelpOptions = true)
    public static class NewsServer implements Runnable{
        @CommandLine.Option(
                names = {"-n", "--news"},
                description = "Port to listen news.")
        private int portNews = 5000;
        @CommandLine.Option(
                names = {"-c", "--client"},
                description = "Port to listen clients.")
        private int portClient = 5001;
        @Override
        public void run() {
            ch.heigvd.NewsServer.NewsServer server = new ch.heigvd.NewsServer.NewsServer();
            server.start(portNews, portClient);
        }
    }

    @CommandLine.Command(
            name = "client",
            version = "1.0",
            description = "Start the clients to read news.",
            mixinStandardHelpOptions = true)
    public static class NewsClient implements Runnable{

        @CommandLine.Option(
                names = {"-c", "--client"},
                description = "Port to connect.")
        private int portClient = 5001;

        @Override
        public void run() {
            ch.heigvd.NewsClient.NewsClient n = new ch.heigvd.NewsClient.NewsClient();
            n.start(portClient);
        }
    }
}