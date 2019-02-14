import javax.swing.*;

public class UI extends JFrame {
    public UI(){
        //Replace with better name
        this.setTitle("Forest Program");
        this.setSize(600,500);
        this.setContentPane(new FileSelector(this));
        pack();
    }
}
