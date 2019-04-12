import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
            }
        });
    }

    private void cleanup(){
        Prop.deleteProperties();
        if(SquareTheCircle.getSquareFilepath()!=null) {
            SquareTheCircle.deleteSquare();
        }
    }
}
