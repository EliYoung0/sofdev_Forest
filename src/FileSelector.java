
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class FileSelector extends Container {


    private JTextArea address;

    public FileSelector(JFrame UI){
        setLayout(new BorderLayout());

        JPanel fileBrowser = createBrowser();
        JButton open = new JButton("Open");
        OpenAction o = new OpenAction(UI,this);
        open.addActionListener(o);
        add(fileBrowser,BorderLayout.CENTER);
        add(open,BorderLayout.PAGE_END);
    }

    private JPanel createBrowser() {
        JPanel fb = new JPanel();
        fb.setLayout(new BoxLayout(fb,BoxLayout.X_AXIS));
        address = new JTextArea(1,20);
        JButton browse = new JButton("Browse");
        browse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg");
            chooser.setFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = chooser.showOpenDialog(chooser);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                address.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        fb.add(address);
        fb.add(browse);
        return fb;
    }

    public JTextArea getAddress() {
        return address;
    }

}
