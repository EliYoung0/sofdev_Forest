import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UITest {

    public static void main(String[] args) {
        UI ui = new UI();
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setVisible(true);
        FileSelector f = (FileSelector)ui.getContentPane();
        f.getAddress().setText("/Users/kepler/Desktop/a");
        assertEquals(f.getAddress().getText(),"/Users/kepler/Desktop/a");
        f.getOpen().doClick();

        Circle c = (Circle)ui.getContentPane();
        c.setValues(1824,1216,980,0);
        c.getProceed().doClick();

    }
}