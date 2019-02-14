import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class removeColour {
    //Initiation of universal variable
    private static BufferedImage blueLessInput = removeBlue.blueLessOutput;
    static BufferedImage colourLessOutput;

    public static void removeColourMain() {
        removeColourMethod();
        printImage();
    }

    //This method removes the colour from blueLessInput and returns colourLessOutput
    private static void removeColourMethod(){
        Object dataElements = null;
        ColorModel colourModel = blueLessInput.getColorModel();
        Raster raster = blueLessInput.getRaster();
        colourLessOutput = blueLessInput;

        for(int y = 0; y < blueLessInput.getHeight(); y++) {
            for (int x = 0; x < blueLessInput.getWidth(); x++) {
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

    //This method
    private static void printImage(){
        File outputFile = new File("colourlessOutputImage.jpg");
        try {
            ImageIO.write(colourLessOutput, "jpg", outputFile);
        } catch (IOException e) {System.out.println("There was an error in removeColour.printImage()");}
    }
}
