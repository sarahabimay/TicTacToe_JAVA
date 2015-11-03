import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int POSITIVE_OFFSET = 1;
    private static final int NEGATIVE_OFFSET = -1;
    private int dimension;
    private List<Counter> cells;

    public Board(int dimension) {
        this.dimension = dimension;
        this.cells = new ArrayList<>(generateEmptyCells());
    }

    public Board(int dimension, List<Counter> initialState) {
        this.dimension = dimension;
        this.cells = initialState;
    }

    public Board(List<Counter> cells) {
        this.dimension = (int) Math.sqrt(cells.size());
        this.cells = cells;
    }

    public void resetBoard() {
        this.cells = generateEmptyCells();
    }

    public Board playCounterInPosition(Integer position, Counter counter) {
        if (validPosition(position)) {
            cells.set(position - POSITIVE_OFFSET, counter);
        }
        return new Board(cells);
    }

    public boolean isGameOver() {
        return isAWinner() || !areEmptyPositions();
    }

    public Counter getWinner() {
        if (findWin(Counter.X)) {
            return Counter.X;
        }
        if (findWin(Counter.O)) {
            return Counter.O;
        }
        return Counter.EMPTY;
    }

    public boolean findWin(Counter searchCounter) {
        return hasRowWin(searchCounter) ||
                hasColumnWin(searchCounter) ||
                hasDiagonalWin(searchCounter);
    }

    public boolean hasRowWin(Counter searchCounter) {
        for (int row = 0; row < boardSize(); row += dimension) {
            if (rowWin(row, searchCounter)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasColumnWin(Counter searchCounter) {
        for (int column = 0; column < dimension; column++) {
            if (columnWin(column, searchCounter)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDiagonalWin(Counter searchCounter) {
        return diagonalWin(0, searchCounter) ||
                diagonalWin(dimension - 1, searchCounter);
    }

    protected boolean isAWinner() {
        return getWinner() != Counter.EMPTY;
    }

    public boolean validPosition(Integer position) {
        return position != null && positionIsWithinRange(position) && !cellIsOccupied(position - POSITIVE_OFFSET);
    }

    public boolean cellIsOccupied(int cellIndex) {
        return cells.get(cellIndex) == Counter.X || cells.get(cellIndex) == Counter.O;
    }

    protected int numberOfOpenPositions() {
        return boardSize() - numberOfPositionsTaken();
    }

    protected ArrayList<String> findPositions(Counter counter) {
        ArrayList<String> counterPositions = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            if (cellValue(i) == counter) {
                counterPositions.add(String.valueOf(i));
            }
        }
        return counterPositions;
    }

    protected List<Counter> getCells() {
        return cells;
    }

    protected int boardSize() {
        return dimension * dimension;
    }

    protected Counter cellValue(int startIndex) {
        return cells.get(startIndex);
    }

    private boolean areEmptyPositions() {
        return boardSize() != numberOfPositionsTaken();
    }

    private int numberOfPositionsTaken() {
        return findPositions(Counter.X).size() + findPositions(Counter.O).size();
    }

    private boolean positionIsWithinRange(int position) {
        return 0 < position && position <= boardSize();
    }


    private boolean rowWin(int rowIndex, Counter searchCounter) {
        Counter counter = cellValue(rowIndex);
        if (counter == searchCounter) {
            return searchRowForWin(rowIndex, counter);
        }
        return false;
    }

    private boolean searchRowForWin(int rowIndex, Counter counter) {
        for (int i = rowIndex; i < (rowIndex + dimension); i++) {
            if (cellValue(i) != counter) {
                return false;
            }
        }
        return true;
    }

    private boolean columnWin(int columnIndex, Counter searchCounter) {
        Counter counter = cellValue(columnIndex);
        if (counter == searchCounter) {
            return searchColumnForWin(columnIndex, counter);
        }
        return false;
    }

    private boolean searchColumnForWin(int columnIndex, Counter counter) {
        for (int i = columnIndex; i < boardSize(); i += dimension) {
            if (cellValue(i) != counter) {
                return false;
            }
        }
        return true;
    }

    private boolean diagonalWin(int startIndex, Counter searchCounter) {
        Counter counterToMatch = cellValue(startIndex);
        if (counterToMatch == searchCounter) {
            return searchDiagonalForWin(startIndex, counterToMatch);
        }
        return false;
    }

    private boolean searchDiagonalForWin(int startIndex, Counter counterToMatch) {
        int nextCellIncrement = determineNextDiagonalCellIncrement(startIndex);
        int lastCellIndex = determineLastIndexForDiagonal(startIndex);
        for (int i = startIndex + nextCellIncrement; i < lastCellIndex; i += nextCellIncrement) {
            if (!isMatch(counterToMatch, i)) {
                return false;
            }
        }
        return true;
    }

    private int determineLastIndexForDiagonal(int startIndex) {
        int directionalOffset = determineDiagonalIndexDirection(startIndex);
        return boardSize() + directionalOffset;
    }

    private int determineNextDiagonalCellIncrement(int startIndex) {
        int directionalOffset = determineDiagonalIndexDirection(startIndex);
        return startIndex == 0 ? dimension + directionalOffset : dimension + (directionalOffset);
    }

    private int determineDiagonalIndexDirection(int startIndex) {
        return startIndex == 0 ? POSITIVE_OFFSET : NEGATIVE_OFFSET;
    }

    private boolean isMatch(Counter counterToMatch, int indexToCompare) {
        return cellValue(indexToCompare) == counterToMatch;
    }


    private List<Counter> generateEmptyCells() {
        List<Counter> initialCells = new ArrayList<>(boardSize());
        for (int i = 0; i < boardSize(); i++) {
            initialCells.add(Counter.EMPTY);
        }
        return initialCells;
    }
}
