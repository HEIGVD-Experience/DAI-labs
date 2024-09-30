package ch.heigvd.MusicToImage;

import ch.heigvd.ImageGenerator.ImageGenerator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class MusicToImage {
    /**
     * Constructor of the class that will convert the text to an image
     * @param inputPath = the path where the prompt will come
     * @param width = the width of the image
     * @param height = the height of the image
     * @param outputPath = the path where to write the image
     * @param colorProfile = the color profile
     */
    public MusicToImage(String inputPath, int width, int height, String outputPath, String colorProfile) {
        File outputFile = new File(outputPath);
        File inputFile = new File(inputPath);

        if (inputFile.exists()) {
            String fileName = inputFile.getName();

            if (!fileName.endsWith(".mp3")) {
                System.out.println("Error: Incorrect extension.");
                System.exit(1);
            }
        } else {
            System.out.println("Error: Can't find the input file.");
            System.exit(1);
        }

        if(!outputFile.exists()){
            System.out.println("Error: Can't find the output folder.");
            System.exit(1);
        }

        if(width <= 0 || height <= 0){
            System.out.println("Error: Height and width as to be more than 0.");
            System.exit(1);
        }

        if(!(Objects.equals(colorProfile, "yellow") || Objects.equals(colorProfile, "red") || Objects.equals(colorProfile, "blue") || Objects.equals(colorProfile, "green"))){
            System.out.println("Warning: Color unknown -> random.");
        }

        readBytes(inputPath);
        ImageGenerator imageGenerator = new ImageGenerator(outputPath, width, height, readBytes(inputPath), colorProfile);
        imageGenerator.GenerateImage();
    }

    // Read bytes from the music given in the inputPath
    private byte[] readBytes(String inputPath) {
        byte[] data = null;

        try {
            FileInputStream fileMP3 = new FileInputStream(inputPath);

            // Define the size of the data array to be returned
            int fileSize = (int) new File(inputPath).length();
            data = new byte[fileSize];

            int bytesRead = fileMP3.read(data);

            if (bytesRead != fileSize) {
                throw new RuntimeException("Error: The lecture of your file didn't work. Please retry.");
            }

            fileMP3.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }

        return data;
    }
}
