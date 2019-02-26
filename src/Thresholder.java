import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


class Thresholder extends Container {

    private BufferedImage blackOutput;
    private int currentThreshold;
    private String filepath;

    Thresholder(String path, UI ui) {
        filepath=path;
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
        algPanel.setLayout(new BoxLayout(algPanel, BoxLayout.Y_AXIS));

        JTextField threshold = new JTextField();
        JButton update = new JButton("Update");
        JTextArea consoleOutput = new JTextArea("");
        consoleOutput.setEditable(false);

        update.addActionListener(new UpdateAction(path,imageLabel,threshold,this, consoleOutput));
        JButton proceed = new JButton("Save & Exit");

        JLabel updateText = new JLabel("<html>Simple converter to black and white.<br>Enter the average RGB value which is the lower bound for white:   </html>");


        algPanel.add(updateText);
        algPanel.add(threshold);
        algPanel.add(update);
        algPanel.add(consoleOutput);
        add(algPanel,BorderLayout.LINE_END);
        proceed.addActionListener(e->{
            saveBlack();
            System.exit(0);
        });
        add(proceed,BorderLayout.PAGE_END);

    }
    void setBlack(BufferedImage b){
        blackOutput=b;
    }
    void saveBlack(){
        String newFilepath;
        newFilepath = filepath.replaceAll("(.[a-zA-Z]{3,4}$)","_basic_"+currentThreshold+"_"+java.time.LocalDate.now()+"$1");
        File outputFile = new File(newFilepath);
        try {
            ImageIO.write(blackOutput,"jpg",outputFile);
        }
        catch (IOException ignored){}
    }

    public BufferedImage getBlackOutput() {
        return blackOutput;
    }

    public void setCurrentThreshold(int currentThreshold) {
        this.currentThreshold = currentThreshold;
    }
}

class UpdateAction implements ActionListener {
    private JTextField text;
    private JLabel t;
    private String path;
    private Thresholder outer;
    private JTextArea console;
    UpdateAction(String path, JLabel t, JTextField thresh,Thresholder outer, JTextArea output){
        text=thresh;
        this.t=t;
        this.outer=outer;
        this.path=path;
        console=output;
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
                    outer.setCurrentThreshold(threshold);
                    //Remove in final product. Just to show functionality right now.
                    console.append("\nGap Fraction is: "+Black.getGapFraction(bl));
                }catch (IOException f){
                    console.append("\nInvalid input image.");
                }

            }
            else{
                console.append("\nThreshold must be between an integer between 0 and 255.");
            }
        }
        catch (NumberFormatException f){
            console.append("\nEnter a valid integer value.");
        }

    }
}
