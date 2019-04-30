import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

abstract class Black {

    /**
     * Creates a black and white image from a colored image
     * @param original original image (no colors removed)
     * @param threshold integer for the threshold limit used
     * @return a black and white image of the original
     */
    static BufferedImage makeBlack(BufferedImage original,int threshold, boolean[][] mask) {
        //Creates image from path

        Object dataElements = null;
        ColorModel colourModel = original.getColorModel();
        Raster raster = original.getRaster();

        //Makes each pixel's red and green component 0
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                //Get colours in 0-255 values
                if (mask[y][x]) {
                    dataElements = raster.getDataElements(x, y, dataElements);
                    int red = 0;
                    int green = 0;
                    int blue = colourModel.getBlue(dataElements);

                    //Turn to bits
                    Color colour = new Color(red, green, blue);
                    int rgb = colour.getRGB();

                    original.setRGB(x, y, rgb);
                }
            }
        }

        dataElements = null;
        colourModel = original.getColorModel();
        raster = original.getRaster();

        Algorithms.gapmask = new double[original.getWidth()][original.getHeight()];

        //Goes through each pixel and changes it to black or white based on threshold value
        for(int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                if(mask[x][y]) {
                    //Get colours in 0-255 values
                    dataElements = raster.getDataElements(x, y, dataElements);
                    int blue = colourModel.getBlue(dataElements);

                    Color black = new Color(0, 0, 0);
                    Color white = new Color(255, 255, 255);
                    //Compares pixel to threshold
                    if (blue >= threshold) {
                        original.setRGB(x, y, white.getRGB());
                        Algorithms.gapmask[x][y] = 1;
                    } else {
                        original.setRGB(x, y, black.getRGB());
                        Algorithms.gapmask[x][y] = 0;
                    }
                }
            }
        }
        //Returns black and white image
        return original;
    }

    /**
     * Calculates gap fraction of a black and white image
     * Uses the mask created in the Algorithms class to add up the rgb values (white is 1, black is 0) and divide by total pixels
     * @param black Image to have gap fraction calculated
     * @return a double that is the gap fraction
     */
    static double getGapFraction(BufferedImage black,boolean[][] mask){
        double rgbCount = 0.0;
        double totalCount = 0.0;

        for (int x = 0; x < black.getWidth(); x++) {
            for (int y = 0; y < black.getHeight(); y++) {
                if (mask[x][y]) {
                    rgbCount = rgbCount + Algorithms.gapmask[x][y];
                    totalCount++;
                }
            }
        }
        return rgbCount / totalCount;
    }
}