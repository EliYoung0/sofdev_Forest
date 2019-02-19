import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Thresholder extends Container {
    public Thresholder (String path) {
        setLayout(new BorderLayout());

        JPanel algPanel = new JPanel();
        JTextField threshold = new JTextField(10);
        JButton update = new JButton("Update");

        JButton proceed = new JButton("Proceed");

        algPanel.add(threshold);
        algPanel.add(update);
        add(algPanel,BorderLayout.LINE_END);
        add(proceed,BorderLayout.PAGE_END);

        try {
            BufferedImage image = ImageIO.read(new File(path));
            int height = image.getHeight();
            int width = image.getWidth();
            Image i = image.getScaledInstance((500 * width) / height, 500, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(i));
            add(imageLabel,BorderLayout.LINE_START);
        } catch (IOException ex) {}
    }
}
