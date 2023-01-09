import javax.swing.*;

public class GridButton extends JButton {
    private final int row, col;
    private double value;

    GridButton(int randomNumber, int row, int col) {
        super(randomNumber + "");
        this.row = row;
        this.col = col;
        this.value = randomNumber;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        this.setText(fromDoubleFormatString(value));
    }

    public String fromDoubleFormatString (double in) {
        String strValue = in + "";
        if (strValue.endsWith(".0")) {
            strValue = strValue.replace(".0", "");
        }
        return strValue;
    }
}
