import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePrepper extends Container {
    public ImagePrepper(String path){

        //File image = new File(path);

        setLayout(new BorderLayout());

        JPanel imagepanel = new JPanel();
        JButton next = new JButton("Next");
        JPanel controls = new JPanel();

        add(imagepanel, BorderLayout.WEST);
        add(next, BorderLayout.PAGE_END);
        add(controls, BorderLayout.EAST);


        try {
            BufferedImage image = ImageIO.read(new File(path));
            int height =image.getHeight();
            int width = image.getWidth();
            Image i = image.getScaledInstance((500*width)/height,500,Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(i));
            add(imageLabel);
        }
        catch(IOException ex){

        }




    }
}
