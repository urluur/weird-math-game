import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Random;

public class GameGui {
    private JFrame frame = new JFrame();
    private JPanel gamePanel, buttonsPanel, topPanel;
    private JLabel targetValueLabel, currentSumLabel, movesLeftLabel;
    private Border defaultBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);
    private int selectedButtonRow = -1, selectedButtonCol = -1;
    private int movesLeft;
    private Settings settings;
    private GridButton[][] buttons;

    GameGui() {
        settings = new Settings();
        // TODO: main menu
        init(settings);
    }

    public void init(Settings settings){
        this.settings = settings;
        movesLeft = settings.getMovesLeft();

        // frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("More or less, less is more!");
        frame.setSize(800, 600);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        gamePanel = new JPanel(new BorderLayout());

        // panel
        buttonsPanel = new JPanel();
        buttonsPanel.setBorder(defaultBorder);
        buttonsPanel.setLayout(new GridLayout(settings.getNumOfRows(), settings.getNumOfCols()));

        // top panel
        topPanel = new JPanel(new GridLayout(0,3));
        topPanel.setBorder(defaultBorder);

        changeButtonGridSize(settings.getNumOfRows(), settings.getNumOfCols());

        targetValueLabel = new JLabel("Target value:");
        targetValueLabel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(new JLabel());
        topPanel.add(targetValueLabel, BorderLayout.CENTER);

        currentSumLabel = new JLabel("Current sum:");
        currentSumLabel.setVerticalAlignment(JLabel.CENTER);
        currentSumLabel.setBorder(defaultBorder);
        currentSumLabel.setHorizontalAlignment(JLabel.CENTER);
        gamePanel.add(currentSumLabel, BorderLayout.SOUTH);

        movesLeftLabel = new JLabel("Moves left:");
        movesLeftLabel.setHorizontalAlignment(JLabel.RIGHT);
        topPanel.add(movesLeftLabel, BorderLayout.EAST);


        gamePanel.add(topPanel, BorderLayout.NORTH);
        gamePanel.add(buttonsPanel, BorderLayout.CENTER);
        frame.add(gamePanel);
        frame.pack();
        frame.setVisible(true);

        printTargetValue();
        updateCurrentSum(settings.getNumOfRows(), settings.getNumOfCols());
        moveDone(true);
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

    public void printTargetValue() {
        this.targetValueLabel.setText("Target value: " + buttons[0][0].fromDoubleFormatString(settings.getTargetValue()));
    }

    public double updateCurrentSum(int rows, int cols) {
        double sum = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum = sum + buttons[i][j].getValue();
            }
        }
        currentSumLabel.setText("Current sum: " + buttons[0][0].fromDoubleFormatString(sum)); // would be better to use a static function
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

    public void addButtonActionListeners(int maxRow, int maxCol) {
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                addButtonActionListener(i, j, maxRow, maxCol);
            }
        }
    }

    public void addButtonActionListener(int currentX, int currentY, int maxRow, int maxCol) {
        buttons[currentX][currentY].addActionListener(e -> {
            if (selectedButtonRow == -1 && selectedButtonCol == -1) { // when first click
                selectedButtonRow = buttons[currentX][currentY].getRow();
                selectedButtonCol = buttons[currentX][currentY].getCol();
                createAvailableButtonsCross(maxRow, maxCol);
            }
            else { // all other buttons clicked after first one
                double result = 0;
                double arg1 = buttons[selectedButtonRow][selectedButtonCol].getValue();
                double arg2 = buttons[currentX][currentY].getValue();
                switch (settings.getOperator()) {
                    case '+':
                        result = arg1 + arg2;
                        break;
                }
                result = result % 10;
                buttons[selectedButtonRow][selectedButtonCol].setValue(result);
                selectedButtonRow = buttons[currentX][currentY].getRow();
                selectedButtonCol = buttons[currentX][currentY].getCol();
                createAvailableButtonsCross(maxRow, maxCol);
                updateCurrentSum(maxRow, maxCol);
            }
            moveDone(); // subtracts 1 from movesLeft and updates label
        });
    }

    public void createAvailableButtonsCross(int maxRow, int maxCol) {
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                if (selectedButtonRow == i && selectedButtonCol == j) {
                    buttons[i][j].setEnabled(false);
                }
                else if (selectedButtonRow == i || selectedButtonCol == j) {
                    buttons[i][j].setEnabled(true);
                }
                else {
                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }

    public void moveDone() {
        moveDone(false);
    }

    public void moveDone(boolean init) {
        if (!init) {
            movesLeft--;
            if (movesLeft <= 0 && updateCurrentSum(settings.getNumOfRows(), settings.getNumOfCols()) != settings.getTargetValue()) {
                gameOver();
            } else if (updateCurrentSum(settings.getNumOfRows(), settings.getNumOfCols()) == settings.getTargetValue()) {
                youWin();
            }
        }
        movesLeftLabel.setText("Moves left: " + buttons[0][0].fromDoubleFormatString(movesLeft));
    }

    public void gameOver() {
        frame.remove(gamePanel);
        frame.setVisible(false);
        frame.setVisible(true);

        JPanel gameOverPanel = new JPanel(new BorderLayout());
        JLabel gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);
        gameOverPanel.add(gameOverLabel, BorderLayout.CENTER);

        JButton retry = new JButton("retry");
        retry.addActionListener(e -> {
            init(settings);
            frame.remove(gameOverPanel);
            frame.setVisible(false);
            frame.setVisible(true);
        });
        gameOverPanel.add(retry, BorderLayout.SOUTH);
        frame.add(gameOverPanel);
    }

    public void youWin() {
        frame.remove(gamePanel);
        frame.setVisible(false);
        frame.setVisible(true);

        JPanel youWinPanel = new JPanel(new BorderLayout());
        JLabel youWinLabel = new JLabel("YOU WIN");
        youWinLabel.setHorizontalAlignment(JLabel.CENTER);
        youWinPanel.add(youWinLabel, BorderLayout.CENTER);

        JButton retry = new JButton("retry");
        retry.addActionListener(e -> {
            init(settings);
            frame.remove(youWinPanel);
            frame.setVisible(false);
            frame.setVisible(true);
        });
        youWinPanel.add(retry, BorderLayout.SOUTH);
        frame.add(youWinPanel);
    }
}
