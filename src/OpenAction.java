import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenAction implements ActionListener {
    private static FileSelector outer;
    private JFrame ui;
    public OpenAction(JFrame ui, FileSelector c){
        outer=c;
        this.ui=ui;
    }

    public void actionPerformed(ActionEvent e) {
        String path = outer.getAddress().getText();
        if(new File(path).exists()) {
            ImagePrepper imagePrepper = new ImagePrepper(path);
            outer.setVisible(false);
            ui.setContentPane(imagePrepper);
        }
        else{
            System.out.print("Invalid File Path");
        }
    }
}
