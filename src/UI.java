import javax.swing.*;

public class UI extends JFrame {
    /**
     * Constructor for overall UI of the program
     * Outermost Layer of the GUI
     */
    public UI(){
        //Replace with better name
        setTitle("Forest Program");
        setContentPane(new FileSelector(this));
        pack();
    }
}