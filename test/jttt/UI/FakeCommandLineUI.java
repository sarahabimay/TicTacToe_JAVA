package jttt.UI;

import jttt.core.game.GameMaker;
import jttt.core.board.Board;
import jttt.core.board.Mark;
import jttt.core.game.Game;

import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class FakeCommandLineUI extends CommandLineUI{
    private final int DEFAULT_DIMENSION = 3;
    private Game game;
    private List<Integer> dummyInputs;
    private int playerType;
    private int dummyDimension;
    private boolean hasBoardBeenDisplayedToUI = false;
    private boolean hasGameBeenCreated = false;

    public FakeCommandLineUI(GameMaker gameMaker, InputStream inputStream, Writer writer) {
        super(gameMaker, inputStream, writer);

        this.game = new Game(new Board(DEFAULT_DIMENSION),
                new HumanPlayer(Mark.X, this),
                new HumanPlayer(Mark.O, this),
                new UIDisplayer(this, new DisplayStyler()));
        this.playerType = -1;
        this.dummyDimension = 0;
        this.dummyInputs = new ArrayList<>();
    }

    @Override
    public void start() {
    }

    @Override
    public int requestNextPosition(Board board) {
        int nextMove = dummyInputs.remove(0);
        while (!validBoardPosition(nextMove, board)) {
            nextMove = dummyInputs.size() > 0 ? dummyInputs.remove(0) : -1;
        }
        return nextMove;
    }

    @Override
    public boolean validBoardPosition(int oneIndexedPosition, Board board) {
        return (0 < oneIndexedPosition && oneIndexedPosition <= board.boardSize()) &&
                !board.cellIsOccupied(oneIndexedPosition - 1);
    }

    @Override
    public int displayGreetingRequest() {
        return 1;
    }

    @Override
    public boolean validateContinueChoice(int playOrQuit) {
        return false;
    }

    @Override
    public int requestBoardDimension() {
        return dummyDimension;
    }

    @Override
    public int requestGameType() {
        if (!validate(playerType, this::validGameType)) {
            playerType = -1;
        }
        return playerType;
    }

    @Override
    public int requestPlayAgain() {
        return BinaryChoice.NO.getChoiceOption();
    }

    @Override
    public boolean validateDimension(int dimension) {
        return dimension >= 3;
    }

    @Override
    public boolean validReplayChoice(int instruction) {
        return 0 < instruction && instruction < 3;
    }

    @Override
    public boolean validGameType(int choice) {
        return choice == 1 || choice == 2 || choice == 3;
    }

    @Override
    public void displayResult(Mark winner) {
    }

    @Override
    public void displayBoardToUser(String boardForDisplay) {
        hasBoardBeenDisplayedToUI = true;
    }

    public void setGameType(int gameType) {
        this.playerType = gameType;
    }

    public void addDummyDimension(int dimension) {
        this.dummyDimension = dimension;
    }

    public void addDummyHumanMoves(List<Integer> inputs) {
        dummyInputs = inputs;
    }

    @Override
    public void createNewGameFromOptions(int gameType, int defaultDimension) {
        hasGameBeenCreated = true;
    }

    public boolean hasBoardBeenDisplayedToUI() {
        return hasBoardBeenDisplayedToUI;
    }

    public boolean isHasGameBeenCreated() {
        return hasGameBeenCreated;
    }
}
