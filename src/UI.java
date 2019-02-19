import javax.swing.*;

public class UI extends JFrame {
    private Processing proc;
    /**
     * Constructor for overall UI of the program
     * Outermost Layer of the GUI
     */
    public UI(){
        //Replace with better name
        setTitle("Forest Program");
        //File Selector is initial container in frame
        setContentPane(new FileSelector(this));
        pack();
    }

    public void setProc(String filename){
        proc = new Processing(filename);
    }

    public Processing getProc(){
        return proc;
    }
}
