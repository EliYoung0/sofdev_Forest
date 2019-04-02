import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SquareTheCircle {
    private static String squareFilepath;
    private static boolean[][] colourMask = null;

    static void createTheRectangle(String filepath) {
        BufferedImage original = null;
        try {
            original = ImageIO.read(new File(filepath));
        } catch (IOException exception) {
            System.out.println("You done fucked up. There is no god.");
        }
        BufferedImage rectangular = original;

        BufferedImage square = buildASquare(rectangular);
        saveTheSquare(filepath, square);
        circleTheSquare(square);
    }

    static BufferedImage buildASquare(BufferedImage rectangle) {
        int x = Circle.circleX;
        int y = Circle.circleY;
        int radius = Circle.circleR;
        x = x - radius;
        y = y - radius;
        int diameter = radius * 2;
        BufferedImage square = rectangle.getSubimage(x, y, diameter, diameter);
        return square;
    }

    static void saveTheSquare(String filepath, BufferedImage square) {
        String newFilepath;
        newFilepath = filepath.replaceAll("(.[a-zA-Z]{3,4}$)",
                "_square_" + java.time.LocalDate.now());
        squareFilepath = newFilepath;
        File outputFile = new File(newFilepath);
        try {
            ImageIO.write(square, "jpg", outputFile);
        } catch (IOException ignored) {
            System.out.println("Love you! But this failed.");
        }
    }

    static void circleTheSquare(BufferedImage square) {
        int xSquare = square.getWidth(); //1960 (Usually)
        int ySquare = square.getHeight(); //1960 (Usually)
        colourMask = new boolean[xSquare][ySquare];
        int radius = Circle.circleR;
        int centre = xSquare/2;
        quadrantOne(xSquare, ySquare, radius, centre);
        //quadrantTwo(xSquare, ySquare, radius, centre);
        //quadrantThree(xSquare, ySquare, radius, centre);
        //quadrantFour(xSquare, ySquare, radius, centre);
        for(int x=0;x<centre;x++){ //change this to <xSquare
            System.out.println(" "); //change this to <ySquare
            for(int y=0;y<centre;y++){
                if(colourMask[x][y]) {
                    System.out.print("X");
                } else {
                    System.out.print("_");
                }
            }
        }

    }
    public static void quadrantOne(int xSquare, int ySquare, int radius, int centre){
        for(int x=centre;x<xSquare;x++){
            for(int y=0;y<centre;y++) {
                double xDist = x - centre;
                double yDist = centre - y;
                //TOA, O is yDist, A is xDist
                double angle = Math.atan(yDist/xDist);
                double cosine = Math.cos(angle)*radius;
                double sine = radius*Math.sin(angle);
                //System.out.println("y="+y+" x="+x+" xD="+xDist+" yD="+yDist+" a="+angle+" cos="+cosine+" sin"+sine);
                if(sine >= yDist && cosine >= xDist){
                    colourMask[x][y] = true;
                }
            }
        }
    }
    public static void quadrantTwo(int xSquare, int ySquare, int radius, int centre){
        for(int x=0;x<centre;x++){
            for(int y=0;y<centre;y++) {
                double xDist = centre - x;
                double yDist = centre - y;
                //TOA, O is yDist, A is xDist
                double angle = Math.atan(yDist/xDist);
                double cosine = Math.cos(angle)*radius;
                double sine = radius*Math.sin(angle);
                if(sine >= yDist & cosine >= xDist){
                    colourMask[x][y] = true;
                }
            }
        }
    }
    public static void quadrantThree(int xSquare, int ySquare, int radius, int centre){
        for(int x=0;x<centre;x++){
            for(int y=centre;y<ySquare;y++) {
                double xDist = centre - x;
                double yDist = y - centre;
                //TOA, O is yDist, A is xDist
                double angle = Math.atan(yDist/xDist);
                double cosine = Math.cos(angle)*radius;
                double sine = radius*Math.sin(angle);
                if(sine >= yDist & cosine >= xDist){
                    colourMask[x][y] = true;
                }
            }
        }
    }
    public static void quadrantFour(int xSquare, int ySquare, int radius, int centre){
        for(int x=centre;x<xSquare;x++){
            for(int y=centre;y<ySquare;y++) {
                double xDist = x - centre;
                double yDist = y - centre;
                //TOA, O is yDist, A is xDist
                double angle = Math.atan(yDist/xDist);
                double cosine = Math.cos(angle)*radius;
                double sine = radius*Math.sin(angle);
                if(sine >= yDist & cosine >= xDist){
                    colourMask[x][y] = true;
                }
            }
        }
    }

    public static void setColourMask(boolean[][] colourMask) {
        SquareTheCircle.colourMask = colourMask;
    }
    public static boolean[][] getColourMask() {
        return colourMask;
    }
    public static String getSquareFilepath() {
        return squareFilepath;
    }
}