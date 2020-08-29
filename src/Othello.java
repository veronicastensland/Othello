import java.util.Optional;

import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

//==============================================
// Spelet Othella implementerat med minimax-algoritmen för att säkerställa bästa drag. 
//==============================================

// TODO Vad är slutkriterie för spel? Dialogruta och spela om.
// TODO Sista kolumnen funkar kanske inte x = 7

// Klassen Othello startar spelet och ansvarar för grafiken
public class Othello extends Application {
    Pane gameBoard;
    Playground playground;
    Stage myStage;

    private static final int TILE_SIZE = 60;
    private static final int WINDOWSIZE = 600;
    private boolean gameOver = false;

    public void DrawBoard(Pane gameBoard, Playground playground) {
        for (int y = 0; y < Playground.ROWS; y++) {
            for (int x = 0; x < Playground.COLUMNS; x++) {
                Rectangle tile = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                tile.setFill(null);
                tile.setStroke(Color.BLACK);

                if (playground.board[x][y] != Playground.NOTILE) {

                    Color playerColor = null;
                    if (playground.board[x][y] == playground.HumanPlayer.tile) {
                        playerColor = playground.HumanPlayer.color;
                    } else if (playground.board[x][y] == playground.ComputerPlayer.tile) {
                        playerColor = playground.ComputerPlayer.color;
                    }

                    Circle c = new Circle(x * TILE_SIZE + (TILE_SIZE / 2), y * TILE_SIZE + (TILE_SIZE / 2),
                            TILE_SIZE / 2);
                    c.setFill(playerColor);
                    gameBoard.getChildren().addAll(c);
                }
                gameBoard.getChildren().addAll(tile);
            }
        }
    }

    public void GameOverDialogueFxAlert() {
        Alert alert = new Alert(AlertType.CONFIRMATION);

        String w;
        int humanTiles = playground.CountTiles(playground.HumanPlayer);
        int computerTiles = playground.CountTiles(playground.ComputerPlayer);

        if (humanTiles > computerTiles) {
            playground.winner = playground.HumanPlayer;
            w = "Human player wins";
        } else if (computerTiles > humanTiles) {
            playground.winner = playground.ComputerPlayer;
            w = "Computer player wins";
        } else {
            playground.winner = null;
            w = "Its a draw";
        }

        alert.setTitle("Game Over");
        alert.setContentText(w + " Vill du spela igen (J/N)?");
        ButtonType okButton = new ButtonType("Ja", ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Nej", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(noButton) == okButton) {
            playground.Init();
            DrawBoard(gameBoard, playground);
            alert.close();

            myStage.setTitle("Othello (ny instans)");

            // TODO Vi måste göra reset här på JavaFX-fönstret
            Scene scene = new Scene(gameBoard, WINDOWSIZE, WINDOWSIZE, Color.GREEN);
            myStage.setScene(scene);
            myStage.show();
        } else {

        }
    }

    public void GameOverDialogueJOptionPane(Player winner) {
        String w = winner == playground.HumanPlayer ? "Human player wins" : "Computer player wins";
        int yesno = JOptionPane.showConfirmDialog(null, "Game Over!\nVill du spela igen (J/N)?", w,
                JOptionPane.YES_NO_OPTION);
        if (yesno == 0) {
            playground.Init();
        } else {
            // TODO Stäng fönster
        }
    }

    public boolean TryPlayMove(Position pos) {
        if (playground.ValidMove(playground.board, pos, playground.HumanPlayer)) {
            playground.PlayHumanMove(pos);

            if (playground.GameEnded()) {
                return true;
            }

            if (playground.PossibleMovesExist(playground.ComputerPlayer)) {
                playground.PlayComputerMove();
                if (playground.GameEnded()) {
                    return true;
                }
            }
        } else {
            System.out.println("Otillåtet drag");
        }
        return false;
    }

    // JavaFx kräver en start-metod som tar stage som parameter
    @Override
    public void start(Stage stage) {
        myStage = stage;
        gameBoard = new Pane();
        playground = new Playground();
        playground.Init();
        DrawBoard(gameBoard, playground);

        // Eventhandler för musklick. Datorn inväntar ett drag från den mänskliga
        // spelaren
        gameBoard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                gameOver = false;
                double posX = e.getX();
                double posY = e.getY();

                int x = (int) Math.floor(posX / TILE_SIZE);
                int y = (int) Math.floor(posY / TILE_SIZE);

                System.out.println("Mouse[x = " + posX + ", y = " + posY + "]  Pos[x = " + x + ", y = " + y + "]");
                Position pos = new Position(x, y);

                gameOver = TryPlayMove(pos);
                DrawBoard(gameBoard, playground);

                if (gameOver) {
                    GameOverDialogueFxAlert();
                }
            }
        });

        myStage.setTitle("Othello");

        Scene scene = new Scene(gameBoard, WINDOWSIZE, WINDOWSIZE, Color.GREEN);
        myStage.setScene(scene);
        myStage.show();
    }

    // JavaFx förväntar sig även att main-metoden anropar launch.
    public static void main(String[] args) {
        launch(args);
    }
}