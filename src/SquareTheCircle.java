import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SquareTheCircle {
    private static String squareFilepath;

    static void createTheRectangle (String filepath) {
        BufferedImage original = null;
        try {
            original = ImageIO.read(new File(filepath));
        } catch (IOException exception) {
            System.out.println("You done fucked up");
        }
        BufferedImage rectangular = original;

        BufferedImage square = buildASquare(rectangular);
        saveTheSquare(square);
    }

    static BufferedImage buildASquare (BufferedImage rectangle) {
        BufferedImage square = rectangle;
        int x = Circle.circleX;
        int y = Circle.circleY;
        int radius = Circle.circleR;
        square.getSubimage(x, y, radius, radius);
        return square;
    }

    static void saveTheSquare (BufferedImage square) {

    }

    static void circleTheSquare (BufferedImage square) {

    }

    public static String getSquareFilepath() {
        return squareFilepath;
    }
}