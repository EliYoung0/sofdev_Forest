import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class removeBlue {
    private static BufferedImage memeBuff;

    public static void main(String[] args) {
        initiateMeme();
        removeBlue();
        printImage();
    }

    //This method imports an image and catches if file path is corrupted
    private static void initiateMeme() {
        try {
            memeBuff = ImageIO.read(new File("./resources/Dhogwarts.jpg")); //This needs to be changed to connect with the GUI, and multiple file-paths
        } catch (IOException e) { System.out.println("You Done Fucked Up"); }
    }

    private static void removeBlue(){
        Object dataElements = null;
        ColorModel colourModel = memeBuff.getColorModel();
        Raster raster = memeBuff.getRaster();

        for(int y = 0; y < memeBuff.getHeight(); y++) {
            for (int x = 0; x < memeBuff.getWidth(); x++) {
                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int red = colourModel.getRed(dataElements);
                int green = colourModel.getRed(dataElements);
                int blue = 0;

                //Turn to bits
                Color colour = new Color(red,green,blue);
                int rgb = colour.getRGB();

                memeBuff.setRGB(x, y, rgb);

                //int alpha = colourModel.getAlpha(dataElements);
                //Testing print
                //System.out.println("RGB: " + rgb);
            }
        }
    }

    private static void printImage(){
        File memeFile = new File("DhogwartsBlueLess.jpg");
        try {
            ImageIO.write(memeBuff, "jpg", memeFile);
        } catch (IOException e) {System.out.println("You almost made it, then you fucked up.");}
    }
}
