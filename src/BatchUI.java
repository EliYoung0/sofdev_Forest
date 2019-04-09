import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class BatchUI extends Container {
    BatchUI(boolean[][] mask,String csvPAth) {
        setLayout(new BorderLayout());
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        JPanel panel = new JPanel();
        JButton start = new JButton("Batch Process");
        JButton end = new JButton("Finish & Exit");
        end.setEnabled(false);
        start.addActionListener(e -> {
            try {
                Batch.run(mask,csvPAth,progressBar);
                start.setEnabled(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            end.setEnabled(true);
        });
        end.addActionListener(e -> {
            System.exit(0);
        });
        panel.add(start);
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        add(end,BorderLayout.PAGE_END);
    }
}
