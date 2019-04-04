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
    static int circleR;
    static double circleN;
    private static BufferedImage circledCanopy;

    /**
     * Constructor of Circle Container
     * Used to define image center location, radius, and north direction
     * @param path filepath to image used
     * @param ui outer window
     */
    Circle(String[] output, UI ui){
        circledCanopy=null;
        filepath = output[0];
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        //Adding image to grid
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight=3;
        JLabel canopyLabel;
        try {
            BufferedImage image = ImageIO.read(new File(filepath));
            int height = image.getHeight();
            int width = image.getWidth();
            Image i = image.getScaledInstance((500*width)/height, 500, Image.SCALE_SMOOTH);
            canopyLabel = new JLabel(new ImageIcon(i));
            add(canopyLabel, c);
        } catch (IOException j) {
            canopyLabel = new JLabel();
        }

        //Creating input panel for circle parameters
        c.gridy = 0;
        c.gridx = 1;
        c.gridheight = 1;
        JPanel circlePanel = new JPanel();
        circlePanel.setLayout(new BoxLayout(circlePanel, BoxLayout.Y_AXIS));
        add(circlePanel, c);
        //Directions
        JLabel directionText = new JLabel("<html> Input values for the circle below. <br> (Circle must be drawn before placing north) </html>");
        //X-input
        JLabel xText = new JLabel("Centre x pixel value: ");
        JTextField xInputField = new JTextField(20);
        //Y-input
        JLabel yText = new JLabel("Centre y pixel value: ");
        JTextField yInputField = new JTextField(20);
        //Radius input
        JLabel radiusText = new JLabel("Radius pixel value: ");
        JTextField radiusInputField = new JTextField(20);
        //Adding components to panel
        circlePanel.add(directionText);
        circlePanel.add(xText);
        circlePanel.add(xInputField);
        circlePanel.add(yText);
        circlePanel.add(yInputField);
        circlePanel.add(radiusText);
        circlePanel.add(radiusInputField);
        //Adds Draw Circle functionality
        JButton drawCircle = new JButton("Draw Circle");
        circlePanel.add(drawCircle);
        drawCircle.addActionListener(new CircleAction(filepath, xInputField, yInputField, radiusInputField, canopyLabel));

        //Adds north functionality
        c.gridy = 1;
        c.gridx = 1;
        c.gridheight = 1;
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        add(northPanel, c);
        //North Input
        JLabel northText1 = new JLabel("Degree pointing towards north: ");
        JLabel northText2 = new JLabel("(Top of image is 0 and goes to 359. Can take doubles.)");
        JTextField northInputField = new JTextField(20);
        //Add components to panel
        northPanel.add(northText1);
        northPanel.add(northText2);
        northPanel.add(northInputField);
        //Adds North Button for the action
        JButton addNorth = new JButton("Add North");
        northPanel.add(addNorth);
        addNorth.addActionListener(new NorthAction(northInputField, canopyLabel));

        //Provide proceed button functionality
        JButton proceed = new JButton("Save & Continue");
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Prop.addProperties("xCenter",String.valueOf(circleX));
                Prop.addProperties("yCenter",String.valueOf(circleY));
                Prop.addProperties("radius", String.valueOf(circleR));
                Prop.addProperties("north", String.valueOf(circleN));
                SquareTheCircle.createTheRectangle(filepath);
                Thresholder thresholder = new Thresholder(SquareTheCircle.getSquareFilepath(), SquareTheCircle.getColourMask(),output);
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

    /**
     * Sets the center x value
     * @param xInputField label where x center is stored
     */
    static void setCircleX (JTextField xInputField) { circleX = Integer.parseInt(xInputField.getText()); }

    /**
     * Sets the center y value
     * @param yInputField label where y center is stored
     */
    static void setCircleY (JTextField yInputField) { circleY = Integer.parseInt(yInputField.getText()); }

    /**
     * Sets the radius of the image section of the file
     * @param radiusInputField label where radius is input
     */
    static void setCircleR (JTextField radiusInputField) { circleR = Integer.parseInt(radiusInputField.getText()); }

    /**
     * Sets the north location of the image (degrees)
     * @param northInputField component where north value is stored
     */
    static void setCircleN (JTextField northInputField) { circleN = Double.parseDouble(northInputField.getText()); }

    /**
     * Creates image from path of image file
     * @param path file path of image
     * @return image stored in file path
     * @throws IOException in the case the file path is not valid
     */
    static BufferedImage readImage (String path) throws IOException { return ImageIO.read(new File(path)); }

    /**
     * Stores the image with border created by center and radius to circledCanopy
     * @param circleInput image to be stored
     */
    static void setCircledCanopy (BufferedImage circleInput) { circledCanopy = circleInput; }

    /**
     * Returns the image stored in circledCanopy
     * @return circledCanopy image. image with border circle
     */
    static BufferedImage getCircledCanopy () { return circledCanopy; }
}

