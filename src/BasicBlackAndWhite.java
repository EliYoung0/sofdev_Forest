import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Scanner;

public class BasicBlackAndWhite {

    //Initiation of universal variable
    private static BufferedImage colourLessInput = removeColour.colourLessOutput;
    static BufferedImage blackAndWhiteOutput;
    private static double whitePixels=0;
    private static double blackPixels=0;
    private static double gapFraction=0.0;

    public static void BasicBlackAndWhiteMain(int threshold) {
        blackAndWhiteMethod(threshold);
        printImage();
    }

    //This method removes the colour from blueLessInput and returns colourLessOutput
    private static void blackAndWhiteMethod(int threshold){
        Object dataElements = null;
        ColorModel colourModel = colourLessInput.getColorModel();
        Raster raster = colourLessInput.getRaster();
        blackAndWhiteOutput = colourLessInput;

        //needs to be reworked to take input from algorithms in order to determine threshold

        for(int y = 0; y < colourLessInput.getHeight(); y++) {
            for (int x = 0; x < colourLessInput.getWidth(); x++) {

                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int red = colourModel.getRed(dataElements);
                int green = colourModel.getGreen(dataElements);
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
                    whitePixels++;
                }
                else {
                    blackAndWhiteOutput.setRGB(x, y, black.getRGB());
                    blackPixels++;
                }
            }
        }

        gapFraction=(whitePixels/(whitePixels+blackPixels));
        System.out.println("Gap fraction = " + gapFraction);

    }

    //This method
    private static void printImage(){
        File outputFile = new File("blackAndWhiteOutputImage.jpg");
        try {
            ImageIO.write(blackAndWhiteOutput, "jpg", outputFile);
        } catch (IOException e) {System.out.println("There was an error in BasicBlackAndWhite.printImage()");}
    }


}