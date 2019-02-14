import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Scanner;

public class BasicBlackAndWhite {

    //Initiation of universal variable
    private static BufferedImage colourLessInput = removeColour.colourLessOutput;
    static BufferedImage blackAndWhiteOutput;

    public static void BasicBlackAndWhiteMain() {
        blackAndWhiteMethod();
        printImage();
    }

    //This method removes the colour from blueLessInput and returns colourLessOutput
    private static void blackAndWhiteMethod(){
        Object dataElements = null;
        ColorModel colourModel = colourLessInput.getColorModel();
        Raster raster = colourLessInput.getRaster();
        blackAndWhiteOutput = colourLessInput;

        Scanner reader = new Scanner(System.in);
        System.out.print("What threshold would you like to use? (Values 0-255\n");
        int threshold = reader.nextInt();

        for(int y = 0; y < colourLessInput.getHeight(); y++) {
            for (int x = 0; x < colourLessInput.getWidth(); x++) {

                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int red = colourModel.getRed(dataElements);
                int green = colourModel.getRed(dataElements);
                int blue = colourModel.getBlue(dataElements);
                //A precaution if statement
                if((blue != red)|(blue != green)|(red != green)) {
                    System.out.print("RGB values are not even");
                    return;
                }

                Color black = new Color(0,0,0);
                Color white = new Color(255,255,255);
                if(red>=threshold) {
                    blackAndWhiteOutput.setRGB(x, y, white.getRGB());
                }
                else {
                    blackAndWhiteOutput.setRGB(x, y, black.getRGB());
                }
            }
        }
    }

    //This method
    private static void printImage(){
        File outputFile = new File("blackAndWhiteOutputImage.jpg");
        try {
            ImageIO.write(blackAndWhiteOutput, "jpg", outputFile);
        } catch (IOException e) {System.out.println("There was an error in BasicBlackAndWhite.printImage()");}
    }
}