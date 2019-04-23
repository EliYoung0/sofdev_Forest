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
    private static String filepath;
    static int circleX;
    static int circleY;
    static int circleR;
    static double circleN;
    static double circleZ;
    static Shape dot;
    private static Shape border;
    private static JLabel canopyLabel;

    /**
     * Constructor of Circle Container
     * Used to define image center location, radius, and north direction
     * @param output String array containing output data including image path
     * @param ui outer window
     * @param flag true if batch is to be called
     */
    Circle(String[] output, UI ui, boolean flag){
        border =null;
        filepath = output[0];
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        //Adding image to grid
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight=3;
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
        JLabel directionText = new JLabel("<html> Input values for the circle below. " +
                "<br> (Circle must be drawn before placing north) </html>");
        //X-input
        JLabel xText = new JLabel("Centre x pixel value: ");
        JTextField xInputField = new JTextField(20);
        //Y-input
        JLabel yText = new JLabel("Centre y pixel value: ");
        JTextField yInputField = new JTextField(20);
        //Radius input
        JLabel radiusText = new JLabel("Radius pixel value: ");
        JTextField radiusInputField = new JTextField(20);
        //Adds Draw Circle functionality
        JButton drawCircle = new JButton("Draw Circle");
        drawCircle.addActionListener(new CircleAction(filepath, xInputField, yInputField, radiusInputField, canopyLabel));
        //Adding components to panel
        circlePanel.add(directionText);
        circlePanel.add(xText);
        circlePanel.add(xInputField);
        circlePanel.add(yText);
        circlePanel.add(yInputField);
        circlePanel.add(radiusText);
        circlePanel.add(radiusInputField);
        circlePanel.add(drawCircle);

        //Adds north input functionality
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
        addNorth.addActionListener(new NorthAction(northInputField));

        //Adds zenith functionality
        c.gridy = 2;
        c.gridx = 1;
        c.gridheight = 1;
        JPanel zenithPanel = new JPanel();
        zenithPanel.setLayout(new BoxLayout(zenithPanel, BoxLayout.Y_AXIS));
        add(zenithPanel, c);
        //Zenith Input
        JLabel zenithText1 = new JLabel("Time of day this image was taken at: ");
        JLabel zenithText2 = new JLabel("(This will draw where the Sun is at the time the image was taken.)");
        JLabel zenithText3 = new JLabel("(Time of day from 0-24 in decimal format, e.g. 1:30pm is 13.5.)");
        JTextField zenithInputField = new JTextField(20);
        //Adds components to panel
        zenithPanel.add(zenithText1);
        zenithPanel.add(zenithText2);
        zenithPanel.add(zenithText3);
        zenithPanel.add(zenithInputField);
        //Adds zenith button for the action
        JButton addZenith = new JButton("Add Zenith");
        zenithPanel.add(addZenith);
        addZenith.addActionListener(new ZenithAction(zenithInputField));

        //Provide proceed button functionality
        JButton proceed = new JButton("Save & Continue");
        ActionListener listener = e -> {
            Prop.addProperty("xCenter",String.valueOf(circleX));
            Prop.addProperty("yCenter",String.valueOf(circleY));
            Prop.addProperty("radius", String.valueOf(circleR));
            Prop.addProperty("north", String.valueOf(circleN));
            SquareTheCircle.createTheRectangle(filepath);
            Thresholder thresholder = new Thresholder(SquareTheCircle.getSquareFilepath(),
                    SquareTheCircle.getColourMask(),output,ui,flag);
            ui.setContentPane(thresholder);
            ui.pack();
        };
        proceed.addActionListener(listener);
        //Add proceed button to container
        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 2;
        add(proceed, c);
    }

    static void setCircleX (JTextField xInputField) { circleX = Integer.parseInt(xInputField.getText()); }
    static void setCircleY (JTextField yInputField) { circleY = Integer.parseInt(yInputField.getText()); }
    static void setCircleR (JTextField radiusInputField) { circleR = Integer.parseInt(radiusInputField.getText()); }
    static void setCircleN (JTextField northInputField) { circleN = Double.parseDouble(northInputField.getText()); }
    static void setCircleZ (JTextField zenithInputField) { circleZ = Double.parseDouble(zenithInputField.getText());}
    static void setBorder(Shape circleInput) { border = circleInput; }
    static void setCircleDot(Shape dotInput) { dot = dotInput;}

    static BufferedImage readImage (String path) throws IOException { return ImageIO.read(new File(path)); }

    static void drawNorth(Shape dot) {
        try {
            BufferedImage base = readImage(filepath);
            Graphics image = base.getGraphics();
            Graphics2D layers = (Graphics2D) image;
            layers.setColor(Color.RED);
            layers.fill(border);
            layers.setColor(Color.BLACK);
            layers.draw(border);
            layers.setColor(Color.MAGENTA);
            layers.fill(dot);
            layers.setColor(Color.BLACK);
            layers.draw(dot);

            layers.dispose();
            image.dispose();
            int width = base.getWidth();
            int height = base.getHeight();
            Image i = base.getScaledInstance((500 * width) / height, 500, Image.SCALE_SMOOTH);
            canopyLabel.setIcon(new ImageIcon(i));
            canopyLabel.repaint();

        }
        catch (IOException e) { e.printStackTrace(); }
    }
    static void drawZenith(Shape sun) {
        try {
            BufferedImage base = readImage(filepath);
            Graphics image = base.getGraphics();
            Graphics2D layers = (Graphics2D) image;
            layers.setColor(Color.RED);
            layers.fill(border);
            layers.setColor(Color.BLACK);
            layers.draw(border);
            layers.setColor(Color.MAGENTA);
            layers.fill(dot);
            layers.setColor(Color.BLACK);
            layers.draw(dot);
            layers.setColor(Color.YELLOW);
            layers.fill(sun);
            layers.setColor(Color.BLACK);
            layers.draw(sun);

            layers.dispose();
            image.dispose();
            int width = base.getWidth();
            int height = base.getHeight();
            Image i = base.getScaledInstance((500 * width) / height, 500, Image.SCALE_SMOOTH);
            canopyLabel.setIcon(new ImageIcon(i));
            canopyLabel.repaint();

        }
        catch (IOException e) { e.printStackTrace(); }
    }
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
        try { canopyInput = Circle.readImage(path); }
        catch (IOException g) {g.printStackTrace(); }
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
        Circle.setBorder(ring);
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

    NorthAction(JTextField northInputField){
        north = northInputField;
    }

    public void actionPerformed(ActionEvent e) {
        //Set variables and then calculate
        Circle.setCircleN(north);
        double radians = Math.toRadians(Circle.circleN);
        double radius = (double)Circle.circleR;
        double sin = Math.sin(radians - (0.5 * Math.PI));
        double cos = Math.cos(radians + (0.5 * Math.PI));
        double a = radius * sin;
        double b = radius * cos;
        double yNorth = Circle.circleY + a;
        double xNorth = Circle.circleX + b;

        //Creates north dot and draws it into the image
        Shape dot = new Ellipse2D.Double(xNorth-15, yNorth-15, 30, 30);
        Circle.setCircleDot(dot);
        Circle.drawNorth(dot);
    }
}

