package ch.heigvd.ImageGenerator;

// Java program to demonstrate
// creation of random pixel image

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;

/**
 * Tools to generate an image based on a byte array.
 */
public class ImageGenerator
{
    private final byte[] data;
    private final int imgWidth, imgHeight;
    private final String outputPath;
    private final ColorProfiles colorPaletteChoosen;

    // Magic number is used to make colors lighter
    private static final int MAGIC_NUMBER = 70;


    /**
     * Enum to manage different profile colors.
     */
    private enum ColorProfiles {
        // Also add colors here
        YELLOW(150,150,1),
        BLUE(1,1,200),
        RED(200,1,1),
        GREEN(1,200,1),
        RANDOM(1,1,1);

        private final int r;
        private final int g;
        private final int b;

        ColorProfiles(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public int getRed() {
            return r;
        }

        public int getGreen() {
            return g;
        }

        public int getBlue() {
            return b;
        }

        public static ColorProfiles getByName(String name) {
            return switch (name) {
                case "yellow" -> YELLOW;
                case "red" -> RED;
                case "blue" -> BLUE;
                case "green" -> GREEN;
                default -> RANDOM;
            };
        }
    }

    /**
     * Constructor of our ImageGenerator class
     * @param outputPath = path where file will be generated
     * @param width = width of the image that will be created
     * @param height = width of the image that will be created
     * @param data = table of bytes to be analysed to create the image
     * @param colorProfileUsed = preferred color theme to apply to the image
     */
    public ImageGenerator(String outputPath, int width, int height, byte[] data, String colorProfileUsed){

        this.imgWidth = width;
        this.imgHeight = height;
        this.data = data;
        this.outputPath = outputPath;

        colorPaletteChoosen = ColorProfiles.getByName(colorProfileUsed);

    }

    /**
     * Generate an image
     */
    public void GenerateImage() {

        // Create buffered image object
        BufferedImage img = null;
        img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        // file object
        File f = null;

        int count = 0;
        int min = 1, max = 255;

        // create random values pixel by pixel
        for (int y = 0; y < imgHeight; y++)
        {
            for (int x = 0; x < imgWidth; x++)
            {
                int r,g,b = 0;

                if(colorPaletteChoosen != ColorProfiles.RANDOM) {
                    r = (data[count] % colorPaletteChoosen.getRed());
                    g = (data[count] % colorPaletteChoosen.getGreen());
                    b = (data[count] % colorPaletteChoosen.getBlue());
                } else {
                    r = (data[count] + MAGIC_NUMBER % ThreadLocalRandom.current().nextInt(min,max));
                    g = (data[count] + MAGIC_NUMBER % ThreadLocalRandom.current().nextInt(min,max));
                    b = (data[count] + MAGIC_NUMBER % ThreadLocalRandom.current().nextInt(min,max));
                }

                //pixel
                int p = (r<<16) | (g<<8) | b;

                img.setRGB(x, y, p);

                count++;

                if(count >= data.length) {
                    count = 0;
                }
            }
        }

        // Write image
        try
        {
            // Image name will be kuby "width of image" x "height of image".png
            f = new File(outputPath + "/kuby" + imgWidth + "x" + imgHeight + ".png");
            ImageIO.write(img, "png", f);
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }

    }
}
