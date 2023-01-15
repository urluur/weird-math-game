public class Settings {
    private int numOfRows = 7, numOfCols = 5, movesLeft = 20, targetValue = 150;

    public int getRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public int getCols() {
        return numOfCols;
    }

    public void setNumOfCols(int numOfCols) {
        this.numOfCols = numOfCols;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public void setMovesLeft(int movesLeft) {
        this.movesLeft = movesLeft;
    }

    public int getTargetVal() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }

    public String fileFriendlyString() {
        return numOfRows + "|" + numOfCols + "|" + movesLeft + "|" + targetValue;
    }
}
