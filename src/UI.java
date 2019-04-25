import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class UI extends JFrame {

    /**
     * Constructor for overall UI of the program
     * Outermost Layer of the GUI
     */
    UI(){
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {e.printStackTrace(); }
        //TODO:Replace with better name
        setTitle("Forest Canopy Program");
        //File Selector is initial container in frame
        setContentPane(new FileSelector(this));
        pack();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
            }
        });
    }

    /**
     * Called when window is closed
     * Deletes created properties files and square image file if they exist
     */
    private void cleanup(){
        Prop.deleteProperties();
        SquareTheCircle.deleteSquare();
    }
}
