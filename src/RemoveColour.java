import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class RemoveColour {
    //Initiation of universal variable
    private static BufferedImage blueExtractInput = ExtractBlue.blueExtractOutput;
    static BufferedImage colourLessOutput;

    public static void removeColourMain() {
        removeColourMethod();
        printImage();
    }

    //This method removes the colour from blueExtractInput and returns colourLessOutput
    private static void removeColourMethod(){
        Object dataElements = null;
        ColorModel colourModel = blueExtractInput.getColorModel();
        Raster raster = blueExtractInput.getRaster();
        colourLessOutput = blueExtractInput;

        for(int y = 0; y < blueExtractInput.getHeight(); y++) {
            for (int x = 0; x < blueExtractInput.getWidth(); x++) {
                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int red = colourModel.getRed(dataElements);
                int green = colourModel.getRed(dataElements);
                int blue = colourModel.getBlue(dataElements);
                //A precaution if statement
                if(blue != 0) {
                    System.out.print("Blue is not equal to 0.");
                    return;
                }

                int greyscale = (red + green + blue) / 3;
                //System.out.println(greyscale);

                Color colour = new Color(greyscale,greyscale,greyscale);
                int rgb = colour.getRGB();
                colourLessOutput.setRGB(x, y, rgb);
            }
        }
    }

    //This method prints out colourLessOutput to a file after removal of blue hues
    private static void printImage(){
        File outputFile = new File("colourlessOutputImage.jpg");
        try {
            ImageIO.write(colourLessOutput, "jpg", outputFile);
        } catch (IOException e) {System.out.println("There was an error in removeColour.printImage()");}
    }
}
