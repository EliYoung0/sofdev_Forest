import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Circle extends Container {
    private String filepath;
    static int circleX;
    static int circleY;
    static int circleD;

    Circle(String path, UI ui){
        filepath = path;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        //Adding image to grid
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight=3;
        JLabel imageLabel;
        try {
            BufferedImage image = ImageIO.read(new File(filepath));
            int height = image.getHeight();
            int width = image.getWidth();
            Image i = image.getScaledInstance((500*width)/height, 500, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(i));
            add(imageLabel, c);
        } catch (IOException j) {
            imageLabel = new JLabel();
        }

        //Creating input panel for circle parameters
        c.gridy = 0;
        c.gridx = 1;
        c.gridheight = 2;
        JPanel circlePanel = new JPanel();
        circlePanel.setLayout(new BoxLayout(circlePanel, BoxLayout.Y_AXIS));
        add(circlePanel, c);
        //X-input
        JLabel xText = new JLabel("Centre x pixel value: ");
        JTextField xInputField = new JTextField(20);
        //Y-input
        JLabel yText = new JLabel("Centre y pixel value: ");
        JTextField yInputField = new JTextField(20);
        //Diameter input
        JLabel diameterText = new JLabel("Diameter pixel value: ");
        JTextField diameterInputField = new JTextField(20);
        //"Draw Circle" button
        JButton drawCircle = new JButton("Draw Circle");
        //Adding components to panel
        circlePanel.add(xText);
        circlePanel.add(xInputField);
        circlePanel.add(yText);
        circlePanel.add(yInputField);
        circlePanel.add(diameterText);
        circlePanel.add(diameterInputField);
        circlePanel.add(drawCircle);

        drawCircle.addActionListener(new CircleAction(filepath, xInputField, yInputField, diameterInputField, imageLabel));

        //TODO add in NORTH functionality
        //TODO save north values
        //Adds north functionality
        c.gridy = 2;
        c.gridx = 1;
        c.gridheight = 1;


        //Provide proceed button functionality
        JButton proceed = new JButton("Save & Continue");
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thresholder thresholder = new Thresholder(filepath,ui);
                ui.setContentPane(thresholder);
                ui.pack();
            }
        };
        proceed.addActionListener(listener);
        //Add proceed button to container
        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 2;
        add(proceed, c);

    }

    static void setCircleX (JTextField xInputField) {
        circleX = Integer.parseInt(xInputField.getText());
    }
    static void setCircleY (JTextField yInputField) {
        circleY = Integer.parseInt(yInputField.getText());
    }
    static void setCircleD (JTextField diameterInputField) {
        circleD = Integer.parseInt(diameterInputField.getText());
    }

    static BufferedImage readImage (String path) throws IOException {
        return ImageIO.read(new File(path));
    }
}

class CircleAction implements ActionListener {
    private JTextField x;
    private JTextField y;
    private JTextField diameter;
    private BufferedImage blackInput;
    private JLabel returnImage;
    private String path;

    CircleAction(String path, JTextField x, JTextField y, JTextField diameter, JLabel image){
        this.path = path;
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        returnImage = image;
    }

    public void actionPerformed(ActionEvent e) {
        //sets Circle universal variables
        Circle.setCircleX(x);
        Circle.setCircleY(y);
        Circle.setCircleD(diameter);

        //Calls colour image from file-path to be able to reset it every time
        try {
            blackInput = Circle.readImage(path);
        } catch (IOException g) {
            System.out.println("Why");
        }

        //Creates graphic and circle and draws ring onto image
        Graphics circle = blackInput.getGraphics();
        Graphics2D circleRing = (Graphics2D)circle;
        int x = Integer.parseInt(this.x.getText());
        int y = Integer.parseInt(this.y.getText());
        int diameter = Integer.parseInt(this.diameter.getText());
        Shape ring = createRingShape(x, y, (diameter+10));
        circleRing.setColor(Color.RED);
        circleRing.fill(ring);
        circleRing.setColor(Color.BLACK);
        circleRing.draw(ring);

        //clears memory of useless info
        circleRing.dispose();
        circle.dispose();

        //Repaints image
        int width = blackInput.getWidth();
        int height = blackInput.getHeight();
        Image i = blackInput.getScaledInstance((500*width)/height, 500, Image.SCALE_SMOOTH);
        returnImage.setIcon(new ImageIcon(i));
        returnImage.repaint();
    }

    //Draws a ring using two circles
    private static Shape createRingShape(double centerX, double centerY, double outerRadius){
        double thickness = 10.0;
        Ellipse2D outer = new Ellipse2D.Double(
                centerX - outerRadius,
                centerY - outerRadius,
                outerRadius + outerRadius,
                outerRadius + outerRadius);
        Ellipse2D inner = new Ellipse2D.Double(
                centerX - outerRadius + thickness,
                centerY - outerRadius + thickness,
                outerRadius + outerRadius - thickness - thickness,
                outerRadius + outerRadius - thickness - thickness);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        return area;
    }
}