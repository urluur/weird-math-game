import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class GameGui {
    private final JFrame frame = new JFrame();
    private JPanel gamePanel, buttonsPanel, nextOperatorsPanel;
    private JLabel targetValueLabel, currentSumLabel, movesLeftLabel;
    private JLabel[] operatorsLabels;
    private final Border defaultBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);
    private int selectedButtonRow = -1, selectedButtonCol = -1;
    private int movesLeft;
    private Settings settings;
    private GridButton[][] buttons;
    private JSpinner rowSpinner, colSpinner, targetSpinner, moveSpinner;

    GameGui() {
        // change look and feel to nimbus
        // source: https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/index.html
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.put("nimbusBase", Color.gray); // spinners
                    UIManager.put("nimbusBlueGrey", Color.lightGray); //buttons
                    UIManager.put("control", Color.lightGray); // background


                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        mainMenu();
    }

    /**
     * Sets up main menu with default settings defined in Settings.java
     */
    public void mainMenu() {
        mainMenu(new Settings());
    }

    /**
     * Sets up main menu with with settings defined in argument object
     * Used when going to main menu after already finishing the game to keep previous settings
     * @param settings Settings object from which settings on spinners will be set
     */
    public void mainMenu(Settings settings) {
        this.settings = settings;

        // frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("More or less, less is more!");
        frame.setResizable(false);
        // frame.setLocationRelativeTo(null); // opens window in the center of the screen

        JPanel mainMenuPanel = new JPanel(new BorderLayout());
        JPanel settingsPanel = new JPanel(new GridLayout(2, 1));
        JPanel spinnersPanel = new JPanel(new GridLayout(2, 2));
        JPanel[] labelAndSpinnerPanels = new JPanel[4];
        for (int i = 0; i < 4; i++) {
            labelAndSpinnerPanels[i] = new JPanel(new GridLayout(1, 2));
        }
        JPanel difficultyPresetsPanel = new JPanel(new GridLayout(1, 4));

        // spinners
        /*
        sources:
        https://docs.oracle.com/javase/7/docs/api/javax/swing/JSpinner.html
        https://docs.oracle.com/javase/7/docs/api/javax/swing/SpinnerNumberModel.html
        */

        // rows
        JLabel rowsLabel = new JLabel("Rows: ");
        rowsLabel.setHorizontalAlignment(JLabel.RIGHT);
        SpinnerModel value = new SpinnerNumberModel(
                settings.getRows(), // default value
                2, // minimum number of rows
                10, // maximum number of rows
                1 // spinner step
        );
        rowSpinner = new JSpinner(value);
        labelAndSpinnerPanels[0].add(rowsLabel);
        labelAndSpinnerPanels[0].add(rowSpinner);

        // columns
        JLabel colsLabel = new JLabel("Columns: ");
        colsLabel.setHorizontalAlignment(JLabel.RIGHT);
        value = new SpinnerNumberModel(
                settings.getCols(), // default value
                2, // minimum number of columns
                10, // maximum number of columns
                1 // spinner step
        );
        colSpinner = new JSpinner(value);
        labelAndSpinnerPanels[1].add(colsLabel);
        labelAndSpinnerPanels[1].add(colSpinner);

        // Target value
        JLabel targetValueLabel = new JLabel("Target value: ");
        targetValueLabel.setHorizontalAlignment(JLabel.RIGHT);
        value = new SpinnerNumberModel(
                settings.getTargetVal(), // default value
                1, // minimum target value
                420, // maximum target value
                1 // spinner step
        );
        targetSpinner = new JSpinner(value);
        labelAndSpinnerPanels[2].add(targetValueLabel);
        labelAndSpinnerPanels[2].add(targetSpinner);

        // Available moves
        JLabel movesValueLabel = new JLabel("Available moves: ");
        movesValueLabel.setHorizontalAlignment(JLabel.RIGHT);
        value = new SpinnerNumberModel(
                settings.getMovesLeft(), // default value
                1, // minimum moves
                99, // maximum moves
                1 // spinner step
        );
        moveSpinner = new JSpinner(value);
        labelAndSpinnerPanels[3].add(movesValueLabel);
        labelAndSpinnerPanels[3].add(moveSpinner);
        // that's it for spinners

        // start game buttons
        JPanel startGamePanel = new JPanel(new GridLayout(1, 2));
        JButton startNewGameButton = new JButton("Start new game!");
        startNewGameButton.addActionListener(e -> {
            settings.setNumOfRows((int) rowSpinner.getValue());
            settings.setNumOfCols((int) colSpinner.getValue());
            settings.setTargetValue((int) targetSpinner.getValue());
            settings.setMovesLeft((int) moveSpinner.getValue());
            frame.remove(mainMenuPanel);
            init();
        });
        JButton loadFileButton = new JButton("Load from file...");
        loadFileButton.addActionListener(e -> {
            frame.remove(mainMenuPanel);
            load();
        });
        loadFileButton.setEnabled(readFromFilePossible());

        JButton easyPresetButton = new JButton("Easy");
        easyPresetButton.addActionListener(e -> setSpinnersTo(5, 5, 111, 60));

        JButton mediumPresetButton = new JButton("Medium");
        mediumPresetButton.addActionListener(e -> setSpinnersTo(6, 6, 150, 50));

        JButton hardPresetButton = new JButton("Hard");
        hardPresetButton.addActionListener(e -> setSpinnersTo(7, 7, 222, 30));

        JLabel presetsLabel = new JLabel("Presets:");
        presetsLabel.setHorizontalAlignment(JLabel.CENTER);
        difficultyPresetsPanel.add(presetsLabel);
        difficultyPresetsPanel.add(easyPresetButton);
        difficultyPresetsPanel.add(mediumPresetButton);
        difficultyPresetsPanel.add(hardPresetButton);

        for (int i = 0; i < 4; i++) {
            labelAndSpinnerPanels[i].setBorder(
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)
            );
            spinnersPanel.add(labelAndSpinnerPanels[i]);
        }

        settingsPanel.add(spinnersPanel);
        difficultyPresetsPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 2, 10, 2)
        );
        settingsPanel.add(difficultyPresetsPanel);

        startGamePanel.add(startNewGameButton);
        startGamePanel.add(loadFileButton);

        mainMenuPanel.add(settingsPanel, BorderLayout.CENTER);
        mainMenuPanel.add(startGamePanel, BorderLayout.SOUTH);
        frame.add(mainMenuPanel);
        frame.pack();
        frame.setVisible(true);
        frame.revalidate();
    }

    /**
     * Sets up game
     * Called when not reading from a file
     */
    public void init() {
        init(null, -1, null, null);
    }

    /**
     * Sets up game with state read from a file or arguments (null, -1, null, null) for new game
     * @param newSettings settings obtained from file; must be in order: rows, cols, movesLeft, targetValue
     * @param newMovesLeft moves left when player saved to file
     * @param newButtons buttons on the grid when player saved to file
     * @param newOperators operators in queue when player saved to file
     */
    public void init(String[] newSettings, int newMovesLeft, String[] newButtons, String[] newOperators){
        if (newMovesLeft == -1) {
            movesLeft = settings.getMovesLeft();
        } else {
            movesLeft = newMovesLeft;
        }

        if (newSettings != null) {
            settings.setNumOfRows(Integer.parseInt(newSettings[0]));
            settings.setNumOfCols(Integer.parseInt(newSettings[1]));
            settings.setMovesLeft(Integer.parseInt(newSettings[2]));
            settings.setTargetValue(Integer.parseInt(newSettings[3]));
        }

        selectedButtonRow = -1;
        selectedButtonCol = -1;
        frame.setResizable(true);

        gamePanel = new JPanel(new BorderLayout());

        // panel
        buttonsPanel = new JPanel();
        buttonsPanel.setBorder(defaultBorder);
        buttonsPanel.setLayout(new GridLayout(settings.getRows(), settings.getCols()));

        // top panel
        JPanel topPanel = new JPanel(new GridLayout(0, 3));
        topPanel.setBorder(defaultBorder);

        createButtonGrid(newButtons);

        // next operators panel
        nextOperatorsPanel = new JPanel(new GridLayout(settings.getRows(), 1));
        nextOperatorsPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 15)
        );
        setupNextOperators(newOperators);

        JButton saveButton = new JButton("Save & Quit");
        saveButton.addActionListener(e -> save());
        topPanel.add(saveButton);

        targetValueLabel = new JLabel("Target value:");
        targetValueLabel.setHorizontalAlignment(JLabel.CENTER);
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
        gamePanel.add(nextOperatorsPanel, BorderLayout.EAST);
        frame.add(gamePanel);

        printTargetValue();
        updateCurrentSum(settings.getRows(), settings.getCols());
        moveDone(true);
        frame.revalidate();

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Generates a grid of buttons with random values
     * @param newButtons null to generate new random values or operators read from file
     */
    public void createButtonGrid(String[] newButtons) {
        buttons = new GridButton[settings.getRows()][settings.getCols()];
        Random rand = new Random();
        int newButtonCount = 0;
        for (int i = 0; i < settings.getRows(); i++) {
            for (int j = 0; j < settings.getCols(); j++) {
                if (newButtons != null) {
                    buttons[i][j] = new GridButton(Integer.parseInt(newButtons[newButtonCount]), i, j);
                    newButtonCount++;
                } else {
                    buttons[i][j] = new GridButton(rand.nextInt(9), i, j);
                }
                buttonsPanel.add(buttons[i][j]);
            }
        }
        addButtonActionListeners();
    }

    /**
     * Sets spinners to determined values
     * @param rows number shown on rows spinner
     * @param cols number shown on columns spinner
     * @param target number shown on target number spinner
     * @param moves number shown on spinner moves left spinner
     */
    public void setSpinnersTo(int rows, int cols, int target, int moves) {
        rowSpinner.setValue(rows);
        colSpinner.setValue(cols);
        targetSpinner.setValue(target);
        moveSpinner.setValue(moves);
    }

    /**
     * Creates labels on the right of the screen
     * Only called when starting a game or loading a game
     * @param newOperators null for random operators or data read from file
     */
    public void setupNextOperators(String[] newOperators) {
        int newOperatorsCount = 0;
        operatorsLabels = new JLabel[settings.getRows()];
        for (int i = 0; i < settings.getRows(); i++) {
            if (newOperators != null && i < newOperators.length) {
                operatorsLabels[i] = new JLabel(newOperators[newOperatorsCount]);
                newOperatorsCount++;
            } else {
                operatorsLabels[i] = new JLabel(getRandOperator());
            }
            if(i < movesLeft) {
                if (i == 0) {
                    operatorsLabels[i].setFont(new Font("Arial", Font.BOLD, 22));
                    operatorsLabels[i].setHorizontalAlignment(JLabel.LEFT);
                }
                else {
                    operatorsLabels[i].setHorizontalAlignment(JLabel.CENTER);
                }
                nextOperatorsPanel.add(operatorsLabels[i]);
            }

        }
    }

    /**
     * Moves the operators up in queue after each move
     */
    public void updateOperators() {
        for (int i = 0; i < operatorsLabels.length; i++) { // goes trough all labels
            if (movesLeft <= operatorsLabels.length) { // there are less operators in queue than all labels
                if (i < movesLeft - 2) { // labels that will get text from next label
                    operatorsLabels[i].setText(operatorsLabels[i + 1].getText());
                } else if (i < movesLeft - 1) {
                    if (i == operatorsLabels.length - 1) { // can't access out of array
                        operatorsLabels[i].setText(getRandOperator());
                    } else {
                        operatorsLabels[i].setText(operatorsLabels[i + 1].getText());
                    }
                } else { // when there are less operators in queue than all labels
                    operatorsLabels[i].setText("");
                }
            } else { // there are more operators in queue than labels
                if (i < operatorsLabels.length - 1) {
                    operatorsLabels[i].setText(operatorsLabels[i + 1].getText());
                } else {
                    operatorsLabels[i].setText(getRandOperator());
                }
            }
        }
    }

    /**
     * Returns a random operator
     * @return a string with one of the following operators: + - * /
     */
    public String getRandOperator() {
        Random random = new Random();
        switch (random.nextInt(4)) {
            case 0 -> {
                return "+";
            }
            case 1 -> {
                return "-";
            }
            case 2 -> {
                return "*";
            }
            case 3 -> {
                return "/";
            }
            default -> {
                System.out.println("Error: random operator!");
                return "error";
            }
        }
    }

    /**
     * Sets the label text for target value
     */
    public void printTargetValue() {
        this.targetValueLabel.setText("Target value: " + settings.getTargetVal());
    }

    /**
     * Counts all numbers on the buttons on the grid
     * Updates the current sum label
     * @param rows number of rows of the button grid
     * @param cols number of columns of the button grid
     * @return sum of all numbers on the buttons
     */
    public int updateCurrentSum(int rows, int cols) {
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum = sum + buttons[i][j].getValue();
            }
        }
        currentSumLabel.setText("Current sum: " + sum);
        return sum;
    }

    /**
     * Adds action listeners to all buttons
     */
    public void addButtonActionListeners() {
        for (int i = 0; i < settings.getRows(); i++) {
            for (int j = 0; j < settings.getCols(); j++) {
                addButtonActionListener(i, j);
            }
        }
    }

    /**
     * Adds action listener to a button
     * @param currentRow current button's row
     * @param currentCol current button's column
     */
    public void addButtonActionListener(int currentRow, int currentCol) {
        buttons[currentRow][currentCol].addActionListener(e -> {
            if (selectedButtonRow == -1 && selectedButtonCol == -1) { // on first button click
                selectedButtonRow = buttons[currentRow][currentCol].getRow();
                selectedButtonCol = buttons[currentRow][currentCol].getCol();
                createAvailableButtonsCross();
            } else { // all other buttons clicked after first one
                int result = 0;
                int arg1 = buttons[selectedButtonRow][selectedButtonCol].getValue();
                int arg2 = buttons[currentRow][currentCol].getValue();
                switch (operatorsLabels[0].getText()) {
                    case "+" -> result = arg1 + arg2;
                    case "-" -> result = arg1 - arg2;
                    case "*" -> result = arg1 * arg2;
                    case "/" -> result = arg1 / arg2;
                }
                result = Math.abs(result % 10);
                buttons[selectedButtonRow][selectedButtonCol].setValue(result);
                if (selectedButtonRow != -1) {
                    updateOperators();
                }
                selectedButtonRow = buttons[currentRow][currentCol].getRow();
                selectedButtonCol = buttons[currentRow][currentCol].getCol();
                createAvailableButtonsCross();
                updateCurrentSum(settings.getRows(), settings.getCols());
                moveDone(); // subtracts 1 from movesLeft and updates label
            }
        });
    }

    /**
     * Disables numbers that aren't in the same row or column as selected button
     * Also disables all buttons with number 0 if current operator is /
     */
    public void createAvailableButtonsCross() {
        int numOfAvailableButtons = 0;
        for (int i = 0; i < settings.getRows(); i++) {
            for (int j = 0; j < settings.getCols(); j++) {
                if ((selectedButtonRow == i && selectedButtonCol == j) ||
                    (operatorsLabels[0].getText().equals("/") && buttons[i][j].getText().equals("0"))
                ) {
                    buttons[i][j].setEnabled(false);
                } else {
                    buttons[i][j].setEnabled(selectedButtonRow == i || selectedButtonCol == j);
                    if (buttons[i][j].isEnabled()) {
                        numOfAvailableButtons++;
                    }
                }
            }
        }
        if (numOfAvailableButtons == 0) { // instant game over if player can't press any buttons
            movesLeft = 0;
            moveDone();
        }
    }

    /**
     * Call when player plays their move
     */
    public void moveDone() {
        moveDone(false);
    }

    /**
     * Decrements movesLeft, updates currentSum, and checks win/lose
     * @param init true when initializing the game, false when playing normally
     */
    public void moveDone(boolean init) {
        if (!init) {
            movesLeft--;
            int points = updateCurrentSum(settings.getRows(), settings.getCols());
            if (movesLeft <= 0 && points != settings.getTargetVal()) {
                String pointsStr = "YOU WERE " + Math.abs(settings.getTargetVal() - points);
                postGame("YOU LOST! " + pointsStr + " POINT/S AWAY FROM TARGET NUMBER!");
            } else if (updateCurrentSum(settings.getRows(), settings.getCols()) == settings.getTargetVal()) {
                postGame("YOU WIN!");
            }
        }
        movesLeftLabel.setText("Moves left: " + movesLeft);
        frame.revalidate();
    }

    /**
     * Creates win or lose post-game screen
     * @param labelText text displayed to user
     */
    public void postGame(String labelText) {
        frame.remove(gamePanel);
        JPanel postGamePanel = new JPanel(new BorderLayout());
        JPanel whatsNextButtons = new JPanel(new GridLayout(1, 2));

        JLabel postGameLabel = new JLabel(labelText);
        postGameLabel.setOpaque(true);

        // win or lose picture
        // source: https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel
        BufferedImage myPicture;
        JLabel picLabel;
        postGameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        if (labelText.startsWith("YOU LOST!")) { // loser screen
            postGameLabel.setForeground(Color.WHITE);
            try {
                myPicture = ImageIO.read(new File("src/betaLoser.jpg"));
                picLabel = new JLabel(new ImageIcon(myPicture));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            postGameLabel.setBackground(Color.RED);
        } else { // winner screen
            try {
                myPicture = ImageIO.read(new File("src/sigmaSwagWinner.jpg"));
                picLabel = new JLabel(new ImageIcon(myPicture));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            postGameLabel.setBackground(Color.WHITE);

        }
        postGamePanel.add(picLabel, BorderLayout.CENTER);

        postGameLabel.setHorizontalAlignment(JLabel.CENTER);
        postGameLabel.setBorder(
                BorderFactory.createEmptyBorder(30, 20, 30, 20)
        );
        postGamePanel.add(postGameLabel, BorderLayout.NORTH);

        JButton changeSettingsButton = new JButton("Main menu...");
        changeSettingsButton.addActionListener(e -> {
            frame.remove(postGamePanel);
            mainMenu(settings); // keeps current settings
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
        frame.pack();
        frame.setResizable(false);
    }

    /**
     * Saves the current state of the game to a file
     */
    public void save() {
        File file = new File("src/saveData.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("Settings");
            writer.newLine();
            writer.write(settings.fileFriendlyString());
            writer.newLine();

            writer.write("Moves left");
            writer.newLine();
            writer.write(movesLeft + "");
            writer.newLine();

            writer.write("Buttons");
            writer.newLine();
            writer.write(fileFriendlyButtons());
            writer.newLine();

            writer.write("Next operators");
            writer.newLine();
            writer.write(fileFriendlyOperators());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        frame.dispose(); // closes the window and ends program
    }

    /**
     * Loads previous game state from file
     */
    public void load() {
        String [] newSettings;
        int newMovesLeft;
        String [] newButtons;
        String [] newOperators;

        File file = new File("src/saveData.txt");
        try (BufferedReader reader = new BufferedReader((new FileReader(file)))) {
            String line = reader.readLine();
            if (!line.equals("Settings")) {
                throw new Exception("Wrong file!");
            } else {
                line = reader.readLine();
                newSettings = line.split("\\|");
            }

            line = reader.readLine();
            if (!line.equals("Moves left")) {
                throw new Exception("Wrong file!");
            } else {
                line = reader.readLine();
                newMovesLeft = Integer.parseInt(line);
            }

            line = reader.readLine();
            if (!line.equals("Buttons")) {
                throw new Exception("Wrong file!");
            } else {
                line = reader.readLine();
                newButtons = line.split("\\|");
            }

            line = reader.readLine();
            if (!line.equals("Next operators")) {
                throw new Exception("Wrong file!");
            } else {
                line = reader.readLine();
                newOperators = line.split("\\|");
            }
            init(newSettings, newMovesLeft, newButtons, newOperators);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Generates a string with values of all buttons ready to write to file
     * @return string with all values seperated with pipe character |
     */
    public String fileFriendlyButtons() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < settings.getRows(); i++) {
            for (int j = 0; j < settings.getCols(); j++) {
                str.append(buttons[i][j].getValue());
                if (
                    i + 1 != settings.getRows() ||
                    j + 1 != settings.getCols()
                ) {
                    str.append("|");
                }
            }
        }
        return str.toString();
    }

    /**
     * Generates a string with all operators in queue ready to write to file
     * @return string with all operators seperated with pipe character |
     */
    public String fileFriendlyOperators() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < operatorsLabels.length; i++) {
            if (i == movesLeft) {
                break;
            }
            str.append(operatorsLabels[i].getText());
            if(i + 1 != operatorsLabels.length && i + 1 != movesLeft) {
                str.append("|");
            }
        }
        return str.toString();
    }

    /**
     * Checks if file has valid information for reading
     * @return true if file is possible to read from
     */
    public boolean readFromFilePossible() {
        File file = new File("src/saveData.txt");
        try (BufferedReader reader = new BufferedReader((new FileReader(file)))) {
            String line = reader.readLine();
            if (!line.equals("Settings")) {
                throw new Exception("Wrong file!");
            } else {
                reader.readLine();
            }

            line = reader.readLine();
            if (!line.equals("Moves left")) {
                throw new Exception("Wrong file!");
            } else {
                reader.readLine();
            }

            line = reader.readLine();
            if (!line.equals("Buttons")) {
                throw new Exception("Wrong file!");
            } else {
                reader.readLine();
            }

            line = reader.readLine();
            if (!line.equals("Next operators")) {
                throw new Exception("Wrong file!");
            } else {
                reader.readLine();
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
