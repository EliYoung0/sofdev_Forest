import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Ignore
class UITest {

    public static void main(String[] args) {
        UI ui = new UI();
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setVisible(true);
        FileSelector f = (FileSelector)ui.getContentPane();
        f.getAddress().setText("./testresources/a/1.jpg");
        assertEquals(f.getAddress().getText(),"./testresources/a/1.jpg");
        f.getOpen().doClick();

        Circle c = (Circle)ui.getContentPane();
        c.setValues(1824,1216,980,0,12);
        c.getProceed().doClick();

        Thresholder t = (Thresholder)ui.getContentPane();
        JButton[] algs = t.getAlgButtons();
        algs[2].doClick();
        t.getProceedButton().doClick();

    }
}