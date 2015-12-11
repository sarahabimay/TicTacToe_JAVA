package javafxgui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import jttt.Core.Board.Board;
import jttt.Core.Board.Mark;

public class GUIDisplay {

    public static final String GAME_HEADER = "TIC TAC TOE GAME!";
    public static final String WINNER_ANNOUNCEMENT = "PLAYER %s HAS WON!";
    public static final String DRAW_ANNOUNCEMENT = "THE GAME IS A DRAW!";
    private final int POSITION_OFFSET = 1;
    private final int DEFAULT_DIMENSION = 3;
    private final String RESULTS_LABEL = "RESULTS HERE";
    private final String RESULTS_TARGET_ID = "resultTarget";
    private Controller controller;
    private BorderPane border;
    private Scene scene;
    private StackPane root;

    public GUIDisplay() {
        this.controller = null;
        this.root = new StackPane();
        this.scene = new Scene(root, 700, 675);
        scene.getStylesheets().add(Main.class.getResource("javafxgui.css").toExternalForm());
        this.border = new BorderPane();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Node lookup(String id) {
        return scene.lookup(id);
    }

    public Scene displayGUI() {
        root.getChildren().add(generateBorderLayout(new Board(DEFAULT_DIMENSION)));
        return scene;
    }

    public GridPane displayBoard(Board board) {
        GridPane boardPane = createGameBoard(board);
        border.setCenter(boardPane);
        return boardPane;
    }

    public void disableBoard() {
        GridPane boardPane = (GridPane) border.getCenter();
        for (int i = 0; i < boardPane.getChildren().size(); i++) {
            boardPane.getChildren().get(i).setDisable(true);
        }
        border.setCenter(boardPane);
    }

    public void displayResult(Mark winner) {
        Text resultTarget = (Text) lookup("#resultTarget");
        resultTarget.setText(createResultAnnouncement(winner));
    }

    public BorderPane generateBorderLayout(Board board) {
        border.setTop(titleHeader());
        border.setCenter(createGameBoard(board));
        border.setBottom(resultFooter());
        border.setId("borderPane");
        return border;
    }

    public HBox titleHeader() {
        HBox titleBar = createHBox("titleBar");
        titleBar.getChildren().add(createTextElement(GAME_HEADER, "gameTitle"));
        return titleBar;
    }

    public VBox resultFooter() {
        VBox resultFooter = createVBox("footer");
        resultFooter.getChildren().add(createGameResultsTarget());
        resultFooter.getChildren().add(createPlayAgainButtonTarget());
        return resultFooter;
    }

    public String createResultAnnouncement(Mark winner) {
        if (winner.isEmpty()) {
            return announceDraw();
        }
        return announceWinner(winner);
    }

    public String announceDraw() {
        return DRAW_ANNOUNCEMENT;
    }

    public String announceWinner(Mark winner) {
        return String.format(WINNER_ANNOUNCEMENT, winner.toString());
    }

    public void makePlayAgainVisible() {
        Button playAgain = (Button) lookup("#playAgain");
        switchElementVisibility(playAgain, true);
    }

    public Button createPlayAgainButtonTarget() {
        Button playAgain = createButton("Play Again?", "playAgain");
        playAgain.setVisible(false);
        registerPlayAgainButtonWithHandler(new JavaFXButton(playAgain));
        return playAgain;
    }

    public void registerBoardButtonWithHandler(ClickableElement clickableElement) {
        registerElementWithHandler(clickableElement, new NewPlayerMoveEventHandler(controller));
    }

    private void registerPlayAgainButtonWithHandler(ClickableElement clickableElement) {
        registerElementWithHandler(clickableElement, new NewGameEventHandler(controller));
    }

    public void registerElementWithHandler(ClickableElement clickableElement, ClickEventHandler eventHandler) {
        clickableElement.setOnAction(eventHandler);
    }

    private GridPane createGameBoard(Board board) {
        GridPane boardGrid = createGridPane("gameBoard");
        boardGrid = generateBoardCells(board, boardGrid);
        return boardGrid;
    }

    private GridPane generateBoardCells(Board board, GridPane boardGrid) {
        int position = 0;
        for (int row = 0; row < board.getDimension(); row++) {
            for (int col = 0; col < board.getDimension(); col++) {
                Button cell = createButtonForBoard(board, position);
                boardGrid.add(cell, col, row);
                registerBoardButtonWithHandler(new JavaFXButton(cell));
                position++;
            }
        }
        return boardGrid;
    }

    private Button createButtonForBoard(Board board, int position) {
        return boardCell(position,
                cellForDisplay(board, position),
                shouldBeDisabled(board, position));
    }

    private boolean shouldBeDisabled(Board board, int position) {
        return board.findMarkAtIndex(position).isEmpty() ? false : true;
    }

    private String cellForDisplay(Board board, int position) {
        return cellContents(position, board.findMarkAtIndex(position));
    }

    private String cellContents(int position, Mark markAtIndex) {
        return markAtIndex.markOrPositionForDisplay(position);
    }

    private Button boardCell(int position, String text, boolean setDisabled) {
        Button cell = createButton(text, buttonID(position));
        cell.setDisable(setDisabled);
        return cell;
    }

    private String buttonID(int position) {
        return String.valueOf(position + POSITION_OFFSET);
    }

    private void switchElementVisibility(Node element, boolean isVisible) {
        element.setVisible(isVisible);
    }

    private GridPane createGridPane(String id) {
        GridPane boardGrid = new GridPane();
        boardGrid.setId(id);
        return boardGrid;
    }

    private Button createButton(String label, String id) {
        Button playAgain = new Button(label);
        playAgain.setId(id);
        return playAgain;
    }

    private Text createGameResultsTarget() {
        return createTextElement(RESULTS_LABEL, RESULTS_TARGET_ID);
    }

    private HBox createHBox(String id) {
        HBox titleBar = new HBox();
        titleBar.setId(id);
        return titleBar;
    }

    private VBox createVBox(String id) {
        VBox resultFooter = new VBox();
        resultFooter.setId(id);
        return resultFooter;
    }

    private Text createTextElement(String label, String id) {
        Text title = new Text(label);
        title.setId(id);
        return title;
    }
}
