import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


class Thresholder extends Container {
    private BufferedImage blackOutput;
    Thresholder(String path, UI ui) {
        setLayout(new BorderLayout());
        JLabel imageLabel;
        try {
            BufferedImage image = ImageIO.read(new File(path));
            int height = image.getHeight();
            int width = image.getWidth();
            Image i = image.getScaledInstance((500 * width) / height, 500, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(i));
            add(imageLabel,BorderLayout.LINE_START);
        } catch (IOException ex) {
            imageLabel=new JLabel();
        }
        JPanel algPanel = new JPanel();
        JTextField threshold = new JTextField(10);
        JButton update = new JButton("Update");
        update.addActionListener(new UpdateAction(path,imageLabel,threshold,this));
        JButton proceed = new JButton("Proceed");

        algPanel.add(threshold);
        algPanel.add(update);
        add(algPanel,BorderLayout.LINE_END);
        add(proceed,BorderLayout.PAGE_END);

    }
    void setBlack(BufferedImage b){
        blackOutput=b;
    }
}

class UpdateAction implements ActionListener {
    private JTextField text;
    private JLabel t;
    private String path;
    private Thresholder outer;
    UpdateAction(String path, JLabel t, JTextField thresh,Thresholder outer){
        text=thresh;
        this.t=t;
        this.outer=outer;
        this.path=path;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            int threshold = Integer.parseInt(text.getText());
            if(threshold>=0&&threshold<=255){
                try {
                    BufferedImage bl = Black.makeBlack(path, threshold);
                    Image i = bl.getScaledInstance((500 * bl.getWidth()) / bl.getHeight(), 500, Image.SCALE_SMOOTH);
                    t.setIcon(new ImageIcon(i));
                    t.repaint();
                    t.update(t.getGraphics());
                    outer.setBlack(bl);
                    //Remove in final product. Just to show functionality right now.
                    System.out.println("Gap Fraction is: "+Black.getGapFraction(bl));
                }catch (IOException f){
                    System.out.println("Invalid input image");
                }

            }
            else{
                System.out.println("Threshold must be between an integer between 0 and 255");
            }
        }
        catch (NumberFormatException f){
            System.out.println("Enter a valid integer value");
        }

    }
}
