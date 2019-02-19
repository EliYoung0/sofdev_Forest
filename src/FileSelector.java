
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class FileSelector extends Container {


    private JTextArea address;

    public FileSelector(JFrame UI){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600,500));

        JPanel fileBrowser = createBrowser();
        JButton open = new JButton("Open");
        OpenAction o = new OpenAction(UI,this);
        open.addActionListener(o);
        add(fileBrowser,BorderLayout.CENTER);
        add(open,BorderLayout.PAGE_END);
    }

    /**
     * Creates the Browser frame
     * Contains a text field and browse button
     * @return returns the Panel to be shown by FileSelector
     */
    private JPanel createBrowser() {
        JPanel fb = new JPanel();
        fb.setBorder(new EmptyBorder(225,100,225,100));
        fb.setLayout(new BoxLayout(fb,BoxLayout.X_AXIS));
        address = new JTextArea("/Users/kepler/Desktop/DCSN1068.JPG",1,1);
        address.setBorder(new EtchedBorder(1,null,Color.black));
        JButton browse = new JButton("Browse");
        //Opens a new window when browse button is clicked
        browse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            //Sets the allowed file extension types
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg");
            chooser.setFileFilter(filter);
            //Sets whether files or folders or both are allowed
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

    /**
     * Returns the file path text field object
     * @return the address text field
     */
    public JTextArea getAddress() {
        return address;
    }

}
