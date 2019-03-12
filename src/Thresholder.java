import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


class Thresholder extends Container {

    public static BufferedImage blackOutput;
    private int currentThreshold;
    private String filepath;

    Thresholder(String path, UI ui) {
        filepath = path;
        setLayout(new GridBagLayout());
        JLabel imageLabel;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridheight=3;
        try {
            BufferedImage image = ImageIO.read(new File(path));
            int height = image.getHeight();
            int width = image.getWidth();
            Image i = image.getScaledInstance((500 * width) / height, 500, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(i));
            add(imageLabel, c);
        } catch (IOException ex) {
            imageLabel = new JLabel();
        }

        //For future algorithms
        JPanel algPanel = new JPanel();
        algPanel.setLayout(new BoxLayout(algPanel, BoxLayout.Y_AXIS));

        //for the basic threshold panel
        JPanel threshPanel = new JPanel();
        threshPanel.setLayout(new GridLayout());
        threshPanel.setLayout(new GridBagLayout());

        JTextField threshold = new JTextField(20);

        JButton update = new JButton("Update");

        JTextArea consoleOutput = new JTextArea("");
        consoleOutput.setEditable(false);
        JScrollPane consoleScroll = new JScrollPane(consoleOutput);
        consoleScroll.setPreferredSize(new Dimension(350,400));

        update.addActionListener(new UpdateAction(path, imageLabel, threshold, this, consoleOutput));


        JLabel updateText = new JLabel("<html>Simple converter to black and white." +
                "<br>Enter the average RGB value which is the lower bound for white:  " +
                "<br> (RGB values are from 0-255.) </html>");

        c.gridheight=1;
        c.gridx=1;
        c.gridy=0;
        add(updateText,c);
        threshPanel.add(threshold);
        threshPanel.add(update);
        c.gridy=1;
        add(threshPanel,c);
        c.gridy=2;
        add(consoleScroll,c);

        //Give proceed button functionality
        JButton proceed = new JButton("Save & Continue");
        ActionListener listener = new ActionListener()
        {
            public void actionPerformed(ActionEvent event){

            }
        };
        proceed.addActionListener(listener);
        //This part used to close the program
        /*proceed.addActionListener(e -> {
            if(blackOutput!=null) {
                saveBlack();
            }
            System.exit(0);
        });*/

        //Add components to Container
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=2;
        add(proceed, c);

    }

    void setBlack(BufferedImage b) {
        blackOutput = b;
    }

    private void saveBlack() {
        String newFilepath;
        newFilepath = filepath.replaceAll("(.[a-zA-Z]{3,4}$)",
                "_basic_" + currentThreshold + "_" + java.time.LocalDate.now() + "$1");
        File outputFile = new File(newFilepath);
        try {
            ImageIO.write(blackOutput, "jpg", outputFile);
        } catch (IOException ignored) {
        }
    }

    public static BufferedImage getBlackOutput() {
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

    UpdateAction(String path, JLabel t, JTextField thresh, Thresholder outer, JTextArea output) {
        text = thresh;
        this.t = t;
        this.outer = outer;
        this.path = path;
        console = output;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            int threshold = Integer.parseInt(text.getText());
            if (threshold >= 0 && threshold <= 255) {

                    BufferedImage bl = Black.makeBlack(path, threshold);
                    Image i = bl.getScaledInstance((500 * bl.getWidth()) / bl.getHeight(), 500, Image.SCALE_SMOOTH);
                    t.setIcon(new ImageIcon(i));
                    t.repaint();
                    outer.setBlack(bl);
                    outer.setCurrentThreshold(threshold);
                    //Remove following line in final product. Just to show functionality right now.
                    console.append("\nGap Fraction is: " + Black.getGapFraction(bl));

            } else {
                console.append("\nThreshold must be an integer between 0 and 255.");
            }
        } catch (NumberFormatException f) {
            console.append("\nEnter a valid integer value.");
        }
        catch (IOException ex){
            console.append("\nInvalid image file path");
        }

    }
}
