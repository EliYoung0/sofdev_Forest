import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ImagePrepper extends Container {

    /**
     * Constructor for Image Prepping Container
     * @param path filepath for image selected
     * @throws IOException exception thrown if file is not valid
     */
    ImagePrepper(String path) throws IOException {

        setLayout(new BorderLayout());

        //Creates and adds components
        JButton next = new JButton("Next");
        JPanel controls = makeControls();
        JPanel imagePanel = makeImage(path);

        add(next, BorderLayout.PAGE_END);
        add(controls, BorderLayout.EAST);

        add(imagePanel);



    }

    private JPanel makeImage(String path) throws IOException {
        JPanel panel=new JPanel();
        //Creates and displays the image selected
        BufferedImage image = ImageIO.read(new File(path));
        int height =image.getHeight();
        int width = image.getWidth();
        Image i = image.getScaledInstance((500*width)/height,500,Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(i));
        panel.add(imageLabel);
        return panel;
    }

    private JPanel makeControls() {
        JPanel outer = new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));
        JTextField xCenter = new JTextField();
        JLabel xTag = new JLabel("Center pixel x value: ");
        panel.add(xTag);
        panel.add(xCenter);
        JTextField yCenter = new JTextField();
        JLabel yTag = new JLabel("Center pixel x value: ");
        panel.add(yTag);
        panel.add(yCenter);
        JTextField north = new JTextField();
        JLabel northTag = new JLabel("North direction(Degrees CW from top): ");
        panel.add(northTag);
        panel.add(north);
        JButton update = new JButton("Update");
        outer.add(panel);
        outer.add(update);
        return outer;
    }
}
