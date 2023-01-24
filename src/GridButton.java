import javax.swing.*;

public class GridButton extends JButton {
    private final int row, col;
    private int value;

    GridButton(int randomNumber, int row, int col) {
        super(randomNumber + "");
        this.row = row;
        this.col = col;
        value = randomNumber;
        setFocusable(false);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.setText(value + "");
    }
}
