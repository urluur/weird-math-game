import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameGui {
    private JFrame frame = new JFrame();
    private JPanel buttonsPanel, topPanel;
    private JLabel targetValue, currentSum, movesLeft;
    private Border defaultBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    private int selectedButtonRow, selectedButtonCol;

    private GridButton[][] buttons;

    GameGui(int x, int y) {
        // frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("More or less, less is more!");
        frame.setSize(800, 600);
        frame.setResizable(true);

        // panel
        buttonsPanel = new JPanel();
        buttonsPanel.setBorder(defaultBorder);
        buttonsPanel.setLayout(new GridLayout(x, y));

        // top panel
        topPanel = new JPanel(new GridLayout(0,3));
        topPanel.setBorder(defaultBorder);

        changeButtonGridSize(x, y);

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
        frame.pack();
        frame.setVisible(true);
    }

    public void changeButtonGridSize(int x, int y) {
        buttons = new GridButton[x][y];
        addButtons(x, y);
    }

    public void addButtons(int x, int y) {
        Random rand = new Random();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                buttons[i][j] = new GridButton(rand.nextInt(9), i, j);
                buttonsPanel.add(buttons[i][j]);
            }
        }
        addButtonActionListeners(x, y);
    }

    public void printTargetValue(int targetValue) {
        this.targetValue.setText("Target value: " + targetValue);
    }

    public int updateCurrentSum(int rows, int cols) {
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum = sum + buttons[i][j].getValue();
            }
        }
        currentSum.setText("Current sum: " + sum);
        return sum;
    }

    public void setSelectedButtonRow(int selectedButtonRow) {
        this.selectedButtonRow = selectedButtonRow;
    }

    public void setSelectedButtonCol(int selectedButtonCol) {
        this.selectedButtonCol = selectedButtonCol;
    }

    public int getSelectedButtonRow() {
        return selectedButtonRow;
    }

    public int getSelectedButtonCol() {
        return selectedButtonCol;
    }

    public void addButtonActionListeners(int row, int col) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                addButtonActionListener(i, j);
            }
        }
    }
    public void addButtonActionListener(int i, int j) {
        buttons[i][j].addActionListener(e -> {
            selectedButtonRow = buttons[i][j].getRow();
            selectedButtonCol = buttons[i][j].getCol();
            System.out.println(selectedButtonRow + " " + selectedButtonCol);
        });
    }
}
