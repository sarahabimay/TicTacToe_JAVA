package jttt.guiapp.javafxcomponents;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jttt.core.board.Board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static jttt.guiapp.javafxcomponents.JavaFxBoardComponent.GAME_BOARD_ID;
import static jttt.core.board.Mark.EMPTY;
import static jttt.core.board.Mark.X;
import static org.junit.Assert.assertEquals;

public class JavaFxGameLayoutComponentTest {
    private final int DEFAULT_BOARD_DIMENSION = 3;
    private final int GUI_WINDOW_HEIGHT = 700;
    private final int GUI_WINDOW_WIDTH = 600;
    private GUIViewSpy guiViewSpy;
    private JavaFxGameLayoutComponent layout;

    @Before
    public void setUp() {
        new JFXPanel();
        guiViewSpy = new GUIViewSpy(new Scene(new StackPane(), GUI_WINDOW_HEIGHT, GUI_WINDOW_WIDTH));
        layout = new JavaFxGameLayoutComponent(new Board(DEFAULT_BOARD_DIMENSION), guiViewSpy);
    }

    @Test
    public void layoutHasThreePanels() {
        assertEquals(3, layout.getChildren().size());
    }

    @Test
    public void thereIsATitleBar() {
        Assert.assertEquals(JavaFxGameLayoutComponent.TITLE_BAR_ID, layout.getTop().getId());
    }

    @Test
    public void thereIsAFooter() {
        Assert.assertEquals(JavaFxGameLayoutComponent.FOOTER_ID, layout.getBottom().getId());
    }

    @Test
    public void theFooterHasTwoChildren() {
        VBox results = (VBox) layout.getBottom();
        assertEquals(2, results.getChildren().size());
        Assert.assertEquals(JavaFxGameLayoutComponent.RESULTS_TARGET_ID, results.getChildren().get(0).getId());
        Assert.assertEquals(JavaFxGameLayoutComponent.PLAY_AGAIN_ID, results.getChildren().get(1).getId());
    }

    @Test
    public void disableBoard() {
        layout.disableGameBoard();
        GridPane gameBoard = (GridPane)layout.getCenter();
        assertEquals(true, gameBoard.isDisabled());
    }

    @Test
    public void playAgainButtonIsDisplayed() {
        layout.displayPlayAgainButton();
        VBox results = (VBox) layout.getBottom();
        Button playAgain = (Button) results.getChildren().get(1);
        assertEquals(true, playAgain.isVisible());
    }

    @Test
    public void thePlayAgainButtonHasEventHandler() {
        VBox results = (VBox) layout.getBottom();
        Button button = (Button) results.getChildren().get(1);
        Assert.assertEquals(JavaFxGameLayoutComponent.PLAY_AGAIN_ID, button.getId());
        button.fire();
        assertEquals(true, guiViewSpy.hasReplayGameBeenSelected());
    }

    @Test
    public void thereIsAGridPaneInCenter() {
        assertEquals(GAME_BOARD_ID, layout.getCenter().getId());
    }

    @Test
    public void displayWinningResult() {
        layout.displayResult(X);
        VBox results = (VBox) layout.getBottom();
        Text result = (Text) results.getChildren().get(0);
        assertEquals(String.format(layout.WINNER_ANNOUNCEMENT, X.toString()), result.getText());
    }

    @Test
    public void displayDrawResult() {
        layout.displayResult(EMPTY);
        VBox results = (VBox) layout.getBottom();
        Text result = (Text) results.getChildren().get(0);
        assertEquals(layout.DRAW_ANNOUNCEMENT, result.getText());
    }
}
