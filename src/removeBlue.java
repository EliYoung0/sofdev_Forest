import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class removeBlue {
    //Initiation of variables for before and after, and for transfer to the removeColour class
    private static BufferedImage withBlueInput;
    static BufferedImage blueLessOutput;

    public static void removeBlueMain(String filepath){
        initiateImage(filepath);
        removeBlueMethod();
        printImage();
    }

    //This method imports an image and assigns it to withBlueInput and catches if file path is corrupted
    private static void initiateImage(String filepath) {
        try {
            withBlueInput = ImageIO.read(new File(filepath)); //This needs to be changed to connect with the GUI, and multiple file-paths
        } catch (IOException e) { System.out.println("There was an error in removeBlue.initiateImage()"); }
    }

    //This method removes the blue from the image, withBlueInput, and returns blueLessOutput.
    private static void removeBlueMethod(){
        Object dataElements = null;
        ColorModel colourModel = withBlueInput.getColorModel();
        Raster raster = withBlueInput.getRaster();
        blueLessOutput = withBlueInput;

        for(int y = 0; y < withBlueInput.getHeight(); y++) {
            for (int x = 0; x < withBlueInput.getWidth(); x++) {
                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int red = colourModel.getRed(dataElements);
                int green = colourModel.getRed(dataElements);
                int blue = 0;

                //Turn to bits
                Color colour = new Color(red,green,blue);
                int rgb = colour.getRGB();

                blueLessOutput.setRGB(x, y, rgb);
            }
        }
    }

    //This method prints out blueLessOutput to a file after removal of blue hues
    private static void printImage(){
        File outputFile = new File("blueLessOutput.jpg");
        try {
            ImageIO.write(blueLessOutput, "jpg", outputFile);
        } catch (IOException e) {System.out.println("There was an error in removeBlue.printImage()");}
    }
}
