package ch.heigvd.PicoCLI;

import ch.heigvd.MusicToImage.MusicToImage;
import ch.heigvd.TextToImage.TextToImage;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(
        name = "Kuby - Transform text or music into stunning colorful images!",
        version = "1.0",
        description = "Kuby is a CLI tool made during our bachloor degree in computer science that will be able to convert a text or a music to a pixel designed image.",
        subcommands = { PicoCLI.ConvertTextToImage.class, PicoCLI.ConvertMusicToImage.class},
        mixinStandardHelpOptions = true)

public class PicoCLI {
    @CommandLine.Command(
            name = "text",
            version = "1.0",
            description = "Conversion of a text file into an image.",
            mixinStandardHelpOptions = true)

    public static class ConvertTextToImage implements Runnable {
        @CommandLine.Option(
                names = {"-i", "--input"},
                required = true,
                description = "Path to the input file.")
        private String inputFile;

        @CommandLine.Option(
                names = {"-o", "--output"},
                required = true,
                description = "Path to the folder where the image will be generated.")
        private String outputFile;

        @CommandLine.Option(
                names = {"-c", "--color"},
                required = false,
                description = "Values = [blue, green, yellow, red, random] : Color of the generated image.")
        private String color = "";

        @CommandLine.Option(
                names = {"-h", "--height"},
                required = false,
                description = "Height of the generated image.")
        private int height = 100;

        @CommandLine.Option(
                names = {"-w", "--width"},
                required = false,
                description = "Width of the generated image.")
        private int width = 100;

        @Override
        public void run() {

            Timer timer = new Timer();

            if(inputFile != null && outputFile != null) {
                new TextToImage(inputFile, width, height, outputFile, color);
                System.out.println("Successful conversion !");
                timer.Stop();
            } else {
                System.out.println("Input or output file can't be null.");
                timer.Abort();
            }
        }
    }

    @CommandLine.Command(
            name = "music",
            version = "1.0",
            description = "Conversion of music into an image.",
            mixinStandardHelpOptions = true)
    public static class ConvertMusicToImage implements Runnable {
        @CommandLine.Option(
                names = {"-i", "--input"},
                required = true,
                description = "Path to the input file.")
        private String inputFile;

        @CommandLine.Option(
                names = {"-o", "--output"},
                required = true,
                description = "Path to the folder where the image will be generated.")
        private String outputFile;

        @CommandLine.Option(
                names = {"-c", "--color"},
                required = false,
                description = "Values = [blue, green, yellow, red, random] : Color of the generated image.")
        private String color = "";

        @CommandLine.Option(
                names = {"-h", "--hight"},
                required = false,
                description = "Height of the generated image.")
        private int height = 100;

        @CommandLine.Option(
                names = {"-w", "--width"},
                required = false,
                description = "Width of the generated image.")
        private int width = 100;

        @Override
        public void run() {

            Timer timer = new Timer();

            if(inputFile != null && outputFile != null) {
                new MusicToImage(inputFile, width, height, outputFile, color);
                System.out.println("Successful conversion !");
                timer.Stop();
            } else {
                System.out.println("Input or output file can't be null.");
                timer.Abort();
            }
        }
    }

    private static class Timer{
        long startTime, endTime;
        public Timer(){
            startTime = System.currentTimeMillis();
        }
        public void Abort() {
            endTime = System.currentTimeMillis();
        }
        public void Stop() {
            Abort();
            System.out.println("Execution time: " + (endTime - startTime) + " millisecondes");
        }
    }
}
