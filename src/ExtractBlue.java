import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

@Deprecated
public class ExtractBlue {
    //Note that extraction means the isolation of the blue channel, not the removal of blue

    //Initiation of variables for before and after, and for transfer to the removeColour class
    private static BufferedImage withColourInput;
    static BufferedImage blueExtractOutput;

    public static void extractBlueMain(String filepath){
        initiateImage(filepath);
        extractBlueMethod();
        printImage();
    }

    //This method imports an image and assigns it to withColourInput and catches if file path is corrupted
    private static void initiateImage(String filepath) {
        try {
            withColourInput = ImageIO.read(new File(filepath)); //This needs to be changed to connect with the GUI, and multiple file-paths
        } catch (IOException e) { System.out.println("There was an error in removeBlue.initiateImage()"); }
    }

    //This method extracts the blue from the image, withColourInput, and returns blueExtractOutput.
    private static void extractBlueMethod(){
        Object dataElements = null;
        ColorModel colourModel = withColourInput.getColorModel();
        Raster raster = withColourInput.getRaster();
        blueExtractOutput = withColourInput;

        for(int y = 0; y < withColourInput.getHeight(); y++) {
            for (int x = 0; x < withColourInput.getWidth(); x++) {
                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int red = 0;
                int green = 0;
                int blue = colourModel.getBlue(dataElements);

                //Turn to bits
                Color colour = new Color(red,green,blue);
                int rgb = colour.getRGB();

                blueExtractOutput.setRGB(x, y, rgb);
            }
        }
    }

    //This method prints out blueExtractOutput to a file after removal of blue hues
    private static void printImage(){
        File outputFile = new File("bblueExtractOutput.jpg");
        try {
            ImageIO.write(blueExtractOutput, "jpg", outputFile);
        } catch (IOException e) {System.out.println("There was an error in ExtractBlue.printImage()");}
    }
}
