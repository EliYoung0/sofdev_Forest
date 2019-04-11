import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;

class BatchUI extends Container {
    private static JProgressBar progressBar;
    private JTextArea taskOutput;

    static class Batch {
        static void run(boolean[][] mask, String csvPath, JProgressBar progressBar) throws IOException {
            Properties config = new Properties();
            InputStream input = new FileInputStream("config.properties");
            config.load(input);

            String threshold = config.getProperty("threshold");
            String path = config.getProperty("path");
            String[] paths = path.split(","); //This needs to be done to turn the string into an array to be able to run through all of the paths
            int method = Integer.parseInt(config.getProperty("method"));

            double north = Double.parseDouble(config.getProperty("north"));
            int yCenter = Integer.parseInt(config.getProperty("yCenter"));
            int radius = Integer.parseInt(config.getProperty("radius"));
            int xCenter = Integer.parseInt(config.getProperty("xCenter"));

            Prop.deleteProperties();
            input.close();

            //For loop to run through everything
            File temp = new File(paths[0]);
            if(temp.isDirectory()) {
                File[] files = temp.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
                if(files==null){return;}
                paths = new String[files.length];
                for (int a = 1; a < files.length; a++) {
                    paths[a]=files[a].getAbsolutePath();
                }
            }
            double gapFraction = -1.0;
            int pct;
            for(int i=1; i<paths.length; i++){
                String[] methods= {"Manual","Nobis","Single Binary"};
                BufferedImage original = ImageIO.read(new File(paths[i]));
                BufferedImage square = SquareTheCircle.buildASquare(original);
                if(method == 0) {
                    BufferedImage black = Black.makeBlack(square, Integer.parseInt(threshold), mask);
                    gapFraction = Black.getGapFraction(black, mask);
                }
                else if(method == 1) {
                    BufferedImage black = Algorithms.nobis(square, mask);
                    gapFraction = Black.getGapFraction(black, mask);
                }
                else if(method == 2){
                    BufferedImage black = Algorithms.single(square);
                    gapFraction = Black.getGapFraction(black, mask);
                }
            /*
            ADD IN HERE ANY OTHER THRESHOLDING METHODS
             */
                String[] data = new String[]{paths[i],methods[method],"N/A","",String.valueOf(gapFraction)};
                if(method==0){
                    data[2]=String.valueOf(threshold);
                }
                //Write to the CSV
                CSV.writeTo(csvPath,data);
                pct = (i*100/paths.length);
                progressBar.setValue(pct);
                progressBar.setStringPainted(true);
            }
            progressBar.setValue(100);

        }
    }

    BatchUI(boolean[][] mask,String csvPAth) {
        setLayout(new BorderLayout());
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        JPanel panel = new JPanel();
        JButton start = new JButton("Batch Process");
        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
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
        add(taskOutput,BorderLayout.CENTER);

        add(panel, BorderLayout.PAGE_START);
        add(end,BorderLayout.PAGE_END);
    }
}
