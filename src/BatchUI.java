import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BatchUI extends Container {
    BatchUI(boolean[][] mask,String csvPAth) throws IOException {
        setLayout(new BorderLayout());
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        Batch.run(mask,csvPAth,progressBar);
        progressBar.setStringPainted(true);
        JPanel panel = new JPanel();
        panel.add(progressBar);
        add(panel, BorderLayout.PAGE_START);
    }
}
