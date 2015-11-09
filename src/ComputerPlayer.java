import java.util.HashMap;
import java.util.List;

public class ComputerPlayer extends Player {
    public final int DISPLAY_POSITION_OFFSET = 1;
    private String MOVE_KEY = "Move";
    private String SCORE_KEY = "Score";
    private Integer DEPTH = 0;

    public ComputerPlayer(Counter counter, UserInterface userInterface) {
        super(counter, userInterface);
    }

    Board playTurn(Board board) {
        return board.playCounterInPosition(calculateNextMoveWithMinimax(board), counter);
    }

    public Integer calculateNextMoveWithMinimax(Board board) {
        DEPTH = board.numberOfOpenPositions();
        return indexToDisplayPosition(minimax(DEPTH, this.counter, board));
    }

    private Integer indexToDisplayPosition(HashMap<String, Integer> result) {
        return result.get(MOVE_KEY) + DISPLAY_POSITION_OFFSET;
    }

    private HashMap<String, Integer> minimax(Integer depth, Counter currentCounter, Board currentBoard) {
        Integer bestScore = setInitialBestScore(currentCounter);
        Integer bestMove = -1;
        List<Integer> openPositions = currentBoard.findOpenPositions();
        if (noMoreMovesNeeded(depth, currentBoard, openPositions)) {
            return calculateResult(currentBoard);
        }

        for (Integer move : openPositions) {
            Board currentStateOfBoard = currentBoard.newBoardWithNewMove(move, currentCounter);
            Integer score = minimax(depth - 1, currentCounter.opponentCounter(), currentStateOfBoard).get(SCORE_KEY);
            if (foundNewBestScore(currentCounter, bestScore, score)) {
                bestScore = score;
                bestMove = move;
            }
        }
        return createResultMap(bestScore, bestMove);
    }

    private boolean foundNewBestScore(Counter currentCounter, Integer bestScore, Integer score) {
        return this.counter == currentCounter && score >= bestScore ||
                this.counter != currentCounter && score <= bestScore;
    }

    private boolean noMoreMovesNeeded(Integer depth, Board currentBoard, List<Integer> openPositions) {
        return currentBoard.isGameOver() || /*depth == 0 ||*/ openPositions.size() == 0;
    }

    private Integer setInitialBestScore(Counter currentCounter) {
        return currentCounter == counter ? -1000000 : 1000000;
    }


    private HashMap<String, Integer> calculateResult(Board currentBoard) {
        HashMap<String, Integer> result = createResultMap(currentBoard.calculateBoardScore(counter), -1);
        return result;
    }

    private HashMap<String, Integer> createResultMap(int score, int move) {
        HashMap<String, Integer> result = new HashMap<>();
        result.put(SCORE_KEY, score);
        result.put(MOVE_KEY, move);
        return result;
    }
}