class CircleAction implements ActionListener {
    private JTextField x;
    private JTextField y;
    private JTextField radius;
    private BufferedImage canopyInput;
    private JLabel returnImage;
    private String path;

    CircleAction(String path, JTextField x, JTextField y, JTextField radius, JLabel image) {
        this.path = path;
        this.x = x;
        this.y = y;
        this.radius = radius;
        returnImage = image;
    }

    public void actionPerformed(ActionEvent e) {
        //sets Circle universal variables
        Circle.setCircleX(x);
        Circle.setCircleY(y);
        Circle.setCircleR(radius);

        //Calls colour image from file-path to be able to reset it every time
        try {
            canopyInput = Circle.readImage(path);
        } catch (IOException g) {
            System.out.println("Why");
        }
        //Creates graphic and circle and draws ring onto image
        Graphics image = canopyInput.getGraphics();
        Graphics2D circleRing = (Graphics2D) image;
        int x = Integer.parseInt(this.x.getText());
        int y = Integer.parseInt(this.y.getText());
        Shape ring = createRingShape(x, y, (Circle.circleR + 10));
        circleRing.setColor(Color.RED);
        circleRing.fill(ring);
        circleRing.setColor(Color.BLACK);
        circleRing.draw(ring);

        //clears memory of useless info
        circleRing.dispose();
        image.dispose();

        //Repaints image
        int width = canopyInput.getWidth();
        int height = canopyInput.getHeight();
        Image i = canopyInput.getScaledInstance((500 * width) / height, 500, Image.SCALE_SMOOTH);
        Circle.setCircledCanopy(canopyInput);
        returnImage.setIcon(new ImageIcon(i));
        returnImage.repaint();
    }

    /**
     * Creates border circle to overlay on top of the image
     * @param centerX center x value of image canopy area
     * @param centerY center y value of canopy area
     * @param outerRadius radius of canopy area
     * @return border circle to be shown with the image
     */
    private static Shape createRingShape(double centerX, double centerY, double outerRadius) {
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

class NorthAction implements ActionListener {
    private JTextField north;
    private JLabel returnImage;

    NorthAction(JTextField northInputField, JLabel image){
        north = northInputField;
        returnImage = image;
    }

    public void actionPerformed(ActionEvent e) {
        //Set variables and then calculate
        Circle.setCircleN(north);
        double radians = Math.toRadians(Circle.circleN);
        double radius = (double)Circle.circleR;
        double sin = Math.sin(radians - (0.5 * Math.PI));
        double cos = Math.cos(radians - (0.5 * Math.PI));
        double a = radius * sin;
        double b = radius * cos;
        double yNorth = Circle.circleY + a;
        double xNorth = Circle.circleX + b;

        BufferedImage canopyInput = Circle.getCircledCanopy();
        Graphics image = canopyInput.getGraphics();

        //Creates north dot and draws it into the image
        Graphics2D northArrow = (Graphics2D)image;
        Shape dot = new Ellipse2D.Double(xNorth-15, yNorth-15, 30, 30);
        northArrow.setColor(Color.YELLOW);
        northArrow.fill(dot);
        northArrow.draw(dot);

        //clears memory of useless info
        northArrow.dispose();
        image.dispose();

        //Repaints image
        int width = canopyInput.getWidth();
        int height = canopyInput.getHeight();
        Image i = canopyInput.getScaledInstance((500 * width) / height, 500, Image.SCALE_SMOOTH);
        returnImage.setIcon(new ImageIcon(i));
        returnImage.repaint();
    }
}