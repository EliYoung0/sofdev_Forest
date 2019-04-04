import javax.swing.*;
import java.awt.*;

public class BatchUI extends Container {
    BatchUI(){
       //super(new BorderLayout());

        //Create the demo's UI.
        JButton startButton = new JButton("Start");
        startButton.setActionCommand("start");
        //startButton.addActionListener(this);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        JTextArea taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        //setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
}
