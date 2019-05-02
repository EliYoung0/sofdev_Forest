import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;

class BatchUI extends Container {

    private JProgressBar progressBar;
    private JTextArea output;
    private JButton finish;
    private boolean[][] mask;
    private String csvPath;

    class Batch extends SwingWorker<Void,Void>{

        @Override
        protected Void doInBackground() throws Exception {
            //This is tracked by a property change listener
            setProgress(0);

            Properties config = new Properties();
            InputStream input = new FileInputStream("config.properties");
            config.load(input);

            String threshold = config.getProperty("threshold");
            String path = config.getProperty("path");
            String[] paths = path.split(",");
            int method = Integer.parseInt(config.getProperty("method"));

            input.close();

            //For loop to run through everything
            File temp = new File(paths[0]);
            if(temp.isDirectory()) {
                File[] files = temp.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
                if(files==null){return null;}
                paths = new String[files.length-1];
                for (int a = 1; a < files.length; a++) {
                    paths[a-1]=files[a].getAbsolutePath();
                }
            }
            double gapFraction;
            for(int i=0; i<paths.length; i++){
                String[] methods= {"Manual","Nobis","Single Binary","DHP"};
                SquareTheCircle.createTheRectangle(paths[i]);
                BufferedImage square = ImageIO.read(new File(SquareTheCircle.getSquareFilepath()));
                BufferedImage black;
                if(method == 0) {
                    black = Black.makeBlack(square, Integer.parseInt(threshold), mask);
                    gapFraction = Black.getGapFraction(black, mask);
                }
                else if(method == 1) {
                    black = Algorithms.nobis(square, mask);
                    gapFraction = Black.getGapFraction(black, mask);
                }
                else if(method == 2){
                    black = Algorithms.single(square);
                    gapFraction = Black.getGapFraction(black, mask);
                }
                else {
                    black = Algorithms.dhp(paths[i]);
                    gapFraction = Black.getGapFraction(black, mask);

                }
                //Calculates gap fraction
                String[] data = new String[]{paths[i],methods[method],"N/A", String.valueOf(IndirectSiteFactor.getISF(black)),String.valueOf(gapFraction)};
                if(method==0){ data[2]= threshold; }

                //Write to the CSV
                CSV.writeTo(csvPath,data);
                //Increment progress bar
                setProgress(i*100/paths.length);
                SquareTheCircle.deleteSquare();
            }
            setProgress(100);
            return null;
        }

        @Override
        public void done(){
            output.append("Done.\n");
            finish.setEnabled(true);
        }
    }

    BatchUI(boolean[][] mask, String csv,UI ui){
        this.mask = mask;
        csvPath=csv;
        setLayout(new BorderLayout());

        //Create Components
        JPanel panel = new JPanel();
        output = new JTextArea(5,20);
        output.append("Press Start to begin the batch process.\n");
        JButton start = new JButton("Start");
        start.addActionListener(e -> {
            start.setEnabled(false);
            Batch batch = new Batch();
            batch.addPropertyChangeListener(ev ->{
                if ("progress".equals(ev.getPropertyName())) {
                    int progress = (Integer) ev.getNewValue();
                    progressBar.setValue(progress);
                    output.append(progress+"% done.\n");
                }
            });
            batch.execute();
        });
        panel.add(start);

        progressBar = new JProgressBar(0,100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        panel.add(progressBar);
        add(panel,BorderLayout.PAGE_START);
        output.setEditable(false);
        output.setMargin(new Insets(5,5,5,5));
        add(new JScrollPane(output),BorderLayout.CENTER);
        finish = new JButton("Finish & Exit");
        finish.addActionListener(e -> ui.dispatchEvent(new WindowEvent(ui, WindowEvent.WINDOW_CLOSING)));
        finish.setEnabled(false);
        add(finish,BorderLayout.PAGE_END);

    }
}
