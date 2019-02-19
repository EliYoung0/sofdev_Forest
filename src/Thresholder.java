import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Thresholder extends Container {
    public Thresholder (String path, UI ui) {
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
        update.addActionListener(new UpdateAction(ui,Integer.parseInt(threshold.getText()),imageLabel));
        JButton proceed = new JButton("Proceed");

        algPanel.add(threshold);
        algPanel.add(update);
        add(algPanel,BorderLayout.LINE_END);
        add(proceed,BorderLayout.PAGE_END);

    }

}
class UpdateAction implements ActionListener {
    private int threshold;
    private JLabel t;
    private UI ui;
    public UpdateAction(UI ui, int c,JLabel t){
        threshold=c;
        this.ui=ui;
        this.t=t;
    }

    public void actionPerformed(ActionEvent e) {
        Processing p = ui.getProc();
        p.imageProc(threshold);
        BufferedImage image = p.blackAndWhiteOutput;
        int height = image.getHeight();
        int width = image.getWidth();
        Image i = image.getScaledInstance((500 * width) / height, 500, Image.SCALE_SMOOTH);
        t.setIcon(new ImageIcon(i));
        t.repaint();
        t.update(t.getGraphics());
    }
}
