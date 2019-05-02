import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class FileSelectorTest {
    private UI ui;
    @BeforeEach
    void setUp() {
        ui = new UI();
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Test
    void singleFileTest(){
        FileSelector f = (FileSelector)ui.getContentPane();
        f.getAddress().setText("./testresources/a/1.jpg");
        f.getOpen().doClick();
        assertTrue(f.getGoodExit());
    }

    @Test
    void multiFileTest(){
        FileSelector f = (FileSelector)ui.getContentPane();
        f.getAddress().setText("./testresources/a/1.jpg,./testresources/a/2.jpg");
        f.getOpen().doClick();
        assertTrue(f.getGoodExit());
    }

    @Test
    void directoryTest(){
        FileSelector f = (FileSelector)ui.getContentPane();
        f.getAddress().setText("./testresources/a/");
        f.getOpen().doClick();
        assertTrue(f.getGoodExit());
    }

    @AfterEach
    void tearDown() {
        ui.dispose();
    }
}