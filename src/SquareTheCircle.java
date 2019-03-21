import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
        boolean[][] colourMask = new boolean[xSquare][ySquare];
        //Quadrant 2

        //Quadrant 1

        //Quadrant 3

        //Quadrant 4

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