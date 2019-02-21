import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenAction implements ActionListener {
    private final FileSelector outer;
    private final UI UI;

    /**
     * Constructor for Open Action
     * @param ui JFrame
     * @param c Container holding button
     */
    OpenAction(UI ui, FileSelector c){
        outer=c;
        this.UI=ui;
    }

    /**
     * Closes file selector container and replaces it with new container
     * @param e Open button click
     */
    public void actionPerformed(ActionEvent e) {
        String path = outer.getAddress().getText();
        if(new File(path).exists()) {
            Thresholder thresholder = new Thresholder(path,UI);
            UI.remove(outer);
            UI.setContentPane(thresholder);
            UI.pack();
        }
        else{
            System.out.print("Invalid File Path");
        }
    }
}
