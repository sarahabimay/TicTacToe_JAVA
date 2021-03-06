package jttt.core.game;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import jttt.console.*;
import jttt.core.board.Board;
import jttt.core.players.ComputerPlayer;
import jttt.core.players.Player;
import jttt.guiapp.app.GUIAppSpy;
import jttt.guiapp.gamemaker.GUIGameMakerFake;
import jttt.guiapp.javafxcomponents.GUIBoardDisplayer;
import jttt.guiapp.javafxcomponents.GUIViewSpy;
import jttt.guiapp.players.GUIHumanPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jttt.core.board.Mark.O;
import static jttt.core.board.Mark.X;
import static jttt.core.game.GameType.HVH;
import static org.junit.Assert.assertEquals;

public class GameTest {

    private final int DEFAULT_DIMENSION = 3;
    private final int ZERO_DIMENSION_BOARD = 0;
    private Game defaultGame;
    private Game zeroGame;
    private FakeConsoleApp uiSpy;
    private OutputStream output;
    private InputStream inputStream;
    private Writer writer;

    @Before
    public void setUp() {
        output = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(output);
        inputStream = new ByteArrayInputStream("1".getBytes());
        uiSpy = new FakeConsoleApp(null, inputStream, writer);
        ConsolePlayerFactory playerFactory = new ConsolePlayerFactory(uiSpy);
        List<Player> players = playerFactory.findPlayersFor(HVH.getNumericGameType());

        defaultGame = new Game(
                new Board(DEFAULT_DIMENSION),
                players.get(0),
                players.get(1),
                new ConsoleBoardDisplayer(new FakeConsoleApp(new ConsoleGameMaker(), inputStream, writer)));
    }

    @Test
    public void noBoardCreatedYet() {
        ConsolePlayerFactory playerFactory = new ConsolePlayerFactory(uiSpy);
        List<Player> players = playerFactory.findPlayersFor(HVH.getNumericGameType());
        zeroGame = new Game(
                new Board(ZERO_DIMENSION_BOARD),
                players.get(0),
                players.get(1),
                new ConsoleBoardDisplayer(new ConsoleApp(new ConsoleGameMakerSpy(), inputStream, writer)));
        assertEquals(0, zeroGame.getBoardSize());
    }

    @Test
    public void gameReceivesBoardSize() {
        assertEquals(9, defaultGame.getBoardSize());
    }

    @Test
    public void gameReceivesValidGameType() {
        assertEquals(HumanPlayer.class, defaultGame.getPlayer(X).getClass());
        assertEquals(HumanPlayer.class, defaultGame.getPlayer(O).getClass());
    }

    @Test
    public void createGameFromInputs() {
        assertEquals(9, defaultGame.getBoardSize());
        assertEquals(HumanPlayer.class, defaultGame.getPlayer(X).getClass());
        assertEquals(HumanPlayer.class, defaultGame.getPlayer(O).getClass());
    }

    @Test
    public void askedForFirstPlayer() {
        Player playerA = defaultGame.getNextPlayer();
        assertEquals(X, playerA.getMark());
    }

    @Test
    public void gameAskedForBoard() {
        Board board = new Board(DEFAULT_DIMENSION);
        assertEquals(board.isEmpty(), defaultGame.getBoard().isEmpty());
    }

    @Test
    public void displayBoard() {
        List<Integer> initialState = new ArrayList<>(Arrays.asList(10, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        uiSpy.addDummyHumanMoves(initialState);
        ConsoleBoardDisplayerSpy displayer = new ConsoleBoardDisplayerSpy(null, null);
        Game game = new Game(new Board(DEFAULT_DIMENSION),
                new HumanPlayer(X, uiSpy),
                new ComputerPlayer(O),
                displayer);
        game.playAllAvailableMoves();
        assertEquals(true, displayer.hasBoardBeenDisplayed());
    }

    @Test
    public void createConsolePlayerGameExplicitlyWithPlayers() {
        List<Integer> initialState = new ArrayList<>(Arrays.asList(10, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        uiSpy.addDummyHumanMoves(initialState);
        ConsoleBoardDisplayerSpy displayer = new ConsoleBoardDisplayerSpy(uiSpy, new ConsoleDisplayStylerSpy());
        Game game = new Game(new Board(DEFAULT_DIMENSION),
                new HumanPlayer(X, uiSpy),
                new HumanPlayer(O, uiSpy),
                displayer);

        game.playAllAvailableMoves();
        assertEquals(true, game.getBoard().isGameOver());
        assertEquals(true, displayer.hasBoardBeenDisplayed());
    }

    @Test
    public void createGUIPlayerGameExplicitlyWithPlayers() {
        Board board = new Board(DEFAULT_DIMENSION);
        GUIHumanPlayer guiHumanPlayerX = new GUIHumanPlayer(X);
        GUIHumanPlayer guiHumanPlayerO = new GUIHumanPlayer(O);
        guiHumanPlayerX.setNextUserMove("1");
        guiHumanPlayerO.setNextUserMove("2");
        Game game = new Game(board,
                guiHumanPlayerX,
                guiHumanPlayerO,
                new GUIBoardDisplayer(new GUIAppSpy(new GUIGameMakerFake(), new GUIViewSpy(null))));

        game.playAllAvailableMoves();
        assertEquals(false, game.getBoard().isGameOver());
        assertEquals(1, game.getBoard().findPositions(X).size());
        assertEquals(1, game.getBoard().findPositions(O).size());
    }
}
