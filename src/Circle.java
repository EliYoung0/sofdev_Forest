import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

class Circle extends Container {
    private BufferedImage circledOutput;
    private BufferedImage blackInput = Thresholder.blackOutput;

    Circle(UI ui){
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        //Adding image to grid
        JLabel imageLabel = addImageToGrid(c);

        //Creating input panel for circle parameters
        c.gridy = 0;
        c.gridheight = 2;
        JPanel circlePanel = new JPanel();
        circlePanel.setLayout(new BoxLayout(circlePanel, BoxLayout.Y_AXIS));
        add(circlePanel, c);
        //X-input
        JLabel xInputText = new JLabel("Centre x pixel value: ");
        JTextField xInputField = new JTextField(20);
        //Y-input
        JLabel yInputText = new JLabel("Centre y pixel value: ");
        JTextField yInputField = new JTextField(20);
        //Diameter input
        JLabel diameterInputText = new JLabel("Diameter pixel value: ");
        JTextField diameterInputField = new JTextField(20);
        //"Draw Circle" button
        JButton drawCircle = new JButton("Draw Circle");c.gridx = 1;
        //Adding components to panel
        circlePanel.add(xInputText);
        circlePanel.add(xInputField);
        circlePanel.add(yInputText);
        circlePanel.add(yInputField);
        circlePanel.add(diameterInputText);
        circlePanel.add(diameterInputField);
        circlePanel.add(drawCircle);


        drawCircle.addActionListener(new CircleAction(xInputField, yInputField, diameterInputField, imageLabel));

    }

    private JLabel addImageToGrid(GridBagConstraints c){
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight=3;
        JLabel imageLabel;
        BufferedImage image = blackInput;
        int height = image.getHeight();
        int width = image.getWidth();
        Image i = image.getScaledInstance((500*width)/height, 500, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(i));
        add(imageLabel, c);
        return imageLabel;
    }

}

class CircleAction implements ActionListener {
    private JTextField x;
    private JTextField y;
    private JTextField diameter;
    private BufferedImage blackInput;
    private JLabel returnImage;

    CircleAction(JTextField x, JTextField y, JTextField diameter, JLabel image){
        blackInput =Thresholder.getBlackOutput();
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        returnImage = image;
    }

    public void actionPerformed(ActionEvent e){
        int x = Integer.parseInt(this.x.getText());
        int y = Integer.parseInt(this.y.getText());
        int diameter = Integer.parseInt(this.diameter.getText());

        int width = blackInput.getWidth();
        int height = blackInput.getHeight();

        Graphics circle = blackInput.getGraphics();
        Graphics2D circleRing = (Graphics2D)circle;
        Shape ring = createRingShape(x, y, (diameter+10), 10);
        circleRing.setColor(Color.RED);
        circleRing.fill(ring);
        circleRing.setColor(Color.BLACK);
        circleRing.draw(ring);

        circleRing.dispose();
        circle.dispose();

//NEED TO FIX THE REDRAWING ISSUES
        Image i = blackInput.getScaledInstance((500*width)/height, 500, Image.SCALE_SMOOTH);
        //JLabel imagelabel = new JLabel(new ImageIcon(i));
        //returnImage = imagelabel;
        returnImage.setIcon(new ImageIcon(i));
        returnImage.repaint();
    }

    private static Shape createRingShape(double centerX, double centerY, double outerRadius, double thickness){
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