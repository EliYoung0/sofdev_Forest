import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class SquareTheCircle {
    private static String squareFilepath; //Filepath of cropped square version of original image
    private static boolean[][] imageMask = null; //2d boolean array of pixels to be used. True if used.
    private static String originalPath;

    /**
     * Creates and saves a cropped square version of image provided.
     * Then creates a circular image mask based on this cropped image
     * @param filepath filepath of image to be cropped
     */
    static void createTheRectangle(String filepath) {
        try {
            originalPath=filepath;
            //Opens original image
            BufferedImage original = ImageIO.read(new File(filepath));
            //Crops image to square
            BufferedImage square = buildASquare(original);
            //Saves this square image to filepath similar to the one given
            saveTheSquare(filepath, square);
            //Creates an image mask from the square image
            makeImageMask(square);
        } catch (IOException exception) { exception.printStackTrace(); }
    }

    /**
     * Uses center x & y and radius input in circle to create a cropped version of the original image
     * @param rectangle original image to be cropped
     * @return square cropped image of original input image
     */
    private static BufferedImage buildASquare(BufferedImage rectangle) {
        int x = Circle.circleX;
        int y = Circle.circleY;
        int radius = Circle.circleR;
        x = x - radius;
        y = y - radius;
        int diameter = radius * 2;
        return rectangle.getSubimage(x, y, diameter, diameter);
    }

    /**
     * Saves the square image input to a file
     * @param filepath filepath of original image
     * @param square square image to be saved
     */
    private static void saveTheSquare(String filepath, BufferedImage square) {
        File dir = new File("./square/");
        dir.mkdir();
        //Creates file path "filepath_square_time.jpg"
        String newFilepath = filepath.replaceAll("(.[a-zA-Z]{3,4}$)",
                "_square_" + java.time.LocalDate.now())+".jpg";
        //squareFilepath = originalPath.substring(0,originalPath.lastIndexOf(File.pathSeparator))+"/square/"+newFilepath;
        squareFilepath=newFilepath;
        //Saves square to created filepath
        File outputFile = new File(newFilepath);
        try { ImageIO.write(square, "jpg", outputFile);}
        catch (IOException e) { e.printStackTrace();}
    }

    /**
     * Creates image mask based on square image
     * @param square image mask is created based on
     */
    private static void makeImageMask(BufferedImage square){
        int w = square.getWidth();
        int radius=Circle.circleR;
        imageMask=new boolean[w][w];
        int center = w/2;
        double xdist,ydist,dist;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                xdist=Math.abs(center-i);
                ydist=Math.abs(center-j);
                dist=Math.sqrt(Math.pow(xdist,2)+Math.pow(ydist,2));
                imageMask[i][j]= !(dist > radius);
            }
        }
    }

    /**
     * returns the store imageMask
     * @return 2d boolean array of image mask
     */
    static boolean[][] getImageMask() { return imageMask; }

    /**
     * Gets the filepath of created square image
     * @return String of filepath of square image
     */
    static String getSquareFilepath() { return squareFilepath;}

    /**
     * Deletes the file stored at the SquareFilepath if it exists
     */
    static void deleteSquare() {
        if(SquareTheCircle.getSquareFilepath()!=null) {
            File f = new File(squareFilepath);
            if (f.exists()) {
                boolean success = f.delete();
                if (!success) { System.out.println("Failed to delete square.");}
            }
        }
    }

    /**
     * Checks if two image mask have same values
     * @param a first image mask
     * @param b second image mask
     * @param w width of array
     * @param h height of array
     * @return true if they are same, false otherwise
     */
    private static boolean sameMask(boolean[][] a, boolean[][] b, int w, int h){
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if(a[i][j]!=b[i][j]){
                    System.out.println("X: "+i+", Y: "+j+" different");
                    System.out.println("A: "+a[i][j]);
                    System.out.println("B: "+b[i][j]);
                    return false;
                }
            }
        }
        return true;
    }
}