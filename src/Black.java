import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public interface Black {
    static BufferedImage makeBlack(String path,int threshold) throws IOException {
        try {
            BufferedImage original = ImageIO.read(new File(path));

            Object dataElements = null;
            ColorModel colourModel = original.getColorModel();
            Raster raster = original.getRaster();

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

            //needs to be reworked to take input from algorithms in order to determine threshold
            for(int y = 0; y < original.getHeight(); y++) {
                for (int x = 0; x < original.getWidth(); x++) {

                    //Get colours in 0-255 values
                    dataElements = raster.getDataElements(x, y, dataElements);
                    int blue = colourModel.getBlue(dataElements);

                    Color black = new Color(0,0,0);
                    Color white = new Color(255,255,255);
                    if(blue>=threshold) {
                        original.setRGB(x, y, white.getRGB());
                    }
                    else {
                        original.setRGB(x, y, black.getRGB());
                    }
                }
            }
            return original;
        } catch (IOException e) {
            throw e;

        }
    }

    static double getGapFraction(BufferedImage black){
        double whiteCount=0.0;
        double blackCount=0.0;
        Object dataElements = null;
        ColorModel colourModel = black.getColorModel();
        Raster raster = black.getRaster();

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
        return ((whiteCount)/(whiteCount+blackCount));
    }
}
