
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileSelector extends Container {
    public FileSelector(){
        setLayout(new BorderLayout());

        JPanel fileBrowser = createBrowser();
        JButton open = new JButton("Open");
        OpenAction o = new OpenAction();
        open.addActionListener(o);
        add(fileBrowser,BorderLayout.CENTER);

        open.setSize(200,100);
        add(open,BorderLayout.AFTER_LAST_LINE);
    }

    private JPanel createBrowser() {
        JPanel fb = new JPanel();
        fb.setLayout(new BoxLayout(fb,BoxLayout.X_AXIS));
        JTextArea address = new JTextArea(1,20);
        JButton browse = new JButton("Browse");
        browse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg");
            chooser.setFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = chooser.showOpenDialog(chooser);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                address.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        fb.add(address);
        fb.add(browse);
        return fb;
    }
}
