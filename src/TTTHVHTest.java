import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TTTHVHTest {

    private TTTHvHGame game;

    @Before
    public void setUp() throws Exception {
        game = new TTTHvHGame();
    }

    @Test
    public void start3x3GameWhichReturnsBoardGraphicAndPromptForPlayer1() {
        assertEquals("[1][2][3]\n" +
                        "[4][5][6]\n" +
                        "[7][8][9]\n\n" +
                        "Player1(X) please select a cell[1-9]:\n",
                game.restartGame(new Board(3)));
        assertEquals(game.boardSize(), 9);
    }

    @Test
    public void start4x4GameWhichReturnsBoardGraphicAndPromptForPlayer1() {
        assertEquals("[1][2][3][4]\n" +
                        "[5][6][7][8]\n" +
                        "[9][10][11][12]\n" +
                        "[13][14][15][16]\n\n" +
                        "Player1(X) please select a cell[1-16]:\n",
                game.restartGame(new Board(4)));
        assertEquals(game.boardSize(), 16);
    }

    @Test
    public void playerEntersACellNumberAndNewBoardDisplayed() {
        game.restartGame(new Board(3));
        assertEquals("[X][2][3]\n" +
                        "[4][5][6]\n" +
                        "[7][8][9]\n\n" +
                        "Player2(Y) please select a cell[1-9]:\n",
                game.playMove(1));
    }

    @Test
    public void player1ThenPlayer2TakeATurnAndNewBoardDisplayed() {
        game.restartGame(new Board(3));
        game.playMove(2);
        game.playMove(3);
        game.playMove(4);
        assertEquals("[O][X][O]\n" +
                        "[X][5][6]\n" +
                        "[7][8][9]\n\n" +
                        "Player1(X) please select a cell[1-9]:\n",
                game.playMove(1));

    }

    @Test
    public void noWinAfterCounterIsPlayed() {
        String initialBoard[] = {"X", "2", "3", "4", "5", "6", "7", "8", "9"};
        TTTHvHGame game = new TTTHvHGame(new Board(initializeBoard(initialBoard)), "Player2");
        assertEquals(false, game.isGameOver());
    }

    private List<String> initializeBoard(String[] initialBoard) {
        List<String> initialCells = new ArrayList<>(initialBoard.length);
        for (int i = 0; i < initialBoard.length; i++) {
            initialCells.add(String.valueOf(initialBoard[i]));
        }
        return initialCells;
    }
}
