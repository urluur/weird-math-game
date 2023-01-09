import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        mainMenu();
    }

    public void mainMenu() {
        mainMenu(new Settings());
    }

    public void mainMenu(Settings settings) {
        this.settings = settings;

        // frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("More or less, less is more!");
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        JPanel mainMenuPanel = new JPanel(new BorderLayout());
        JPanel startGamePanel = new JPanel(new GridLayout(1, 2));
        JButton startNewGameButton = new JButton("Start new game!");
        startNewGameButton.addActionListener(e -> {
            frame.remove(mainMenuPanel);
            init();
        });
        JButton loadFileButton = new JButton("Load from file...");
        loadFileButton.addActionListener(e -> {
            // TODO: add function for loading from file
        });
        loadFileButton.setEnabled(false);

        startGamePanel.add(startNewGameButton);
        startGamePanel.add(loadFileButton);

        mainMenuPanel.add(startGamePanel, BorderLayout.SOUTH);
        frame.add(mainMenuPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void init(){
        movesLeft = settings.getMovesLeft();

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
        this.targetValueLabel.setText("Target value: " + buttons[0][0].dblToFStr(settings.getTargetVal()));
    }

    public double updateCurrentSum(int rows, int cols) {
        double sum = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum = sum + buttons[i][j].getValue();
            }
        }
        currentSumLabel.setText("Current sum: " + buttons[0][0].dblToFStr(sum)); // would be better to use a static function
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
                    case '+' -> result = arg1 + arg2;
                    case '-' -> result = arg1 - arg2;
                    case '*' -> result = arg1 * arg2;
                    case '/' -> result = arg1 / arg2;
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
            double points = updateCurrentSum(settings.getNumOfRows(), settings.getNumOfCols());
            if (movesLeft <= 0 && points != settings.getTargetVal()) {
                String pointsStr = "You were " + buttons[0][0].dblToFStr(Math.abs(settings.getTargetVal() - points));
                postGame("YOU LOST! " + pointsStr + " point/s away from target number.");
            } else if (updateCurrentSum(settings.getNumOfRows(), settings.getNumOfCols()) == settings.getTargetVal()) {
                postGame("YOU WIN!");
            }
        }
        movesLeftLabel.setText("Moves left: " + buttons[0][0].dblToFStr(movesLeft));
    }


    public void postGame(String labelText) {
        frame.remove(gamePanel);

        JPanel postGamePanel = new JPanel(new BorderLayout());
        JPanel whatsNextButtons = new JPanel(new GridLayout(1, 2));

        JLabel postGameLabel = new JLabel(labelText);
        postGameLabel.setHorizontalAlignment(JLabel.CENTER);
        postGamePanel.add(postGameLabel);

        JButton changeSettingsButton = new JButton("Main menu...");
        changeSettingsButton.addActionListener(e -> {
            frame.remove(postGamePanel);
            mainMenu(settings);
        });
        JButton playAgainButton = new JButton("Play again!");
        playAgainButton.addActionListener(e -> {
            frame.remove(postGamePanel);
            init();
        });
        whatsNextButtons.add(playAgainButton);
        whatsNextButtons.add(changeSettingsButton);
        postGamePanel.add(whatsNextButtons, BorderLayout.SOUTH);
        frame.add(postGamePanel);
    }
}