class ZenithAction implements ActionListener {
    private JTextField zenith;

    ZenithAction(JTextField zenithInputField){
        zenith = zenithInputField;
    }

    public void actionPerformed(ActionEvent e) {
        //Sets variable for later call
        Circle.setCircleZ(zenith);
        //Calculates starting point (East)
        double north = Math.toRadians(Circle.circleN);
        double radius = (double)Circle.circleR;
        double sin = Math.sin(north);
        double cos = Math.cos(north + Math.PI);
        double a = radius * sin;
        double b = radius * cos;
        //Calculates distance from east to west according to hour put in
        /*
        ATTENTION
        THIS ASSUMES BEING CLOSE TO THE EQUATOR
        Assumes sun travels simply from East to West along the centre of the image
         */
        double zenith = Circle.circleZ;
        double zDistance = -1;
        //These if statements assume sun will rise at or around 06:00 and sets at or around 18:00 daily
        if (zenith < 6.0) {
            zDistance = 0;
            System.out.println(zenith);
        } else if (zenith >= 6.0 && zenith <= 18.0) {
            //Imitates the path of the sun along an invisible half-sphere
            zDistance = 1824 + (980 * Math.cos((Math.PI/12 * zenith) + (Math.PI/2)));
        } else if (zenith > 18.0){
            zDistance = radius * 2;
            System.out.println(zenith);
        }

        double yZenith = Circle.circleY + a;
        double xZenith = zDistance;

        Shape sun = new Ellipse2D.Double(xZenith-15, yZenith-15, 30,30);
        Circle.drawZenith(sun);
    }
}