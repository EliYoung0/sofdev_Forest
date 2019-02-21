import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class Processing {
    public BufferedImage original;
    public BufferedImage greyImage;
    public BufferedImage blackAndWhiteOutput;
    private static double whitePixels;
    private static double blackPixels;

    public Processing(String filepath){
        blackPixels=0.0;
        whitePixels=0.0;
        try {
            //This needs to be changed to connect with the GUI, and multiple file-paths
            original = ImageIO.read(new File(filepath));
            BufferedImage temp = makeBlue();
            greyImage = makeGrey(temp);
        } catch (IOException e) { System.out.println("There was an error in removeBlue.initiateImage()"); }

    }
    public void imageProc(int threshold){
        blackAndWhiteOutput=makeBlack(greyImage,threshold);
    }

    public static double getGapFraction() {
        return (whitePixels/blackPixels);
    }

    private BufferedImage makeBlack(BufferedImage colourLessInput, int threshold) {
        Object dataElements = null;
        ColorModel colourModel = colourLessInput.getColorModel();
        Raster raster = colourLessInput.getRaster();
        BufferedImage returnImage = colourLessInput;

        //needs to be reworked to take input from algorithms in order to determine threshold
        for(int y = 0; y < colourLessInput.getHeight(); y++) {
            for (int x = 0; x < colourLessInput.getWidth(); x++) {

                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int red = colourModel.getRed(dataElements);
                int green = colourModel.getGreen(dataElements);
                int blue = colourModel.getBlue(dataElements);
                //A precaution if statement
                if((blue != red)|(blue != green)|(red != green)) {
                    System.out.print("RGB values are not even");
                    return null;
                }

                Color black = new Color(0,0,0);
                Color white = new Color(255,255,255);
                if(blue>=threshold) {
                    returnImage.setRGB(x, y, white.getRGB());
                    whitePixels++;
                }
                else {
                    returnImage.setRGB(x, y, black.getRGB());
                    blackPixels++;
                }
            }
        }
        return returnImage;
    }

    private BufferedImage makeGrey(BufferedImage blueLessInput) {
        Object dataElements = null;
        ColorModel colourModel = blueLessInput.getColorModel();
        Raster raster = blueLessInput.getRaster();
        BufferedImage returnImage=blueLessInput;

        for(int y = 0; y < blueLessInput.getHeight(); y++) {
            for (int x = 0; x < blueLessInput.getWidth(); x++) {
                //Get colours in 0-255 values
                dataElements = raster.getDataElements(x, y, dataElements);
                int red = colourModel.getRed(dataElements);
                int green = colourModel.getGreen(dataElements);
                int blue = colourModel.getBlue(dataElements);
                //A precaution if statement
                int greyscale = (red + green + blue) / 3;
                //System.out.println(greyscale);

                Color colour = new Color(greyscale,greyscale,greyscale);
                int rgb = colour.getRGB();
                returnImage.setRGB(x, y, rgb);
            }
        }
        return returnImage;
    }

    private BufferedImage makeBlue(){
        Object dataElements = null;
        ColorModel colourModel = original.getColorModel();
        Raster raster = original.getRaster();
        BufferedImage bImage = original;

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

                bImage.setRGB(x, y, rgb);
            }
        }
        return bImage;
    }

    public void printFinal(){
        File outputFile = new File("blackAndWhiteOutputImage.jpg");
        try {
            ImageIO.write(blackAndWhiteOutput, "jpg", outputFile);
        } catch (IOException e) {System.out.println("There was an error in BasicBlackAndWhite.printImage()");}
    }
}
