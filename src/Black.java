import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

abstract class Black {

    /**
     * Creates a black and white image from a colored image
     * @param path Path to the original image (no colors removed)
     * @param threshold integer for the threshold limit used
     * @return a black and white image of the original
     * @throws IOException exception thrown if not a valid image filepath
     */
    static BufferedImage makeBlack(String path,int threshold) throws IOException {
        //Creates image from path
        BufferedImage original = ImageIO.read(new File(path));

        Object dataElements = null;
        ColorModel colourModel = original.getColorModel();
        Raster raster = original.getRaster();

        //Makes each pixel's red and green component 0
        for(int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int red = 0;
                int green = 0;
                int blue = colourModel.getBlue(dataElements);

                //Turn to bits
                Color colour = new Color(red,green,blue);
                int rgb = colour.getRGB();

                original.setRGB(x, y, rgb);
            }
        }

        dataElements = null;
        colourModel = original.getColorModel();
        raster = original.getRaster();

        //TODO:needs to be reworked to take input from algorithms in order to determine threshold
        //Goes through each pixel and changes it to black or white based on threshold value
        for(int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {

                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int blue = colourModel.getBlue(dataElements);

                Color black = new Color(0,0,0);
                Color white = new Color(255,255,255);
                //Compares pixel to threshold
                if(blue>=threshold) {
                    original.setRGB(x, y, white.getRGB());
                }
                else {
                    original.setRGB(x, y, black.getRGB());
                }
            }
        }
        //Returns black and white image
        return original;
    }

    /**
     * Calculates gap fraction of a black and white image
     * Counts white tiles and assumes rest are black
     * @param black Image to have gap fraction calculated
     * @return a double that is the gap fraction
     */
    static double getGapFraction(BufferedImage black){

        double whiteCount=0.0;
        double blackCount=0.0;
        Object dataElements = null;
        ColorModel colourModel = black.getColorModel();
        Raster raster = black.getRaster();

        //Determines if each pixel is black or white
        for(int y = 0; y < black.getHeight(); y++) {
            for (int x = 0; x < black.getWidth(); x++) {
                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int blue = colourModel.getBlue(dataElements);
                if(blue==255){
                    whiteCount++;
                }
                else{
                    blackCount++;
                }
            }
        }
        //Calculates and returns gap fraction
        return ((whiteCount)/(whiteCount+blackCount));
    }
}