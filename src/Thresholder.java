import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


class Thresholder extends Container {

    private static BufferedImage blackOutput;
    static int method;
    private int currentThreshold;
    private String filepath;
    boolean[][] mask;

    Thresholder(String path, boolean[][] mask, String[] output, UI ui) {
        this.mask = mask;
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
        //Threshold Algorithm radio button
        JButton nobis = new JButton("Nobis Algorithm");
        JLabel finalImageLabel = imageLabel;
        nobis.addActionListener(e -> {
            try {
                BufferedImage og= ImageIO.read(new File(path));
                BufferedImage bl = Algorithms.nobis(og,mask);
                method=1;
                Image i = bl.getScaledInstance((500 * bl.getWidth()) / bl.getHeight(), 500, Image.SCALE_SMOOTH);
                finalImageLabel.setIcon(new ImageIcon(i));
                finalImageLabel.repaint();
                setBlack(bl);
                //Remove following line in final product. Just to show functionality right now.
                consoleOutput.append("\nGap Fraction is: " + Black.getGapFraction(bl,mask));
            }
            catch (IOException ex){ex.printStackTrace();}
        });

        c.gridheight=1;
        c.gridx=1;
        c.gridy=0;
        GridBagConstraints tr = new GridBagConstraints();
        tr.fill = GridBagConstraints.VERTICAL;
        tr.gridy=0;
        add(updateText,c);
        threshPanel.add(threshold,tr);
        tr.gridy=1;
        threshPanel.add(update,tr);
        tr.gridy=2;
        threshPanel.add(nobis,tr);
        c.gridy=1;
        add(threshPanel,c);
        c.gridy=2;
        add(consoleScroll,c);

        //Give proceed button functionality
        JButton proceed = new JButton("Save & Continue");
        //This part used to close the program
        proceed.addActionListener(e -> {
            String[] methods = new String[]{"Manual","Nobis"};
            output[1]=methods[method];
            Prop.addProperty("method",String.valueOf(method));
            if(method==0){
                Prop.addProperty("threshold",String.valueOf(currentThreshold));
                output[2]=String.valueOf(currentThreshold);
            }
            else{output[2]="N/A";}
            output[3]="";
            output[4]=String.valueOf(Black.getGapFraction(blackOutput,mask));
            try{CSV.write(output);}
            catch (IOException it){ it.printStackTrace(); }
            BatchUI bui = new BatchUI();
            ui.setContentPane(bui);
            ui.pack();
        });

        //Add components to Container
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=2;
        add(proceed, c);

    }

    void setBlack(BufferedImage b) { blackOutput = b; }

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

    /**
     * Returns the black & white image
     * @return black white version of image
     */
    public static BufferedImage getBlackOutput() { return blackOutput; }

    /**
     * Sets the current threshold being used
     * @param currentThreshold threshold input or calculated by algorithm
     */
    void setCurrentThreshold(int currentThreshold) { this.currentThreshold = currentThreshold; }
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
                    Thresholder.method =0;
                    BufferedImage og = ImageIO.read(new File(path));
                    BufferedImage bl = Black.makeBlack(og, threshold,outer.mask);
                    Image i = bl.getScaledInstance((500 * bl.getWidth()) / bl.getHeight(), 500, Image.SCALE_SMOOTH);
                    t.setIcon(new ImageIcon(i));
                    t.repaint();
                    outer.setBlack(bl);
                    outer.setCurrentThreshold(threshold);
                    //Remove following line in final product. Just to show functionality right now.
                    console.append("\nGap Fraction is: " + Black.getGapFraction(bl,outer.mask));

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
