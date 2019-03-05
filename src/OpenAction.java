import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public abstract class OpenAction implements ActionListener {
    /**
     * Closes file selector container and replaces it with new container
     * ""e"" Open button click
     */

    public void thresholderAction(){
        ActionListener listener = new ActionListener()
        {
            public void actionPerformed(ActionEvent event){
                String path = FileSelector.getAddress().getText();
                final UI UI = FileSelector.ui;
                if(new File(path).exists()) {
                    Thresholder thresholder = new Thresholder(path,UI);
                    UI.setContentPane(thresholder);
                    UI.pack();
                }
                else{
                    System.out.print("Invalid File Path");
                }
            }
        };

    }

    public void circleAction(){
        ActionListener listener = new ActionListener()
        {
            public void actionPerformed(ActionEvent event){
                Circle circle = new Circle(UI);
                //ADD THINGS HERE
            }
        };
    }



}
