import javax.swing.*;
import java.awt.*;


public class Main {

    public static void main(String[] args) {
        removeBlue.removeBlueMain("./resources/meme.jpg");
        removeColour.removeColourMain();
        BasicBlackAndWhite.BasicBlackAndWhiteMain();
        EventQueue.invokeLater(() -> {
            UI frame = new UI();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

}
