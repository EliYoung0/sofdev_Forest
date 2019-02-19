import javax.swing.*;
import java.awt.*;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        removeBlue.removeBlueMain("./resources/meme.jpg");
        removeColour.removeColourMain();
        Scanner reader = new Scanner(System.in);
        System.out.print("What threshold would you like to use? (Values 0-255): \n");
        int threshold = reader.nextInt();
        BasicBlackAndWhite.BasicBlackAndWhiteMain();
        EventQueue.invokeLater(() -> {
            UI frame = new UI();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

}
