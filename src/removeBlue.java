import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class removeBlue {
    static BufferedImage inputBuff;
    static BufferedImage outputBuff;

    public static void main(String[] args) {
        initiateImage();
        removeBlueMethod();
        printImage();
    }

    //This method imports an image and catches if file path is corrupted
    private static void initiateImage() {
        try {
            inputBuff = ImageIO.read(new File("./resources/XXXXXXXXXXXXX")); //This needs to be changed to connect with the GUI, and multiple file-paths
        } catch (IOException e) { System.out.println("There was an error in removeBlue.initiateImage()"); }
    }

    //This method removes the blue from the image, and returns outputBuff, which is inputBuff but blueless.
    private static void removeBlueMethod(){
        Object dataElements = null;
        ColorModel colourModel = inputBuff.getColorModel();
        Raster raster = inputBuff.getRaster();

        for(int y = 0; y < inputBuff.getHeight(); y++) {
            for (int x = 0; x < inputBuff.getWidth(); x++) {
                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int red = colourModel.getRed(dataElements);
                int green = colourModel.getRed(dataElements);
                int blue = 0;

                //Turn to bits
                Color colour = new Color(red,green,blue);
                int rgb = colour.getRGB();

                outputBuff.setRGB(x, y, rgb);
            }
        }
    }

    //This method
    private static void printImage(){
        File outputFile = new File("outputImage.jpg");
        try {
            ImageIO.write(outputBuff, "jpg", outputFile);
        } catch (IOException e) {System.out.println("There was an error in removeBlue.printImage()");}
    }
}
