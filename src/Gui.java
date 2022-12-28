import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Gui {
    private JFrame frame = new JFrame();
    private JPanel buttonsPanel, topPanel;
    private JLabel targetValue, currentSum, movesLeft;

    Gui() {
        // frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("More or less, less is more!");
        frame.setSize(800, 600);

        // panel
        buttonsPanel = new JPanel();
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonsPanel.setLayout(new GridLayout(9,9));

        // top panel
        topPanel = new JPanel(new GridLayout(0,3));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        addButtons();

        targetValue = new JLabel("Target value:");
        topPanel.add(targetValue, BorderLayout.CENTER);

        currentSum = new JLabel("Current sum:");
        frame.add(currentSum, BorderLayout.SOUTH);

        movesLeft = new JLabel("Moves left:");
        topPanel.add(movesLeft, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(buttonsPanel, BorderLayout.CENTER);
        // frame.pack();
        frame.setVisible(true);
    }

    private void addButtons() {
        Random rand = new Random();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JButton b = new JButton("" + rand.nextInt(9));
                buttonsPanel.add(b);
            }
        }
    }
}
