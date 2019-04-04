import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;

public class Batch {
    //What does this do?
    void run (boolean[][] colourmask) throws IOException {
        Properties config = new Properties();
        InputStream input = null;
        //read in info from properties, store it variables
        //path will be folder (for this)
        //create loop through each image and give each variable and do not ask for user
        //method 0 is Manuel, 1 is Nobis

        input = new FileInputStream("config.properties");
        config.load(input);
        int threshold = Integer.parseInt(config.getProperty("threshold"));
        int radius = Integer.parseInt(config.getProperty("radius"));
        int xCenter = Integer.parseInt(config.getProperty("xCenter"));
        String path = config.getProperty("path");
        //This needs to be done to turn the string into an array to be able to run through all of the paths
        String[] paths = path.split(",");
        int north = Integer.parseInt(config.getProperty("north"));
        int yCenter = Integer.parseInt(config.getProperty("yCenter"));
        int method = Integer.parseInt(config.getProperty("method"));
        Prop.deleteProperties();
        input.close();

        //For loop to run through everything
        File temp = new File(paths[0]);
        if(temp.isDirectory()) {
            paths = temp.list((dir, name) -> name.toLowerCase().endsWith(".jpg"));
        }
        double gapFraction;
        for(int i=0; i<paths.length; i++){
            BufferedImage original = ImageIO.read(new File(paths[i]));
            BufferedImage square = SquareTheCircle.buildASquare(original);
            if(method == 0) {
                BufferedImage black = Black.makeBlack(square, threshold, colourmask);
                gapFraction = Black.getGapFraction(black, colourmask);
            }
            else if(method == 1) {

            }
        }
    }
}
