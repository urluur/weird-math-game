public class Game {
    private int numOfRows = 9, numOfCols = 9;
    private int targetValue = 0, currentSum = 0, movesLeft = 0;
    private GameGui gameInstance;

    Game() {
        init();
    }

    public void init() {
        gameInstance = new GameGui(numOfRows, numOfCols);
        gameInstance.printTargetValue(targetValue);
        currentSum = gameInstance.updateCurrentSum(numOfRows, numOfCols);
    }
}
