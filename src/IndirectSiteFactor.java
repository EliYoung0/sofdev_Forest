/*
This is not a true IndirectSiteFactor calculator.
True ISF would have the option for multiple locations in the world,
and would calculate the zenith based on the time of day, time of year, and N/S latitude.
(This would mean that the zenith would be more variable and move around depending on the input image
and where and when it was taken in the world. However, this would require a lot of calculations to discover,
and since this program is only for Dr. Metz's use, it will not be as relevant.)
*/

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

//TODO Call this in Thresholder
public class IndirectSiteFactor {
    /*
    This calculation is done by splitting the plane into nine portions, which,
    if the Sun was at noon (peak), would mean that there would be nine portions of 10 degrees
    each on every side to the horizon.
     */
    private static double[][] mask(double zenith, int height, int width){
        double[][] isfMask = new double[width][height];

        int w = Circle.circleR * 2;
        for(int x=0; x<w; x++){
            for(int y=0; y<w; y++){
                double xDist = (double)Circle.circleX - x;
                double yDist = (double)Circle.circleY - y;
                double distance = Math.sqrt(Math.pow(xDist,2) + Math.pow(yDist,2));
                double zDist = Math.sqrt(Math.pow(Circle.circleR, 2) - Math.pow(distance,2));
                double zenDistance = Circle.circleX + ((double)Circle.circleR * Math.cos((Math.PI/12 * zenith) + (Math.PI/2)));
                double zenX = zenDistance * Math.cos(Circle.circleN + (Math.PI / 2));
                double zenY = zenDistance * Math.sin(Circle.circleN + (Math.PI / 2));
                double zenZ = Math.sqrt(Math.pow(Circle.circleR,2) - Math.pow(zenDistance,2));
                isfMask[x][y] = (xDist * zenX) + (yDist * zenY) + (zDist * zenZ);
            }
        }
        return isfMask;
    }

    static double getISF(BufferedImage black) {
        double [][] mask = mask(Circle.circleZ, black.getHeight(), black.getWidth());
        double rgbCount=0.0;
        double totalCount=0.0;
        Object dataElements=null;
        ColorModel colorModel = black.getColorModel();
        Raster raster = black.getRaster();

        //Determines if each pixel is black or white
        for(int y = 0; y < black.getHeight(); y++) {
            for (int x = 0; x < black.getWidth(); x++) {
                dataElements = raster.getDataElements(x, y, dataElements);
                int blue = colorModel.getBlue(dataElements);
                blue = blue/255;
                rgbCount = rgbCount + (blue * mask[x][y]);
                totalCount++;
            }
        }
        //System.out.println("rgbCount: " + rgbCount + " Total: " + totalCount);
        return rgbCount/totalCount;
    }
}