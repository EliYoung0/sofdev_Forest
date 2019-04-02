import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;

public class FileSelector extends Container {
    private static String path;
    public static String[] full;

    public FileSelector(UI ui){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600,500));
        //Create Components
        JButton open = new JButton("Open");
        ActionListener listener = e -> {
            Batch.createProperties();
            Batch.addFiles(full);
            String path = FileSelector.getPath();
            if(new File(path).exists()) {
                Circle circle = new Circle(path, ui);
                ui.setContentPane(circle);
                ui.pack();
            }
            else{
                System.out.print("Invalid File Path");
            }
        };
        open.addActionListener(listener);
        JPanel fileBrowser = createBrowser();
        //Add components to FileSelector
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
        fb.setBorder(new EmptyBorder(215,100,215,100));
        fb.setLayout(new BoxLayout(fb,BoxLayout.X_AXIS));
        JTextArea address = new JTextArea("",1,1);
        address.setBorder(new EtchedBorder(1,null,Color.black));
        JScrollPane addressScroll = new JScrollPane(address, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        addressScroll.setSize(new Dimension(313, 40));
        JButton browse = new JButton("Browse");
        //Opens a new window when browse button is clicked
        browse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            //Sets the allowed file extension types
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg");
            chooser.setFileFilter(filter);
            //Sets whether files or folders or both are allowed
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setMultiSelectionEnabled(true);
            int returnVal = chooser.showOpenDialog(chooser);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                full = new String[chooser.getSelectedFiles().length];
                address.setText("");
                if(chooser.getSelectedFiles()[0].isDirectory()){
                    File p = chooser.getSelectedFiles()[0];
                    File[] fs = p.listFiles((dir, filename) -> filename.endsWith(".jpg"));
                    assert fs!=null;
                    setPath(fs[0].getAbsolutePath());
                }
                else{ setPath(chooser.getSelectedFiles()[0].getAbsolutePath());}
                for (int i = 0; i < chooser.getSelectedFiles().length; i++) {
                    full[i]=chooser.getSelectedFiles()[i].getAbsolutePath();
                    address.append(full[i]);
                    if(i<chooser.getSelectedFiles().length-1){
                        address.append(", ");
                    }
                }

            }
        });
        fb.add(addressScroll);
        fb.add(browse);
        return fb;
    }

    /**
     * Returns the file path text field object
     * @return the address text field
     */
    static String getPath() {
        return path;
    }

    static void setPath(String val) {
        path = val;
    }

}
