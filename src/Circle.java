import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


class Circle extends Container {

    private BufferedImage circledOutput;
    private BufferedImage blackInput = Thresholder.blackOutput;

    Circle(UI ui){
        setLayout(new GridBagLayout());
        JLabel imageLabel;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        //Adding image to grid
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight=3;
        BufferedImage image = blackInput;
        int height = image.getHeight();
        int width = image.getWidth();
        Image i = image.getScaledInstance((500*width)/height, 500, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(i));
        add(imageLabel, c);

        //DRAW CIRCLE
        //X-input
        JLabel xInputText = new JLabel("Centre x pixel value: ");
        JTextField xInputField = new JTextField(20);
        //Y-input
        JLabel yInputText = new JLabel("Centre y pixel value: ");
        JTextField yInputField = new JTextField(20);
        //Diameter input
        JLabel diameterInputText = new JLabel("Diameter pixel value: ");
        JTextField diameterInputField = new JTextField(20);
        //"Update" button
        JButton update = new JButton("Draw Circle");
        //Creating panel and adding components to panel
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2;
        JPanel circlePanel = new JPanel();
        circlePanel.setLayout(new BoxLayout(circlePanel, BoxLayout.Y_AXIS));
        add(circlePanel, c);
        circlePanel.add(xInputText);
        circlePanel.add(xInputField);
        circlePanel.add(yInputText);
        circlePanel.add(yInputField);
        circlePanel.add(diameterInputText);
        circlePanel.add(diameterInputField);



    }

}

class CircleAction implements ActionListener {
    public void actionPerformed(ActionEvent e){

    }
}