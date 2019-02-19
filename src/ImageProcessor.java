import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {
    protected BufferedImage bwImage;
    protected BufferedImage original;

    public ImageProcessor(String path){
        try {original = ImageIO.read(new File(path)); }
        catch (IOException ignored){}
    }

    public BufferedImage getBwImage() {
        return bwImage;
    }
}
