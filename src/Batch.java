import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;

class Batch {
    //What does this do?
    //read in info from properties, store it variables
    //path will be folder (for this)
    //create loop through each image and give each variable and do not ask for user
    //method 0 is Manuel, 1 is Nobis
    static void run (boolean[][] colormask, String csvPath, JProgressBar progressBar) throws IOException {
        Properties config = new Properties();
        InputStream input = new FileInputStream("config.properties");
        config.load(input);

        String threshold = config.getProperty("threshold");
        String path = config.getProperty("path");
        String[] paths = path.split(","); //This needs to be done to turn the string into an array to be able to run through all of the paths
        int method = Integer.parseInt(config.getProperty("method"));

        double north = Double.parseDouble(config.getProperty("north"));
        int yCenter = Integer.parseInt(config.getProperty("yCenter"));
        int radius = Integer.parseInt(config.getProperty("radius"));
        int xCenter = Integer.parseInt(config.getProperty("xCenter"));

        Prop.deleteProperties();
        input.close();

        //For loop to run through everything
        File temp = new File(paths[0]);
        if(temp.isDirectory()) {
            File[] files = temp.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
            if(files==null){return;}
            paths = new String[files.length];
            for (int a = 1; a < files.length; a++) {
                paths[a]=files[a].getAbsolutePath();
            }
        }
        double gapFraction = -1.0;
        for(int i=1; i<paths.length; i++){
            String[] methods= {"Manual","Nobis","Single Binary"};
            BufferedImage original = ImageIO.read(new File(paths[i]));
            BufferedImage square = SquareTheCircle.buildASquare(original);
            if(method == 0) {
                BufferedImage black = Black.makeBlack(square, Integer.parseInt(threshold), colormask);
                gapFraction = Black.getGapFraction(black, colormask);
            }
            else if(method == 1) {
                BufferedImage black = Algorithms.nobis(square, colormask);
                gapFraction = Black.getGapFraction(black, colormask);
            }
            else if(method == 2){
                BufferedImage black = Algorithms.single(square);
                gapFraction = Black.getGapFraction(black, colormask);
            }
            /*
            ADD IN HERE ANY OTHER THRESHOLDING METHODS
             */
            String[] data = new String[]{paths[i],methods[method],"N/A","",String.valueOf(gapFraction)};
            if(method==0){
                data[2]=String.valueOf(threshold);
            }
            //Write to the CSV
            CSV.writeTo(csvPath,data);
            int pct = (i*100/paths.length);
            progressBar.setValue(pct);
        }
        progressBar.setValue(100);
    }
}
