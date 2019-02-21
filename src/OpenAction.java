import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenAction implements ActionListener {
    private static FileSelector outer;
    private UI ui;

    /**
     * Constructor for Open Action
     * @param ui JFrame
     * @param c
     */
    public OpenAction(UI ui, FileSelector c){
        outer=c;
        this.ui=ui;
    }

   /* public void actionPerformed(ActionEvent e) {
        String path = outer.getAddress().getText();
        if(new File(path).exists()) {
            ImagePrepper imagePrepper = new ImagePrepper(path);
            outer.setVisible(false);
            ui.setContentPane(imagePrepper);
            ui.pack();
        }
        else{
            System.out.print("Invalid File Path");
        }
    }*/

    /**
     * Closes file selector container and replaces it with new container
     * @param e Open button click
     */
    public void actionPerformed(ActionEvent e) {
        String path = outer.getAddress().getText();
        if(new File(path).exists()) {
            Thresholder thresholder = new Thresholder(path,ui);
            outer.setVisible(false);
            ui.setProc(path);
            ui.setContentPane(thresholder);
            ui.pack();
        }
        else{
            System.out.print("Invalid File Path");
        }
    }
}
