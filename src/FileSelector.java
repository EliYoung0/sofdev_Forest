import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;

class FileSelector extends Container {
    private static String path; //file path of image to be processed in GUI
    private static String[] full; //Array of full path(s) for files to process
    private static boolean flag; //determines if batch processing is needed. True for yes
    private JButton open;

    /**
     * File Selector Container Constructor
     * Window used to select the files to be processed
     * @param ui the outer window that holds File Selector
     */
    FileSelector(UI ui){
        flag = false;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600,500));
        //Create Components
        open = new JButton("Open");
        open.setEnabled(false);
        ActionListener listener = e ->{
            if(path!=null){
                Prop.createProperties();
                Prop.addFiles(full);
                String path = FileSelector.getPath();
                if (new File(path).exists()) {
                    String[] output = new String[5];
                    output[0] = path;
                    Circle circle = new Circle(output, ui, flag);
                    ui.setContentPane(circle);
                    ui.pack();
                } else {
                    System.out.print("Invalid File Path");
                }
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
        JPanel browserPanel = new JPanel();
        //Set layout of panel
        browserPanel.setBorder(new EmptyBorder(215,100,215,100));
        browserPanel.setLayout(new BoxLayout(browserPanel,BoxLayout.X_AXIS));
        //Create components of panel: address bar and browse button
        JTextArea address = new JTextArea("",1,1);
        address.setBorder(new EtchedBorder(1,null,Color.black));
        JScrollPane addressScroll = new JScrollPane(address, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        addressScroll.setSize(new Dimension(313, 40));
        JButton browse = new JButton("Browse Files");
        //Opens a new window when browse button is clicked
        browse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            //Sets the allowed file extension types
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG Images", "jpg");
            chooser.setFileFilter(filter);
            //Sets whether files or folders or both are allowed
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setMultiSelectionEnabled(true);
            int returnVal = chooser.showOpenDialog(chooser);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                full = new String[chooser.getSelectedFiles().length];
                address.setText("");
                //Checks if selected path is directory
                if(chooser.getSelectedFiles()[0].isDirectory()){
                    if(chooser.getSelectedFiles().length>1){return;}
                    //If so sets used path to first jpg in directory
                    File p = chooser.getSelectedFiles()[0];
                    File[] files = p.listFiles((dir, filename) -> filename.toLowerCase().endsWith(".jpg"));
                    assert files != null;
                    if(files.length==0){return;}
                    if(files.length>1){flag=true;}
                    setPath(files[0].getAbsolutePath());
                }
                //Otherwise sets used path to first image selected
                else{setPath(chooser.getSelectedFiles()[0].getAbsolutePath());}


                //Goes through each file or directory selected and stores it for later
                int size=chooser.getSelectedFiles().length;
                if(size>1){flag=true;}
                for (int i = 0; i <size; i++) {
                    full[i] = chooser.getSelectedFiles()[i].getAbsolutePath();
                    //Displays each path in address box
                    address.append(full[i]);
                    if(i<size-1){ address.append(", ");}
                }
                open.setEnabled(true);
            }
        });
        browserPanel.add(addressScroll);
        browserPanel.add(browse);
        return browserPanel;
    }

    /**
     * Returns the file path text field object
     * @return the address text field
     */
    private static String getPath() { return path; }

    /**
     * Sets the path of the first image to process
     * @param val String file path of the first image
     */
    private static void setPath(String val) { path = val; }

}
