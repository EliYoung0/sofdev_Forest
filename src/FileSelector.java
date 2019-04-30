import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;

class FileSelector extends Container {
    public static String path; //file path of image to be processed in GUI
    private static String[] full; //Array of full path(s) for files to process
    private static boolean flag; //determines if batch processing is needed. True for yes
    private JTextArea address; //Text area where address is input
    private JLabel warning; //Label where warnings are displayed
    private JButton open;
    private Boolean goodExit;

    /**
     * File Selector Container Constructor
     * Window used to select the files to be processed
     * @param ui the outer window that holds File Selector
     */
    FileSelector(UI ui){
        flag = false;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600,510));

        //Create Components
        open = new JButton("Open");
        ActionListener listener = e ->{
            //Checks entered file path(s) are valid
            if(isValidPath(address.getText())) {
                //Stores all path(s) into properties file for batch processing
                Prop.createProperties();
                Prop.addFiles(full);
                String path = FileSelector.getPath();
                if (new File(path).exists()) {
                    goodExit=true;
                    String[] output = new String[5];
                    output[0] = path;
                    Circle circle = new Circle(output, ui, flag);
                    ui.setTitle(ui.getTitle() + ": " + path);
                    ui.setContentPane(circle);
                    ui.pack();
                } else {
                    goodExit = false;
                    warning.setText("Please enter a file path.");
                }
            }
            else{
                goodExit=false;
                warning.setText("File Path(s) Invalid");
            }
        };
        open.addActionListener(listener);

        //Add components to open panel
        JPanel openPanel = new JPanel(new BorderLayout());
        warning = new JLabel(" ");
        warning.setForeground(Color.red);
        warning.setHorizontalAlignment(JLabel.CENTER);
        openPanel.add(warning,BorderLayout.PAGE_START);
        openPanel.add(open,BorderLayout.PAGE_END);

        //Add components to FileSelector
        JPanel fileBrowser = createBrowser();
        add(fileBrowser,BorderLayout.CENTER);
        add(openPanel,BorderLayout.PAGE_END);
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
        address = new JTextArea("",1,1);
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
                String s = "";
                for(int i =0;i<chooser.getSelectedFiles().length;i++){
                    s=s.concat(chooser.getSelectedFiles()[i].getAbsolutePath());
                    if(i<chooser.getSelectedFiles().length-1){ s=s.concat(","); }
                }

                if(isValidPath(s)) {
                    address.setText("");
                    int size = chooser.getSelectedFiles().length;
                    if (size > 1) { flag = true; }
                    for (int i = 0; i < size; i++) {
                        address.append(chooser.getSelectedFiles()[i].getAbsolutePath());
                        if (i < size - 1) { address.append(","); }
                    }
                }
            }
            else{ warning.setText("Invalid File Path(s)"); }
        });
        browserPanel.add(addressScroll);
        browserPanel.add(browse);
        return browserPanel;
    }

    /**
     * Returns the file path text field object
     * @return the address text field
     */
    public static String getPath() { return path; }

    /**
     * Checks if input string is a valid file path or multiple valid file paths
     * @param s String to be checked
     * @return false if one or more of the input paths are invalid. true otherwise
     */
    private boolean isValidPath(String s){
        assert s!=null;
        //Splits s into an array of paths
        String[] paths = s.split(",");
        //If a single path
        if(paths.length==1){
            File f = new File(s);
            //Checks path exits
            if(f.exists()){
                //If path is a directory
                if(f.isDirectory()){
                    File[] files = f.listFiles((dir, filename) -> filename.toLowerCase().endsWith(".jpg"));
                    assert files != null;
                    //Checks that directory has jpg's within
                    if(files.length==0){return false;}
                    //Sets flag if more than one file
                    flag = files.length > 1;
                    //Checks each file is valid
                    for (File fi:files) {
                        isValidFile(fi.getAbsolutePath());
                    }
                    //Sets path for GUI to use to be the first file
                    path = files[0].getAbsolutePath();
                    full=new String[]{f.getAbsolutePath()};
                    return true;
                }
                else {
                    //If a path is a file checks file is valid
                    if (!isValidFile(f.getAbsolutePath())) {
                        return false;
                    }
                    flag=false;
                }
            }
        }
        //If multiple checks that all files are valid
        else{
            for (String p:paths) {
                if(!isValidFile(p)){return false;}
            }
            flag=true;
        }
        full=paths;
        path = paths[0];
        return true;
    }

    /**
     * Checks that a single file path is valid
     * @param p file path to check
     * @return true if a valid jpg file
     */
    private boolean isValidFile(String p){
        //Check path has correct extension
        if(!p.toLowerCase().endsWith(".jpg")){return false;}
        File f = new File(p);
        //Check image file exists
        if(!f.exists()){return false;}
        //Checks file is not a directory. Might not be needed
        return !f.isDirectory();
    }

    /**
     * Returns address bar
     * @return address bar
     */
    JTextArea getAddress() {
        return address;
    }

    /**
     * Get open button
     * @return open button
     */
    JButton getOpen() {
        return open;
    }

    /**
     * Used to test file select
     * @return true if properly exits, false if error occurs
     */
    Boolean getGoodExit() {
        return goodExit;
    }

    /**
     * Used by test class.
     * @return full path for batch
     */
    String[] getFull() {
        return full;
    }

}
