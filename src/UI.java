import javax.swing.*;

public class UI extends JFrame {
    public UI(){
        //Replace with better name
        setTitle("Forest Program");
        setContentPane(new FileSelector(this));
        pack();
    }
}
