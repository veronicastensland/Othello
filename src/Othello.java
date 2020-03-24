import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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

    public void GameOverDialogue(Player winner) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        String w = winner == playground.HumanPlayer ? "Human player wins" : "Computer player wins";
        VBox vbox = new VBox(new Text("Game Over\n" + w), new Button("Ok"));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();
    }

    public boolean TryPlayMove(Position pos) {
        if (playground.ValidMove(playground.board, pos, playground.HumanPlayer)) {
            playground.PlayHumanMove(pos);
            DrawBoard(gameBoard, playground);

            if (playground.GameEnded()) {
                return true;
            }

            if (playground.PossibleMovesExist(playground.ComputerPlayer)) {
                playground.PlayComputerMove();
                DrawBoard(gameBoard, playground);

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

        // Init
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

                // playground.board[x][y] = playground.HumanPlayer.tile;

                gameOver = TryPlayMove(pos);

                if (gameOver) {
                    gameBoard.setOnMouseClicked(null);
                    GameOverDialogue(playground.winner);
                }
            }
        });

        stage.setTitle("Othello");

        Scene scene = new Scene(gameBoard, WINDOWSIZE, WINDOWSIZE, Color.GREEN);
        stage.setScene(scene);
        stage.show();
    }

    // JavaFx förväntar sig även att main-metoden anropar launch.
    public static void main(String[] args) {
        launch(args);
    }
}