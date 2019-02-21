import javax.swing.*;
import java.awt.*;


public class Main {

    public static void main(String[] args) {
        ExtractBlue.extractBlueMain("./resources/meme.jpg");
        RemoveColour.removeColourMain();
        BasicBlackAndWhite.BasicBlackAndWhiteMain();
        EventQueue.invokeLater(() -> {
            UI frame = new UI();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

}
