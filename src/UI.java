import javax.swing.*;

class UI extends JFrame {

    /**
     * Constructor for overall UI of the program
     * Outermost Layer of the GUI
     */
    UI(){
        //Replace with better name
        setTitle("Forest Program");
        //File Selector is initial container in frame
        setContentPane(new FileSelector(this));
        setResizable(true);
        pack();
    }
}
