import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Random;

public class Gui {
    private JFrame frame = new JFrame();
    private JPanel buttonsPanel, topPanel;
    private JLabel targetValue, currentSum, movesLeft;

    private Border debugBorder = BorderFactory.createLineBorder(Color.GREEN, 3);
    private Border defaultBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    Gui() {
        // frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("More or less, less is more!");
        frame.setSize(800, 600);
        frame.setResizable(false);

        // panel
        buttonsPanel = new JPanel();
        buttonsPanel.setBorder(defaultBorder);
        buttonsPanel.setLayout(new GridLayout(9,9));

        // top panel
        topPanel = new JPanel(new GridLayout(0,3));
        topPanel.setBorder(defaultBorder);

        addButtons();

        targetValue = new JLabel("Target value:");
        targetValue.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(new JLabel());
        topPanel.add(targetValue, BorderLayout.CENTER);

        currentSum = new JLabel("Current sum:");
        currentSum.setVerticalAlignment(JLabel.CENTER);
        currentSum.setBorder(defaultBorder);
        currentSum.setHorizontalAlignment(JLabel.CENTER);
        frame.add(currentSum, BorderLayout.SOUTH);

        movesLeft = new JLabel("Moves left:");
        movesLeft.setHorizontalAlignment(JLabel.RIGHT);
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
