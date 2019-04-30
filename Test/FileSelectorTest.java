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
        f.getAddress().setText("/Users/kepler/Desktop/a/1.jpg");
        f.getOpen().doClick();
        assertTrue(f.getGoodExit());
        //assertEquals(f.getFull(),new String[]{"/Users/kepler/Desktop/a/1.jpg"});
    }

    @Test
    void multiFileTest(){
        FileSelector f = (FileSelector)ui.getContentPane();
        f.getAddress().setText("/Users/kepler/Desktop/a/1.jpg,/Users/kepler/Desktop/a/2.jpg");
        f.getOpen().doClick();
        assertTrue(f.getGoodExit());
        //assertEquals(f.getFull(),new String[]{"/Users/kepler/Desktop/a/1.jpg","/Users/kepler/Desktop/a/2.jpg"});
    }

    @Test
    void directoryTest(){
        FileSelector f = (FileSelector)ui.getContentPane();
        f.getAddress().setText("/Users/kepler/Desktop/a");
        f.getOpen().doClick();
        //assertEquals(f.getFull(),new String[]{"/Users/kepler/Desktop/a"});
        assertTrue(f.getGoodExit());
    }

    @AfterEach
    void tearDown() {
        ui.dispose();
    }
}